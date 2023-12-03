package org.danilkha.connection;

import java.util.Arrays;

public record Response(
        Type type,
        String request,
        String[] data
) {

    @Override
    public String toString() {
        return "Response{" +
                "type=" + type +
                ", request='" + request + '\'' +
                ", data=" + Arrays.toString(data) +
                '}';
    }

    public enum Type{ GIVE, EMIT, RAW }
}
