package org.danilkha.middleware;

import org.danilkha.connection.ClientRequest;

public interface RequestHandler {
    void receiveRequest(ClientRequest clientRequest);
}
