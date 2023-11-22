package org.danilkha;

public interface ClientRoundCallback {
    void moveTo(float x, float y);
    void shoot(float directionAngle);
    boolean destroyTank(int id);
}
