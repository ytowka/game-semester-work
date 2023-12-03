package org.danilkha.connection;

import org.danilkha.connection.api.ClientDisconnectListener;
import org.danilkha.connection.api.ClientPackageReceiver;
import org.danilkha.connection.api.ServerSocketClientConnection;
import org.danilkha.utils.observable.ObservableValue;
import org.danilkha.utils.observable.Observer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Server implements ClientPackageReceiver{

    private final ServerSocket serverSocket;
    private final Map<Integer, ServerSocketClientConnection> clientConnectionList;
    private ClientPackageReceiver clientPackageReceiver;
    private final List<ClientDisconnectListener> disconnectListeners;

    private final int tickRate;

    private int idSequence = 0;

    public Server(int port, int tickRate) throws IOException {
        serverSocket = new ServerSocket(port);
        System.out.println("serever started");
        clientConnectionList = new HashMap<>();
        disconnectListeners = new ArrayList<>();
        this.tickRate = tickRate;
    }

    public void setListener(ClientPackageReceiver clientPackageReceiver){
        this.clientPackageReceiver = clientPackageReceiver;
    }

    public void addClientDisconnectLister(ClientDisconnectListener disconnectListener){
        disconnectListeners.add(disconnectListener);
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
                clientConnection.setDisconnectListener(e -> {
                    disconnectListeners.forEach(it -> it.onDisconnect(id, e));
                    clientConnectionList.remove(id);
                    System.out.println("client disconnected %s".formatted(id));
                });
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

    public <T> void disposeOnDisconnect(int clientId, ObservableValue<T> observableValue, Observer<T> observer){
        SocketClientConnection connection = clientConnectionList.get(clientId);
        connection.disposeOnDisconnect(observableValue, observer);
    }
}
