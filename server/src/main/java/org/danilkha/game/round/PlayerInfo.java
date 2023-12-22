package org.danilkha.game.round;

import org.danilkha.config.GameConfig;
import org.danilkha.game.Player;

import static org.danilkha.config.GameConfig.PLAYER_START_POSITIONS;

public class PlayerInfo {
    private Player player;
    private final int index;

    private float x;
    private float y;

    private int hp;
    private float angle; //deg

    private int score = 0;

    public PlayerInfo(int index, Player player) {
        this.player = player;

        this.index = index;
        reset();
    }

    public void reset(){
        x = PLAYER_START_POSITIONS[index][0];
        y = PLAYER_START_POSITIONS[index][1];;

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

    public boolean isAlive(){
        return hp > 0;
    }

    public int getScore() {
        return score;
    }

    public void addScore(){
        score += 1;
    }
}
