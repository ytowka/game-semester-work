package com.danilkha.client.presentation.game;

import javafx.scene.image.ImageView;

public abstract class Sprite {

    private float width;
    private float height;

    public Sprite(float width, float height){
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
        return (float) getImage().getX() + width/2f;
    }

    public double getX(){
        return getImage().getX();
    }

    public double getY(){
        return getImage().getY();
    }

    public float getCenterY(){
        return (float) getImage().getY() + height/2f;
    }

    public abstract void onAct(int delta);

    public abstract ImageView getImage();

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

    public boolean hits(float x, float y){
        return x > getImage().getX() && x < getImage().getX() + width && y > getImage().getY() && y < getImage().getY() + height;
    }
}
