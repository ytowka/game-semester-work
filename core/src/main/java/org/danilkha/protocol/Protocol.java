package org.danilkha.protocol;

import org.danilkha.connection.Response;

import java.util.Collections;

public class Protocol {
    public static final String GET_PREFIX = ">";
    public static final String GIVE_PREFIX = "<";
    public static final String DROP_PREFIX = "-";

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

    public static String buildGetRequest(String request, String... data){
        return GET_PREFIX + buildRequest(request, data);
    }

    public static String buildDropRequest(String request, String... data){
        return DROP_PREFIX + buildRequest(request, data);
    }

    private static String buildRequest(String request, String... data){
        StringBuilder sb = new StringBuilder();
        sb.append(request);
        sb.append(REQUEST_DATA_DELIMETER);
        if(data.length > 0){
            sb.append("{");
            sb.append(String.join(DATA_DELIMINTER,data));
            sb.append("}");
        }

        return sb.toString();
    }

    public static Response ParseResponse(String rawData){
        String[] splittedData = rawData.substring(1).split(REQUEST_DATA_DELIMETER);
        String data = splittedData[1].substring(1, splittedData[1].length()-1);
        return new Response(splittedData[0], data.split(DATA_DELIMINTER));
    }
}
