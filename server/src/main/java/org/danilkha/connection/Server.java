package org.danilkha.connection;

import org.danilkha.connection.api.ClientConnection;
import org.danilkha.connection.api.ClientPackageReceiver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Server implements ClientPackageReceiver{

    private final ServerSocket serverSocket;
    private final Map<Integer, ClientConnection> clientConnectionList;
    private ClientPackageReceiver clientPackageReceiver;

    private final int tickRate;

    private int idSequence = 0;

    public Server(int port, int tickRate) throws IOException {
        serverSocket = new ServerSocket(port);
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
                SocketClientConnection clientConnection = new SocketClientConnection(id, socket, (data) ->{
                    clientPackageReceiver.receiveData(id, data);
                });
                clientConnection.start();
                clientConnectionList.put(id, clientConnection);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void receiveData(int clientId, String data) {
        ClientConnection clientConnection = clientConnectionList.get(clientId);
        clientConnection.emitData(data);
    }
}
