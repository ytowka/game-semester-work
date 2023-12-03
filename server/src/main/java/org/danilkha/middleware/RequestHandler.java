package org.danilkha.middleware;

import org.danilkha.connection.Request;

public interface RequestHandler {
    void receiveRequest(Request request);
}
