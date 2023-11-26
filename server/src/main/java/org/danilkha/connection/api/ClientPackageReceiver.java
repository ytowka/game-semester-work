package org.danilkha.connection.api;

public interface ClientPackageReceiver {
    void receiveData(int clientId, String data);
}
