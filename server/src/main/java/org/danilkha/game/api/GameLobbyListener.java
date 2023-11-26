package org.danilkha.game.api;

public interface GameLobbyListener {
    boolean createNewLobby(int playerId, String roomName, String playerName);
    boolean connectToLobby(int playerId, String name);
}
