package com.danilkha.client.presentation.game;

import com.danilkha.client.presentation.game.tank.ControllerTankSprite;
import com.danilkha.client.presentation.game.tank.RemoteTankSprite;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import org.danilkha.api.GameEvent;
import org.danilkha.api.GameRoundApi;
import org.danilkha.config.GameConfig;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameModel implements GameCallback{

    public final Scene scene;
    private final GameRoundApi gameRoundApi;

    public static Label debugInfo;

    private final GameStage gameStage;

    public static final int WINDOW_SIZE = 900;

    public GameModel(GameRoundApi gameRoundApi, String[] playerName, String me) {
        this.gameRoundApi = gameRoundApi;


        ControllerTankSprite controllerTankSprite = null;
        List<RemoteTankSprite> remoteTankSprites = new ArrayList<>();
        System.out.println(Arrays.toString(playerName));
        for (int i = 0; i < playerName.length; i++) {
            String s = playerName[i];
            if (s.equals(me)) {
                controllerTankSprite = new ControllerTankSprite(i, 50, 50);
            } else {
                remoteTankSprites.add(new RemoteTankSprite(i, 50, 50));
            }
        }

        gameStage = new GameStage(controllerTankSprite, remoteTankSprites, GameConfig.MAP_SIZE, GameConfig.MAP_SIZE, this);

        controllerTankSprite.setGameStage(gameStage);
        for (RemoteTankSprite remoteTankSprite : remoteTankSprites) {
            remoteTankSprite.setGameStage(gameStage);
        }

        gameStage.setPrefWidth(WINDOW_SIZE);
        gameStage.setPrefHeight(WINDOW_SIZE);

        debugInfo = new Label();

        BorderPane root = new BorderPane(gameStage);

        root.setTop(debugInfo);

        scene = new Scene(root);

        scene.setOnKeyPressed(event -> {
            gameStage.inputListener.onKeyDown(event.getCode().getCode());
        });

        scene.setOnKeyReleased(event -> {
            gameStage.inputListener.onKeyUp(event.getCode().getCode());
        });

        gameStage.setOnMouseMoved(event -> {
            gameStage.inputListener.onMouseMoved((float) event.getX(), (float) event.getY());
        });

        gameStage.setOnMouseDragged(event -> {
            gameStage.inputListener.onMouseMoved((float) event.getX(), (float) event.getY());
        });

        gameStage.setOnMouseClicked(event -> {
            gameStage.inputListener.onMouseClicked((float) event.getX(), (float) event.getY());
        });

        gameRoundApi.subscribeGameEvents().addObserver(value -> {
            Platform.runLater(() -> {
                for (GameEvent gameEvent : value) {
                    if(gameEvent instanceof GameEvent.PlayerMove playerMove){
                        gameStage.moveTank(
                                playerMove.playerIndex(),
                                getActualSize(playerMove.x()),
                                getActualSize(playerMove.y()),
                                playerMove.angle()
                        );
                    }
                    if(gameEvent instanceof GameEvent.Shoot shoot){
                        gameStage.addMissile(shoot.x(), shoot.y(), shoot.angle());
                    }
                }
            });
        });

        gameStage.getPlayerMove().addObserver(vector -> {
            sendPlayerMove(vector[0], vector[1], vector[2]);
        });


        gameStage.start();
    }


    public static float getActualSize(float gameValue){
        return gameValue * WINDOW_SIZE / GameConfig.MAP_SIZE;
    }

    public static float getGameSize(float windowValue){
        return windowValue / WINDOW_SIZE * GameConfig.MAP_SIZE;
    }


    protected void sendPlayerMove(float x, float y, float angle){
        gameRoundApi.moveTo(getGameSize(x), getGameSize(y), angle);
    }


    @Override
    public void shoot(float centerX, float centerY, double degAngle) {
        gameRoundApi.shoot(centerX, centerY, (float) degAngle);
    }

    @Override
    public void playerHit(int to) {
        gameRoundApi.hitPlayer(to);
    }

    @Override
    public void wallHit(int x, int y) {

    }
}
