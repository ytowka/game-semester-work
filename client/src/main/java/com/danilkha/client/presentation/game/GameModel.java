package com.danilkha.client.presentation.game;

import com.danilkha.client.presentation.game.tank.ControllerTankActor;
import com.danilkha.client.presentation.game.tank.RemoteTankActor;
import com.danilkha.client.presentation.game.tank.TankActor;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import org.danilkha.api.GameEvent;
import org.danilkha.api.GameRoundApi;
import org.danilkha.config.GameConfig;

import java.util.Arrays;

public class GameModel{

    public final Scene scene;
    private final GameRoundApi gameRoundApi;

    public static Label debugInfo;

    private final GameStage gameStage;

    public GameModel(GameRoundApi gameRoundApi, String[] playerName, String me) {
        this.gameRoundApi = gameRoundApi;


        gameStage = new GameStage(GameConfig.MAP_SIZE, GameConfig.MAP_SIZE);

        gameStage.maxHeight(900);
        gameStage.maxWidth(900);
        gameStage.minHeight(900);
        gameStage.minWidth(900);
        gameStage.setPrefWidth(900);
        gameStage.setPrefHeight(900);

        debugInfo = new Label();

        System.out.println(Arrays.toString(playerName));
        for (int i = 0; i < playerName.length; i++) {
            String s = playerName[i];
            if (s.equals(me)) {
                gameStage.addActor(new ControllerTankActor(i, 50, 50));
            } else {
                gameStage.addActor(new RemoteTankActor(i, 50, 50));
            }
        }

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
            for (GameEvent gameEvent : value) {
                if(gameEvent instanceof GameEvent.PlayerMove playerMove){
                    gameStage.moveTank(playerMove.playerIndex(), playerMove.x(), playerMove.y(), playerMove.angle());
                }
            }
        });

        gameStage.getPlayerMove().addObserver(vector -> {
            sendPlayerMove(vector[0], vector[1], vector[2]);
        });


        gameStage.start();
    }

    protected void sendPlayerMove(float x, float y, float angle){
        gameRoundApi.moveTo(x, y, angle);
    }

    protected void shoot(float x, float y, float angle){

    }
}
