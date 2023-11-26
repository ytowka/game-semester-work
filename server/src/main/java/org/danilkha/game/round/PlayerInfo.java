package org.danilkha.game.round;

import org.danilkha.config.GameConfig;
import org.danilkha.game.Player;

public class PlayerInfo {
    private Player player;

    private float x;
    private float y;

    private int hp;
    private float rotation; //rad

    public PlayerInfo(Player player) {
        this.player = player;

        x = 0f;
        y = 0f;

        hp = GameConfig.PLAYER_HP;
        rotation = 0f;
    }
}
