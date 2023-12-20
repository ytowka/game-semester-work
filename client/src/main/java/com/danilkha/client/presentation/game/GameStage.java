package com.danilkha.client.presentation.game;

import com.danilkha.client.presentation.game.missle.Missile;
import com.danilkha.client.presentation.game.tank.ControllerTankSprite;
import com.danilkha.client.presentation.game.tank.RemoteTankSprite;
import com.danilkha.client.presentation.game.tank.TankSprite;
import javafx.animation.AnimationTimer;
import javafx.scene.layout.Pane;
import org.danilkha.config.GameConfig;
import org.danilkha.utils.observable.EqualityPolicy;
import org.danilkha.utils.observable.MutableObservableValue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GameStage extends Pane{

    private final AnimationTimer gameLoop;



    private final ControllerTankSprite controllerTankSprite;
    private final List<RemoteTankSprite> remoteTanks;
    private final List<Missile> missiles;

    private final long lastShoot = 0;

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
            if(System.currentTimeMillis() - lastShoot > GameConfig.RELOAD_PERIOD){
                shoot(
                        controllerTankSprite.getCenterX(),
                        controllerTankSprite.getCenterY(),
                        controllerTankSprite.getDegAngle()
                );
            }
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

    private GameCallback gameCallback;

    private void shoot(float centerX, float centerY, double degAngle) {
        Missile missile = new Missile(
                true,
                GameModel.getActualSize(GameConfig.MISSILE_SIZE),
                GameModel.getActualSize(GameConfig.MISSILE_SIZE),
                centerX,
                centerY,
                (float) degAngle
        );
        getChildren().add(missile.getImage());
        missiles.add(missile);
        gameCallback.shoot(centerX, centerY, degAngle);
    }

    public GameStage(ControllerTankSprite controllerTankSprite, List<RemoteTankSprite> remoteTanks, float gameWidth, float gameHeight, GameCallback gameCallback){
        this.controllerTankSprite = controllerTankSprite;
        this.remoteTanks = remoteTanks;
        getChildren().add(controllerTankSprite.getImage());
        for (RemoteTankSprite remoteTank : remoteTanks) {
            getChildren().add(remoteTank.getImage());
        }
        missiles = new ArrayList<>();

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
        this.gameCallback = gameCallback;
    }

    private synchronized void doActors(int delta){
        controllerTankSprite.onAct(delta);
        remoteTanks.forEach(sprite -> {
            sprite.onAct(delta);
        });
        for (Missile missile : missiles) {
            missile.onAct(delta);
            if(missile.getCenterY() < -100
                    || missile.getCenterX() > GameModel.WINDOW_SIZE + 100
                    || missile.getCenterY() < -100
                    ||missile.getCenterY() > GameModel.WINDOW_SIZE + 100
            ){
                missiles.remove(missile);
            }else if(missile.isFromPlayer()){
                for (RemoteTankSprite remoteTank : remoteTanks) {
                    if(remoteTank.hits(missile.getCenterX(), missile.getCenterY())){
                        gameCallback.playerHit(remoteTank.getPlayerIndex());
                        missiles.remove(missile);
                        break;
                    }
                }
            }
        }
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
        for (RemoteTankSprite remoteTank : remoteTanks) {
            if(remoteTank.getPlayerIndex() == index){
                remoteTank.setState(x, y, angle);
            }
        }
    }

    public void addMissile(float x, float y, float angle){
        Missile missile = new Missile(
                false,
                GameModel.getActualSize(GameConfig.MISSILE_SIZE),
                GameModel.getActualSize(GameConfig.MISSILE_SIZE),
                x,
                y,
                angle
        );
        getChildren().add(missile.getImage());
        missiles.add(missile);
    }

    public MutableObservableValue<float[]> getPlayerMove() {
        return playerMove;
    }
}
