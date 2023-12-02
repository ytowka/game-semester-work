package org.danilkha.game;

public record Lobby(
        String hostName,
        String[] playerNames
) {

    public static Lobby fromString(String serialized){
        String args = serialized.replaceAll("[{}]","");
        String[] splitted = args.split("&");
        String name = splitted[0];
        String[] players = splitted[1].replaceAll("[\\[\\]]","").split("\\+");
        return new Lobby(name, players);
    }

    public String serialize(){
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append(hostName);
        sb.append("&");
        sb.append("[");
        sb.append(String.join("+",playerNames));
        sb.append("]}");
        return sb.toString();
    }
}
