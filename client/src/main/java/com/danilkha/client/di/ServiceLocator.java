package com.danilkha.client.di;

import com.danilkha.client.api.LobbyApiImpl;
import com.danilkha.client.api.MultiPackageReceiver;
import org.danilkha.api.LobbyApi;
import org.danilkha.config.ServerConfig;
import org.danilkha.connection.SocketClientConnection;
import org.danilkha.utils.lazy.Lazy;

import java.io.IOException;
import java.net.Socket;

public class ServiceLocator {

    private ServiceLocator(){

    }

    private static Lazy<ServiceLocator> instance = new Lazy<>(ServiceLocator::new);

    public static ServiceLocator getInstance(){
        return instance.get();
    }

    private Lazy<Socket> socket = new Lazy<>(() ->{
        try {
            return new Socket(ServerConfig.HOST, ServerConfig.PORT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    });

    private Lazy<MultiPackageReceiver> globalPackageReceiver = new Lazy<>(MultiPackageReceiver::new);

    public Lazy<SocketClientConnection> socketClientConnection = new Lazy<>(() ->{
        try {
            return new SocketClientConnection(
                    socket.get(),
                    globalPackageReceiver.get()

            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    });

    public Lazy<LobbyApi> lobbyApi = new Lazy<>(() -> {
        LobbyApiImpl lobbyApi1 = new LobbyApiImpl(socketClientConnection.get());
        globalPackageReceiver.get().addReceiver(lobbyApi1);
        return lobbyApi1;
    });

}
