package com.danilkha.client.presentation.game;

import com.danilkha.client.presentation.game.missle.Missile;
import com.danilkha.client.presentation.game.tank.ControllerTankSprite;
import com.danilkha.client.presentation.game.tank.RemoteTankSprite;
import javafx.animation.AnimationTimer;
import javafx.scene.layout.Pane;
import org.danilkha.config.GameConfig;
import org.danilkha.utils.observable.EqualityPolicy;
import org.danilkha.utils.observable.MutableObservableValue;

import java.util.*;

public class GameStage extends Pane{

    private final AnimationTimer gameLoop;


    private ControllerTankSprite controllerTankSprite;
    private final List<RemoteTankSprite> remoteTanks;
    private final List<Missile> missiles;

    private long lastShoot = 0;

    private float mouseX = 0f;
    private float mouseY = 0f;

    private final float gameWidth;
    private final float gameHeight;

    private final String[] playerNames;
    private final String controllerPlayerName;

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
            if(System.currentTimeMillis() - lastShoot > GameConfig.RELOAD_PERIOD && !controllerTankSprite.isDead()){
                lastShoot = System.currentTimeMillis();
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


    public GameStage(String[] players, String controller, float gameWidth, float gameHeight, GameCallback gameCallback){
        playerNames = players;
        controllerPlayerName = controller;

        remoteTanks = new ArrayList<>();

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

    private void doActors(int delta){
        if(controllerTankSprite != null && !controllerTankSprite.isDead()){
            controllerTankSprite.onAct(delta);
        }
        remoteTanks.forEach(sprite -> {
            sprite.onAct(delta);
        });

        ListIterator<Missile> missileListIterator = missiles.listIterator();
        while (missileListIterator.hasNext()){
            Missile missile = missileListIterator.next();

            missile.onAct(delta);
            if(missile.getCenterY() < -100
                    || missile.getCenterX() > GameModel.WINDOW_SIZE + 100
                    || missile.getCenterY() < -100
                    || missile.getCenterY() > GameModel.WINDOW_SIZE + 100
            ){
                getChildren().remove(missile.getImage());
                missileListIterator.remove();
            }else if(missile.getPlayerIndex() == controllerTankSprite.getPlayerIndex()){
                for (RemoteTankSprite remoteTank : remoteTanks) {
                    if(remoteTank.hits(missile.getCenterX(), missile.getCenterY())){
                        gameCallback.playerHit(remoteTank.getPlayerIndex());
                        getChildren().remove(missile.getImage());
                        missileListIterator.remove();
                        break;
                    }
                }
            }else{
                for (RemoteTankSprite remoteTank : remoteTanks) {
                    if(remoteTank.hits(missile.getCenterX(), missile.getCenterY()) && remoteTank.getPlayerIndex() != missile.getPlayerIndex()){
                        getChildren().remove(missile.getImage());
                        missileListIterator.remove();
                        break;
                    }
                }
                if(controllerTankSprite.hits(missile.getCenterX(), missile.getCenterY())){
                    getChildren().remove(missile.getImage());
                    missileListIterator.remove();
                }
            }
        }
    }

    private void shoot(float centerX, float centerY, double degAngle) {
        Missile missile = new Missile(
                controllerTankSprite.getPlayerIndex(),
                GameModel.getActualSize(GameConfig.MISSILE_SIZE),
                GameModel.getActualSize(GameConfig.MISSILE_SIZE),
                centerX,
                centerY,
                (float) degAngle
        );
        getChildren().add(missile.getImage());
        synchronized (missiles){
            missiles.add(missile);
        }
        gameCallback.shoot(centerX, centerY, degAngle);
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

    public void addMissile(int playerIndex, float x, float y, float angle){
        if(controllerTankSprite.getPlayerIndex() != playerIndex){
            Missile missile = new Missile(
                    playerIndex,
                    GameModel.getActualSize(GameConfig.MISSILE_SIZE),
                    GameModel.getActualSize(GameConfig.MISSILE_SIZE),
                    x,
                    y,
                    angle
            );
            getChildren().add(missile.getImage());
            missiles.add(missile);
        }
    }

    public void resetStage(){
        for (RemoteTankSprite remoteTank : remoteTanks) {
            getChildren().remove(remoteTank.getImage());
        }
        remoteTanks.clear();
        if(controllerTankSprite != null){
            getChildren().remove(controllerTankSprite.getImage());
        }

        for (int i = 0; i < playerNames.length; i++) {
            String s = playerNames[i];
            if (s.equals(controllerPlayerName)) {
                controllerTankSprite = new ControllerTankSprite(i, 50, 50) ;
                controllerTankSprite.setGameStage(this);
            } else {
                remoteTanks.add(new RemoteTankSprite(i, 50, 50));
            }
        }

        getChildren().add(controllerTankSprite.getImage());
        for (RemoteTankSprite remoteTank : remoteTanks) {
            getChildren().add(remoteTank.getImage());
        }

    }


    public MutableObservableValue<float[]> getPlayerMove() {
        return playerMove;
    }

    public void registerHit(int index) {
        if(controllerTankSprite.getPlayerIndex() == index){
            boolean dead = controllerTankSprite.hit();
            if(dead){
                getChildren().remove(controllerTankSprite.getImage());
            }
        }
    }

    public void notifyPlayerDead(int index){
        ListIterator<RemoteTankSprite> remoteTankSpriteListIterator = remoteTanks.listIterator();
        while (remoteTankSpriteListIterator.hasNext()){
            RemoteTankSprite tankSprite = remoteTankSpriteListIterator.next();
            if(tankSprite.getPlayerIndex() == index){
                getChildren().remove(tankSprite.getImage());
                remoteTankSpriteListIterator.remove();
                break;
            }
        }
    }

    public String[] getPlayerNames() {
        return playerNames;
    }

    public String getControllerPlayerName() {
        return controllerPlayerName;
    }
}
