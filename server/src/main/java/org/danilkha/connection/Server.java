package org.danilkha.connection;

import org.danilkha.connection.client.ClientConnection;
import org.danilkha.game.GameActionListener;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

public class Server {

    private final ServerSocket serverSocket;
    private final List<ClientConnection> clientConnectionList;
    private final GameActionListener actionListener;

    private final int tickRate;

    public Server(int port, GameActionListener listener, int tickRate) throws IOException {
        serverSocket = new ServerSocket(port);
        clientConnectionList = new ArrayList<>();
        actionListener = listener;
        this.tickRate = tickRate;
    }

    public void start(){

    }
}
