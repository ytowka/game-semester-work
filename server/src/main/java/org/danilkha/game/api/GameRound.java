package org.danilkha.game.api;

public interface GameRound {
    void moveTo(int playerId, float x, float y);
    void shoot(int playerId, float directionAngle);
    void hit(int fromId, int toId);
}
