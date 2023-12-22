package com.danilkha.client.presentation.game;

import com.danilkha.client.presentation.game.missle.Missile;
import com.danilkha.client.presentation.game.tank.ControllerTankSprite;
import com.danilkha.client.presentation.game.tank.RemoteTankSprite;
import com.danilkha.client.presentation.game.wall.Wall;
import javafx.animation.AnimationTimer;
import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
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

    private final Wall[][] walls;

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
        setBackground(new Background(new BackgroundFill(new Color(0.2, 0, 0, 1), CornerRadii.EMPTY, Insets.EMPTY)));

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

        walls = new Wall[GameConfig.MAP_SIZE][GameConfig.MAP_SIZE];
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

            boolean hitted = false;

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
                        hitted = true;
                        break;
                    }
                }
            }else{
                for (RemoteTankSprite remoteTank : remoteTanks) {
                    if(remoteTank.hits(missile.getCenterX(), missile.getCenterY()) && remoteTank.getPlayerIndex() != missile.getPlayerIndex()){
                        getChildren().remove(missile.getImage());
                        missileListIterator.remove();
                        hitted = true;
                        break;
                    }
                }
                if(controllerTankSprite.hits(missile.getCenterX(), missile.getCenterY())){
                    getChildren().remove(missile.getImage());
                    missileListIterator.remove();
                    hitted = true;
                }
            }
            int[] mapCell = new int[]{Math.round(GameModel.getGameSize((float) missile.getX())), Math.round(GameModel.getGameSize((float) missile.getY()))};
            if(!hitted){
                for (int i = mapCell[0]-1; i < mapCell[0] + 2 && !hitted; i++) {
                    for (int j = mapCell[1] - 1; j < mapCell[1] + 2 && !hitted; j++) {
                        Wall wall = null;
                        if (i >= 0 && i < walls.length && j >= 0 && j < walls[i].length) {
                            wall = walls[i][j];
                        }
                        if(wall != null){
                            if(wall.hits(missile.getCenterX(), missile.getCenterY())){
                                if(missile.getPlayerIndex() == controllerTankSprite.getPlayerIndex()){
                                    gameCallback.wallHit(i,j);
                                }
                                getChildren().remove(missile.getImage());
                                missileListIterator.remove();
                                hitted = true;
                            }
                        }
                    }
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

    public void resetStage(boolean[][] map){
        for (Missile missile : missiles) {
            getChildren().remove(missile.getImage());
        }


        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map.length; j++) {
                Wall wall = walls[i][j];
                if(wall != null){
                    getChildren().remove(wall.getImage());
                }
                walls[i][j] = null;
                if(map[i][j]){
                    wall = new Wall(
                            GameModel.getActualSize(1f),
                            GameModel.getActualSize(1f),
                            GameModel.getActualSize(i),
                            GameModel.getActualSize(j)
                    );
                    walls[i][j] = wall;
                    getChildren().add(wall.getImage());
                }
            }
        }

        missiles.clear();

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
                controllerTankSprite = new ControllerTankSprite(
                        i,
                        GameModel.getActualSize(GameConfig.TANK_SIZE),
                        GameModel.getActualSize(GameConfig.TANK_SIZE)) ;
                controllerTankSprite.setGameStage(this);
            } else {
                remoteTanks.add(new RemoteTankSprite(i,
                        GameModel.getActualSize(GameConfig.TANK_SIZE),
                        GameModel.getActualSize(GameConfig.TANK_SIZE)));
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

    public double[] calculateCollisionCorrection(double x, double y, double width, double height) {


        double[] vector = new double[2];
        if(x < 0){
            vector[0] = -x;
            x = 0;
        }
        if(y < 0){
            vector[1] = -y;
            y = 0;
        }
        if(x + width > GameModel.WINDOW_SIZE){
            vector[0] = -(x + width - GameModel.WINDOW_SIZE);
            x = GameModel.WINDOW_SIZE - width;
        }
        if(y + height > GameModel.WINDOW_SIZE){
            vector[1] = -(y + height - GameModel.WINDOW_SIZE);
            y = GameModel.WINDOW_SIZE - height;
        }
        int[] mapCell = new int[]{Math.round(GameModel.getGameSize((float) x)), Math.round(GameModel.getGameSize((float) y))};

        for (int i = mapCell[0]-1; i < mapCell[0] + 2; i++) {
            for (int j = mapCell[1]-1; j < mapCell[1] + 2; j++) {
                Wall wall = null;
                if(i>=0 && i<walls.length && j>=0 && j<walls[i].length){
                    wall = walls[i][j];
                    if(wall != null){
                        //wall.getImage().setRotate(wall.getImage().getRotate() + 5);
                    }
                }
                if(wall != null){
                    double xStartOverlap = x + width - wall.getX();
                    double xEndOverlap = wall.getX() + wall.getWidth() - x; // < 0

                    double yStartOverlap = y + height - wall.getY();
                    double yEndOverlap = wall.getY() + wall.getHeight() - y;

                    if(xStartOverlap > 0 && xEndOverlap > 0 && yStartOverlap > 0 && yEndOverlap > 0){
                        if(Math.min(xStartOverlap, xEndOverlap) < Math.min(yStartOverlap, yEndOverlap)){
                            if(xStartOverlap < xEndOverlap){
                                vector[0] -= xStartOverlap;
                                x -= xStartOverlap;
                            }else{
                                vector[0] += xEndOverlap;
                                x += xEndOverlap;
                            }
                        }else{
                            if(yStartOverlap < yEndOverlap){
                                vector[1] -= yStartOverlap;
                                y -= yStartOverlap;
                            }else{
                                vector[1] += yEndOverlap;
                                y += yEndOverlap;
                            }
                        }
                        //wall.getImage().setRotate(wall.getImage().getRotate() + 5);
                    }

                }

            }
        }
        return vector;
    }

    public void registerWallHit(int x, int y) {
        Wall wall = walls[x][y];
        if(wall != null){
            boolean broke = wall.hit();
            if (broke){
                getChildren().remove(wall.getImage());
                walls[x][y] = null;
            }
        }

    }
}
