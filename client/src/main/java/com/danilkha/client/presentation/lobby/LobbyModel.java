package com.danilkha.client.presentation.lobby;

import com.danilkha.client.utils.BaseScreen;
import org.danilkha.api.LobbyApi;
import org.danilkha.game.LobbyDto;

public class LobbyModel extends BaseScreen<LobbyController> {


    private final LobbyDto lobbyDto;
    private final String playerName;
    private final LobbyApi api;

    public LobbyModel(LobbyApi api, LobbyDto lobbyDto, String playerName) {
        super("lobby.fxml");
        controller.init(this);
        this.playerName = playerName;
        this.lobbyDto = lobbyDto;
        this.api = api;
    }

    public boolean isHost(){
        return lobbyDto.hostName().equals(playerName);
    }

    public void startGame(){
        System.out.println("start game");
        api.startGame();
    }
}
