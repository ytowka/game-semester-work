package com.danilkha.client.presentation.game;

public interface GameCallback {

    void shoot(float centerX, float centerY, double degAngle);
    void playerHit(int to);

    void wallHit(int x, int y);
}
