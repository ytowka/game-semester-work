package com.danilkha.client.presentation.game;

import com.danilkha.client.utils.BaseScreen;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.danilkha.api.GameRoundApi;
import org.danilkha.config.GameConfig;

public class GameModel{

    public final Scene scene;
    private final GameRoundApi gameRoundApi;

    public static Label debugInfo;

    public GameModel(GameRoundApi gameRoundApi) {
        this.gameRoundApi = gameRoundApi;


        GameStage gameStage = new GameStage(GameConfig.MAP_SIZE, GameConfig.MAP_SIZE);

        gameStage.maxHeight(900);
        gameStage.maxWidth(900);
        gameStage.minHeight(900);
        gameStage.minWidth(900);
        gameStage.setPrefWidth(900);
        gameStage.setPrefHeight(900);

        debugInfo = new Label();

        gameStage.addActor(new TankActor(0, 50, 50));

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

        gameStage.start();
    }
}
