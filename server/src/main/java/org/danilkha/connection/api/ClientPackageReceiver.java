package org.danilkha.connection.api;

import org.danilkha.connection.api.PackageReceiver;

public interface ClientPackageReceiver {
    void receiveData(int clientId, String data);
}
