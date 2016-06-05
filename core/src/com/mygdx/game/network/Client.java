package com.mygdx.game.network;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;
import com.badlogic.gdx.utils.Json;
import com.mygdx.game.screens.GameScreen;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by joaopsilva on 03-06-2016.
 */
public class Client {
    private Socket client;
    private BufferedReader in;
    // TODO: ver o que e uma linked blocking queue :^D
    private LinkedBlockingQueue<String> messages;

    public Client(String IPAdress, int port, final GameScreen gameScreen) {
        SocketHints hints = new SocketHints();
        hints.connectTimeout = 5000;
        System.out.println("Atempting to connect to IP " + IPAdress);
        client = Gdx.net.newClientSocket(Net.Protocol.TCP, IPAdress, port, hints);

        in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        messages = new LinkedBlockingQueue<String>();

        // Thread para ler as mensagens recebidas pelo servidor
        // (Evita bloquear a thread principal)
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        messages.put(in.readLine());
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        // Thread para handling das mensagens
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Json json = new Json();
                        Message message = json.fromJson(Message.class, messages.take());
                        // TODO: handling das mensagens
                        gameScreen.handleMessage(message);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public void write(String message) {
        try {
            String toSend = message + "\n";
            client.getOutputStream().write(toSend.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isConnected() {
        return client.isConnected();
    }
}
