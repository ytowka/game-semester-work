package org.danilkha.connection.client;

import java.net.Socket;

public class ClientConnection{

    private final int id;
    private final Socket socket;

    private final Thread thread;

    public ClientConnection(int id, Socket socket) {
        this.id = id;
        this.socket = socket;

        thread = new Thread(this::run);
    }

    private void run(){
        while (true){

        }
    }

    public void start(){
        thread.start();
    }

}
