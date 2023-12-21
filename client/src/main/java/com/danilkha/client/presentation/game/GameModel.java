package com.danilkha.client.presentation.game;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import org.danilkha.api.GameEvent;
import org.danilkha.api.GameRoundApi;
import org.danilkha.config.GameConfig;

public class GameModel implements GameCallback{

    public final Scene scene;
    private final GameRoundApi gameRoundApi;

    public static Label debugInfo;

    private final GameStage gameStage;

    public static final int WINDOW_SIZE = 900;

    public GameModel(GameRoundApi gameRoundApi, String[] playerNames, String me) {
        this.gameRoundApi = gameRoundApi;

        gameStage = new GameStage(playerNames, me,  GameConfig.MAP_SIZE, GameConfig.MAP_SIZE, this);

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
                        gameStage.addMissile(shoot.playerIndex(), shoot.x(), shoot.y(), shoot.angle());
                    }
                    if(gameEvent instanceof GameEvent.HitTank hit){
                        gameStage.registerHit(hit.to());
                    }
                    if(gameEvent instanceof GameEvent.Destroy destroy){
                        gameStage.notifyPlayerDead(destroy.playerIndex());
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
