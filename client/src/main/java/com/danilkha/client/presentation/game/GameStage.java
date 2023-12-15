package com.danilkha.client.presentation.game;

import javafx.animation.AnimationTimer;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;

public class GameStage extends Pane{

    private final AnimationTimer gameLoop;

    public final List<Actor> actors;

    private float mouseX = 0f;
    private float mouseY = 0f;

    private final float gameWidth;
    private final float gameHeight;

    private final List<Integer> pressedKeys;

    public final InputListener inputListener = new InputListener() {
        @Override
        public void onMouseMoved(float x, float y) {
            mouseX = x;
            mouseY = y;
        }

        @Override
        public void onMouseClicked(float x, float y) {

        }

        @Override
        public void onKeyDown(int code) {
            System.out.println("onKeyDown: "+code);
            pressedKeys.add(Integer.valueOf(code));
        }

        @Override
        public void onKeyUp(int code) {
            System.out.println("onKeyUp: "+code);
            pressedKeys.remove(Integer.valueOf(code));
        }
    };

    public GameStage(float gameWidth, float gameHeight){
        actors = new ArrayList<>();

        this.gameWidth = gameWidth;
        this.gameHeight = gameHeight;
        gameLoop = new AnimationTimer() {
            long lastUpdate = 0;
            @Override
            public void handle(long now) {
                int delta = (int)(now/1000L - lastUpdate);
                lastUpdate = now/1000L;
                System.out.println(now / 1_000_000L);
                doActors(delta);
            }
        };
        pressedKeys = new ArrayList<>();
    }

    private void doActors(int delta){
        actors.forEach(actor -> {
            actor.onAct(delta);
        });
    }

    public void addActor(Actor actor){
        actors.add(actor);
        actor.setGameStage(this);
        getChildren().add(actor.getNode());
    }

    public void removeActor(Actor actor){
        actors.remove(actor);
        getChildren().remove(actor.getNode());
    }

    public void start(){
        gameLoop.start();
    }

    public boolean isKeyPressed(int code){
        return pressedKeys.contains(code);
    }

    public float getMouseX() {
        return mouseX;
    }

    public float getMouseY() {
        return mouseY;
    }
}
