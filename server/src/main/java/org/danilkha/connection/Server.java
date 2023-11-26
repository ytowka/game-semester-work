package org.danilkha.connection;

import org.danilkha.connection.api.ClientPackageReceiver;
import org.danilkha.connection.api.ServerSocketClientConnection;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Server implements ClientPackageReceiver{

    private final ServerSocket serverSocket;
    private final Map<Integer, ServerSocketClientConnection> clientConnectionList;
    private ClientPackageReceiver clientPackageReceiver;

    private final int tickRate;

    private int idSequence = 0;

    public Server(int port, int tickRate) throws IOException {
        serverSocket = new ServerSocket(port);
        System.out.println("serever started");
        clientConnectionList = new HashMap<>();
        this.tickRate = tickRate;
    }

    public void setListener(ClientPackageReceiver clientPackageReceiver){
        this.clientPackageReceiver = clientPackageReceiver;
    }

    public void start(){
        while (true){
            try {
                Socket socket = serverSocket.accept();
                int id = idSequence;
                idSequence++;
                ServerSocketClientConnection clientConnection = new ServerSocketClientConnection(id, socket, (data) ->{
                    clientPackageReceiver.receiveData(id, data);
                });
                clientConnection.start();
                System.out.println("client connected %s".formatted(id));
                clientConnectionList.put(id, clientConnection);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void receiveData(int clientId, String data) {
        ServerSocketClientConnection clientConnection = clientConnectionList.get(clientId);
        clientConnection.receiveData(data);
    }
}
