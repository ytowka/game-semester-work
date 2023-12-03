package org.danilkha.middleware;

import org.danilkha.connection.Request;
import org.danilkha.middleware.utils.RequestPackageReceiver;

import java.util.Map;

public class RouterController extends RequestPackageReceiver {

    private Map<String, RequestHandler> requestHandlers;

    public void addRoute(String request, RequestHandler requestHandler){
        requestHandlers.put(request, requestHandler);
    }

    @Override
    public final void receiveRequest(Request request) {
        System.out.println("got request: "+request);
        RequestHandler requestHandler = requestHandlers.get(request.path());
        if(requestHandler != null){
            requestHandler.receiveRequest(request);
        }
    }
}
