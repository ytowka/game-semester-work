package org.danilkha.game;

public interface LobbyCallback {

    boolean startGame();
    boolean isStarted();
    boolean joinPlayer(int id);

}
