package com.mygdx.game.network;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;
import com.badlogic.gdx.utils.GdxRuntimeException;

import java.io.IOException;

/**
 * Created by joaopsilva on 03-06-2016.
 */
public class Client {
    private Socket client;

    public Client(String IPAdress, int port) {
        SocketHints hints = new SocketHints();
        System.out.println("Atempting to connect...");
        client = Gdx.net.newClientSocket(Net.Protocol.TCP, IPAdress, port, hints);
    }

    public void write(String message) {
        try {
            client.getOutputStream().write((message + "\n").getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isConnected() {
        return client.isConnected();
    }
}
