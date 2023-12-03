package org.danilkha.middleware;

import org.danilkha.connection.ClientRequest;
import org.danilkha.connection.Response;

public interface GetHandler {

    String[] receiveRequest(ClientRequest clientRequest) ;
}
