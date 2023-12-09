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

        model.players.addObserver(playerList -> {
            if(playerList != null){
                players.setItems(FXCollections.observableList(mapPlayers(playerList, model.lobbyDto.hostName())));
            }
        });
    }

    private static List<String> mapPlayers(String[] playerNames, String host){
        return Arrays.stream(playerNames).map(name -> {
            if(name.equals(host)){
                return name+" (host)";
            }else return name;
        }).toList();
    }

    @FXML
    void onStartGame(){
        model.startGame();
    }
}
