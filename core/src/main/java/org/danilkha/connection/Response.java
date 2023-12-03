package org.danilkha.connection;

public record Response(
        Type type,
        String request,
        String[] data
) {


    public enum Type{ GIVE, EMIT, RAW }
}
