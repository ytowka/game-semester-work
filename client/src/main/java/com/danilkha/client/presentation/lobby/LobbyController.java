package com.danilkha.client.presentation.lobby;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import org.danilkha.game.LobbyDto;

import java.util.Arrays;
import java.util.List;

public class LobbyController {


    @FXML
    private Button startGameButton;

    @FXML
    private ListView<String> players;

    private LobbyModel model;

    public void init(LobbyModel model) {
        this.model = model;
        startGameButton.setVisible(model.isHost());

        players.setItems(FXCollections.observableList(mapPlayers(model.lobbyDto)));
    }

    private static List<String> mapPlayers(LobbyDto lobbyDto){
        return Arrays.stream(lobbyDto.playerNames()).map(name -> {
            if(name.equals(lobbyDto.hostName())){
                return name+" (host)";
            }else return name;
        }).toList();
    }

    @FXML
    void onStartGame(){
        model.startGame();
    }
}
