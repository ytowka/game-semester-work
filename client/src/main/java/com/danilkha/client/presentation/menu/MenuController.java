package com.danilkha.client.presentation.menu;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import org.danilkha.game.LobbyDto;

public class MenuController {


    @FXML
    Label welcomeText;

    @FXML
    Button btnNewLobby;

    @FXML
    ListView<String> listLobbies;

    private MenuModel model;
    void init(MenuModel menuModel){
        model = menuModel;

        ObservableList<String> listData = FXCollections.observableArrayList();
        model.lobbies.addObserver(lobbies -> {
            if(lobbies != null){
                listData.setAll(lobbies.stream().map(MenuController::lobbyForamtter).toList());
            }
        });

        listLobbies.setItems(listData);

        listLobbies.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            model.onLobbySelected(newValue.intValue());
        });
    }

    @FXML
    public void onNewLobbyClicked(){
        System.out.println("new lobby");
        model.onCreateNewLobbyClicked();
    }

    private static String lobbyForamtter(LobbyDto lobbyDto){
        return lobbyDto.hostName()+" "+ lobbyDto.playerNames().length+"/4";
    }
}
