package org.danilkha.connection;

import java.util.Arrays;

public record Request(
        int clientId,
        Type type,
        String path,
        String[] data
) {
    public enum Type{ GET, DROP, SUBSCRIBE, RAW }

    @Override
    public String toString() {
        return "Request{" +
                "clientId=" + clientId +
                ", type=" + type +
                ", path='" + path + '\'' +
                ", data=" + Arrays.toString(data) +
                '}';
    }
}
