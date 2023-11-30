package org.danilkha.api;

public interface ClientLobbyCallback {
    boolean createNewLobby(String name, String playerName);
    boolean connectToLobby(String name, String playerName);
    boolean leaveLobby();
    boolean startGame();
}
