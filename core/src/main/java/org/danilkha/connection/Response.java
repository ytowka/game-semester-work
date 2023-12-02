package org.danilkha.connection;

public record Response(
        String request,
        String[] data
) {

}
