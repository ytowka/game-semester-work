package org.danilkha.protocol;

import org.danilkha.connection.Response;

public class Protocol {
    public static final String GET_PREFIX = ">";
    public static final String DROP_PREFIX = "-";
    public static final String SUBSCRIBE_PREFIX = "=";

    public static final String GIVE_PREFIX = "<";
    public static final String EMIT_PREFIX = "&";

    public static final String REQUEST_DATA_DELIMETER = ":";
    public static final String DATA_DELIMINTER = ";";

    public static boolean isGetRequest(String data){
        return data.startsWith(GET_PREFIX);
    }
    public static boolean isGiveRequest(String data){
        return data.startsWith(GIVE_PREFIX);
    }

    public static boolean isDropRequest(String data){
        return data.startsWith(DROP_PREFIX);
    }

    public static boolean isSubscribeRequest(String data){
        return data.startsWith(SUBSCRIBE_PREFIX);
    }

    public static String buildGetRequest(String request, String... data){
        return GET_PREFIX + buildRequest(request, data);
    }

    public static String buildSubscribeRequest(String request, String... data){
        return SUBSCRIBE_PREFIX + buildRequest(request, data);
    }

    public static String buildDropRequest(String request, String... data){
        return DROP_PREFIX + buildRequest(request, data);
    }

    public static String buildData(String... data){
        return "{"+String.join(DATA_DELIMINTER,data)+"}";
    }

    private static String[] parseData(String raw){
        String unpacked = raw.substring(1, raw.length()-1);
        if(unpacked.isEmpty()){
            return new String[0];
        }else{
            return unpacked.split(DATA_DELIMINTER);
        }
    }

    public static String buildRequest(String request, String... data){
        StringBuilder sb = new StringBuilder();
        sb.append(request);
        sb.append(REQUEST_DATA_DELIMETER);
        if(data.length > 0){
            sb.append(buildData(data));
        }

        return sb.toString();
    }

    public static String buildResponse(Response response){
        String prefix = switch (response.type()){
            case GIVE -> GIVE_PREFIX;
            case EMIT -> EMIT_PREFIX;
            case RAW -> DROP_PREFIX;
        };
        return prefix+response.request()+REQUEST_DATA_DELIMETER+buildData(response.data());
    }

    public static Response ParseResponse(String rawData){
        Response.Type type;
        if(rawData.startsWith(GIVE_PREFIX)){
            type = Response.Type.GIVE;
        }else if(rawData.startsWith(EMIT_PREFIX)){
            type = Response.Type.EMIT;
        }else{
            type = Response.Type.RAW;
        }
        String[] splittedData = rawData.substring(1).split(REQUEST_DATA_DELIMETER);
        return new Response(type, splittedData[0], parseData(splittedData[1]));
    }
}
