package org.danilkha.game.api;

import org.danilkha.game.Player;

public interface GameLobby {
    boolean startGame();
    boolean isStarted();
    boolean joinPlayer(Player player);
}
