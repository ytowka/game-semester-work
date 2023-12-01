package com.danilkha.client.di;

import com.danilkha.client.api.MultiPackageReceiver;
import org.danilkha.config.ServerConfig;
import org.danilkha.connection.SocketClientConnection;
import org.danilkha.utils.lazy.Lazy;

import java.io.IOException;
import java.net.Socket;

public class ServiceLocator {


    private Lazy<Socket> socket = new Lazy<>(() ->{
        try {
            return new Socket(ServerConfig.HOST, ServerConfig.PORT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    });

    private Lazy<MultiPackageReceiver> globalPackageReceiver = new Lazy<>(MultiPackageReceiver::new);

    private Lazy<SocketClientConnection> socketClientConnection = new Lazy<>(() ->{
        try {
            return new SocketClientConnection(
                    socket.get(),
                    globalPackageReceiver.get()

            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    });


}
