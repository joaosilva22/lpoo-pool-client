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
    private LinkedBlockingQueue<String> messages;
    private boolean connected;

    /**
     * Creates a Client object.
     * Handles the connection between the application and the server.
     * @param IPAdress The server's IP adress.
     * @param port The server's port.
     * @param gameScreen The game screen responsible for the connection.
     */
    public Client(String IPAdress, int port, final GameScreen gameScreen) {
        SocketHints hints = new SocketHints();
        hints.connectTimeout = 5000;
        System.out.println("Atempting to connect to IP " + IPAdress);
        client = Gdx.net.newClientSocket(Net.Protocol.TCP, IPAdress, port, hints);
        connected = true;

        in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        messages = new LinkedBlockingQueue<String>();

        // Thread para ler as mensagens recebidas pelo servidor
        // (Evita bloquear a thread principal)
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (!Thread.currentThread().isInterrupted()) {
                        String toRead = in.readLine();
                        if (toRead == null) {
                            connected = false;
                            return;
                        }
                        System.out.println("Read:" + toRead);
                        messages.put(toRead);
                    }
                } catch (IOException e) {
                    //e.printStackTrace();
                    connected = false;
                    return;
                } catch (InterruptedException e) {
                    connected = false;
                    return;
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
                        System.out.println("handing:" + message.toJson());
                        gameScreen.handleMessage(message);
                    } catch (InterruptedException e) {
                        //e.printStackTrace();
                        return;
                    }
                }
            }
        }).start();
    }

    /**
     * Writes message to server.
     * @param message Message to write.
     */
    public void write(String message) {
        try {
            String toSend = message + "\n";
            client.getOutputStream().write(toSend.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Disconnects the client.
     */
    public void disconnect() {
        client.dispose();
    }

    /**
     * Returns wheter or not the client is still connected to the server.
     * @return Flag connected.
     */
    public boolean isConnected() {
        return connected;
    }
}
