package org.danilkha.connection.api;

public interface ClientDisconnectListener {
    void onDisconnect(int clientId, Exception e);
}
