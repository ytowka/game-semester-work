package com.danilkha.client.presentation.lobby;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class LobbyController {


    @FXML
    private Button startGameButton;

    private LobbyModel model;

    public void init(LobbyModel model) {
        this.model = model;
        startGameButton.setVisible(model.isHost());
    }

    @FXML
    void onStartGame(){
        model.startGame();
    }
}
