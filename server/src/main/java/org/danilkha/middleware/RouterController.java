package org.danilkha.middleware;

import org.danilkha.connection.ClientRequest;
import org.danilkha.connection.Response;
import org.danilkha.connection.Server;
import org.danilkha.connection.api.ClientPackageReceiver;
import org.danilkha.game.Player;
import org.danilkha.middleware.utils.RequestPackageReceiver;
import org.danilkha.protocol.Protocol;
import org.danilkha.utils.observable.ObservableValue;
import org.danilkha.utils.observable.Observer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RouterController extends RequestPackageReceiver {

    private final Map<String, RequestHandler> requestHandlers;
    private final Map<String, GetHandler> getHandlers;

    private final Map<Integer, List<Observer<?>>> clientObservers;

    private final ClientPackageReceiver clientPackageReceiver;


    public RouterController(ClientPackageReceiver clientPackageReceiver){
        requestHandlers = new HashMap<>();
        getHandlers = new HashMap<>();
        clientObservers = new HashMap<>();
        this.clientPackageReceiver = clientPackageReceiver;
    }

    public void addHandler(String request, RequestHandler requestHandler){
        requestHandlers.put(request, requestHandler);
    }

    public void addGetMapping(String request, GetHandler getHandler){
        getHandlers.put(request, getHandler);
    }

    @Override
    public final void receiveRequest(ClientRequest clientRequest) {
        if(!clientRequest.path().equals("game/move")){
            System.out.println(clientRequest);
        }
        if(clientRequest.type() == ClientRequest.Type.GET){
            GetHandler getHandler = getHandlers.get(clientRequest.path());
            if(getHandler != null){
                String[] data = getHandler.receiveRequest(clientRequest);
                Response response = new Response(
                        Response.Type.GIVE,
                        clientRequest.path(),
                        data
                );
                clientPackageReceiver.receiveData(clientRequest.clientId(), Protocol.buildResponse(response));
            }
        }else{
            RequestHandler requestHandler = requestHandlers.get(clientRequest.path());
            if(requestHandler != null){
                requestHandler.receiveRequest(clientRequest);
            }
        }
    }
}
