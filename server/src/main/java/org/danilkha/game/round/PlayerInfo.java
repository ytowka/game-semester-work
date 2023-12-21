package org.danilkha.game.round;

import org.danilkha.config.GameConfig;
import org.danilkha.game.Player;

public class PlayerInfo {
    private Player player;
    private final int index;

    private float x;
    private float y;

    private int hp;
    private float angle; //deg

    public PlayerInfo(int index, Player player) {
        this.player = player;

        this.index = index;
        x = 0f;
        y = 0f;

        hp = GameConfig.PLAYER_HP;
        angle = 0f;
    }

    public Player getPlayer() {
        return player;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public int getHp() {
        return hp;
    }

    public float getAngle() {
        return angle;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public int getIndex() {
        return index;
    }

    public boolean hit(){
        hp -= GameConfig.DEFAULT_DAMAGE;
        return hp <= 0f;
    }
}
