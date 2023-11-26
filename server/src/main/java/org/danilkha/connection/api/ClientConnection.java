package org.danilkha.connection.api;

public interface ClientConnection {
    int getId();
    public void emitData(String data);
}
