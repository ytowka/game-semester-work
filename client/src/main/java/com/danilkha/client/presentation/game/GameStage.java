package com.danilkha.client.presentation.game;

import com.danilkha.client.presentation.game.tank.RemoteTankActor;
import com.danilkha.client.presentation.game.tank.TankActor;
import javafx.animation.AnimationTimer;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import org.danilkha.utils.observable.EqualityPolicy;
import org.danilkha.utils.observable.MutableObservableValue;
import org.danilkha.utils.observable.ObservableValue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GameStage extends Pane{

    private final AnimationTimer gameLoop;

    public final List<Actor> actors;

    public final List<TankActor> tankActors;

    private float mouseX = 0f;
    private float mouseY = 0f;

    private final float gameWidth;
    private final float gameHeight;

    public final Set<Integer> pressedKeys;

    private final MutableObservableValue<float[]> playerMove = new MutableObservableValue<>(EqualityPolicy.REFERENTIAL);

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
            pressedKeys.add(Integer.valueOf(code));
        }

        @Override
        public void onKeyUp(int code) {
            pressedKeys.remove(Integer.valueOf(code));
        }
    };

    public GameStage(float gameWidth, float gameHeight){
        actors = new ArrayList<>();

        this.gameWidth = gameWidth;
        this.gameHeight = gameHeight;
        gameLoop = new AnimationTimer() {
            long lastUpdate = System.currentTimeMillis();
            @Override
            public void handle(long now) {
                int delta = (int)(now/1_000_000L - lastUpdate);
                //System.out.println(delta);
                lastUpdate = now/1_000_000L;
                doActors(delta);
            }
        };
        pressedKeys = new HashSet<>();
        tankActors = new ArrayList<>();
    }

    private void doActors(int delta){
        actors.forEach(actor -> {
            actor.onAct(delta);
        });
    }

    public void addActor(Actor actor){
        actors.add(actor);
        if(actor instanceof TankActor tankActor){
            tankActors.add(tankActor);
        }
        actor.setGameStage(this);
        getChildren().add(actor.getImage());
    }

    public void removeActor(Actor actor){
        actors.remove(actor);
        getChildren().remove(actor.getImage());
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

    public void moveTank(int index, float x, float y, float angle){
        if(tankActors.get(index) instanceof RemoteTankActor remoteTankActor){
            remoteTankActor.setState(x, y, angle);
        }
    }

    public MutableObservableValue<float[]> getPlayerMove() {
        return playerMove;
    }
}
