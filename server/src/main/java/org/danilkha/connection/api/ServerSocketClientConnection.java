package org.danilkha.connection.api;

import org.danilkha.connection.PackageReceiver;
import org.danilkha.connection.SocketClientConnection;

import java.io.IOException;
import java.net.Socket;

public class ServerSocketClientConnection extends SocketClientConnection {
    private int id;
    public ServerSocketClientConnection(int id, Socket socket, PackageReceiver packageReceiver) throws IOException {
        super(socket, packageReceiver);
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
