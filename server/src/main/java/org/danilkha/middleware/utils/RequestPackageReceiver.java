package org.danilkha.middleware.utils;

import org.danilkha.connection.Request;
import org.danilkha.connection.api.ClientPackageReceiver;
import org.danilkha.middleware.RequestHandler;
import org.danilkha.protocol.Protocol;

public abstract class RequestPackageReceiver implements ClientPackageReceiver, RequestHandler {
    @Override
    public final void receiveData(int clientId, String data) {
        Request.Type type;
        if(data.startsWith(Protocol.GET_PREFIX)){
            type = Request.Type.GET;
        }else if(data.startsWith(Protocol.DROP_PREFIX)){
            type = Request.Type.DROP;
        } else if (data.startsWith(Protocol.SUBSCRIBE_PREFIX)) {
            type = Request.Type.SUBSCRIBE;
        } else {
            type = Request.Type.RAW;
        }

        Request request = getRequest(clientId, data, type);

        receiveRequest(request);
    }

    private static Request getRequest(int clientId, String data, Request.Type type) {
        Request request;
        if(type != Request.Type.RAW){
            String[] splitted = data.substring(1).split(Protocol.REQUEST_DATA_DELIMETER);
            String path = splitted[0];
            String[] parsedData;
            if(splitted.length > 1){
                parsedData = splitted[1].substring(1, splitted[1].length()-1).split(Protocol.DATA_DELIMINTER);
            }else{
                parsedData = new String[0];
            }
            request = new Request(clientId, type, path, parsedData);
        }else{
            request = new Request(clientId, Request.Type.DROP, "", new String[]{data});
        }
        return request;
    }
}
