package com.danilkha.client.presentation.game.tank;

public class RemoteTankSprite extends TankSprite {

    private float[] lastState = new float[3];
    private float[] currentState = new float[3];

    public RemoteTankSprite(int playerIndex, float width, float height) {
        super(playerIndex, width, height);
    }

    @Override
    public void onAct(int delta) {
        node.setX(currentState[0]);
        node.setY(currentState[1]);
        node.setRotate(currentState[2]);
    }

    public void setState(float x, float y, float angle){
        currentState[0] = x;
        currentState[1] = y;
        currentState[2] = angle+90;
    }
}
