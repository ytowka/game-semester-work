package com.danilkha.client.presentation.lobby;

import com.danilkha.client.presentation.AppScreen;
import com.danilkha.client.presentation.Navigator;
import com.danilkha.client.utils.BaseScreen;
import org.danilkha.api.LobbyApi;
import org.danilkha.game.LobbyDto;

import java.util.List;

public class LobbyModel extends BaseScreen<LobbyController> {


    final LobbyDto lobbyDto;
    private final String playerName;
    private final LobbyApi api;
    private final Navigator<AppScreen> navigator;

    public LobbyModel(LobbyApi api, LobbyDto lobbyDto, String playerName, Navigator<AppScreen> navigator) {
        super("lobby.fxml");
        this.navigator = navigator;
        this.playerName = playerName;
        this.lobbyDto = lobbyDto;
        this.api = api;
        controller.init(this);
    }

    public boolean isHost(){
        return lobbyDto.hostName().equals(playerName);
    }

    public void startGame(){
        System.out.println("start game");
        api.startGame();
    }
}
