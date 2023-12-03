package org.danilkha.middleware.utils;

import org.danilkha.connection.ClientRequest;
import org.danilkha.connection.api.ClientPackageReceiver;
import org.danilkha.middleware.RequestHandler;
import org.danilkha.protocol.Protocol;

public abstract class RequestPackageReceiver implements ClientPackageReceiver, RequestHandler {

    @Override
    public final void receiveData(int clientId, String data) {
        ClientRequest.Type type;
        if(data.startsWith(Protocol.GET_PREFIX)){
            type = ClientRequest.Type.GET;
        }else if(data.startsWith(Protocol.DROP_PREFIX)){
            type = ClientRequest.Type.DROP;
        } else if (data.startsWith(Protocol.SUBSCRIBE_PREFIX)) {
            type = ClientRequest.Type.SUBSCRIBE;
        } else {
            type = ClientRequest.Type.RAW;
        }

        ClientRequest clientRequest = getRequest(clientId, data, type);

        receiveRequest(clientRequest);
    }

    private static ClientRequest getRequest(int clientId, String data, ClientRequest.Type type) {
        ClientRequest clientRequest;
        if(type != ClientRequest.Type.RAW){
            String[] splitted = data.substring(1).split(Protocol.REQUEST_DATA_DELIMETER);
            String path = splitted[0];
            String[] parsedData;
            if(splitted.length > 1){
                parsedData = splitted[1].substring(1, splitted[1].length()-1).split(Protocol.DATA_DELIMINTER);
            }else{
                parsedData = new String[0];
            }
            clientRequest = new ClientRequest(clientId, type, path, parsedData);
        }else{
            clientRequest = new ClientRequest(clientId, ClientRequest.Type.DROP, "", new String[]{data});
        }
        return clientRequest;
    }
}
