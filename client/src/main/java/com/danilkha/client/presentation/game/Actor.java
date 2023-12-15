package com.danilkha.client.presentation.game;

import javafx.scene.Node;

public abstract class Actor {

    private float width;
    private float height;

    public Actor(float width, float height){
        this.width = width;
        this.height = height;
    }

    private GameStage gameStage;

    public GameStage getGameStage() {
        return gameStage;
    }

    public void setGameStage(GameStage gameStage) {
        this.gameStage = gameStage;
    }

    public float getCenterX(){
        return (float) getNode().getLayoutX() + width/2f;
    }

    public float getCenterY(){
        return (float) getNode().getLayoutY() + height/2f;
    }

    abstract void onAct(int delta);

    abstract Node getNode();

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }
}