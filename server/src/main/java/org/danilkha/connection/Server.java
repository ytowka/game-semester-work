package org.danilkha.connection;

import org.danilkha.connection.client.ClientConnection;
import org.danilkha.game.GameActionListener;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {

    private final ServerSocket serverSocket;
    private final List<ClientConnection> clientConnectionList;
    private final GameActionListener actionListener;

    private final int tickRate;

    private int idSequence = 0;

    public Server(int port, GameActionListener listener, int tickRate) throws IOException {
        serverSocket = new ServerSocket(port);
        clientConnectionList = new ArrayList<>();
        actionListener = listener;
        this.tickRate = tickRate;
    }

    public void start(){
        while (true){
            try {
                Socket socket = serverSocket.accept();
                ClientConnection clientConnection = new ClientConnection(idSequence, socket);
                idSequence++;
                clientConnectionList.add(clientConnection);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
