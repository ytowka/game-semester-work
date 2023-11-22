package org.danilkha.game;

public interface GameActionListener {

    boolean createNewLobby(String name, int hostUserId);
    boolean connectToLobby(String name, int userId);
}
