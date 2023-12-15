package com.danilkha.client.presentation.lobby;

import com.danilkha.client.App;
import com.danilkha.client.presentation.AppScreen;
import com.danilkha.client.presentation.Navigator;
import com.danilkha.client.utils.BaseScreen;
import com.danilkha.client.utils.LiveData;
import javafx.application.Platform;
import org.danilkha.api.LobbyApi;
import org.danilkha.game.LobbyDto;
import org.danilkha.utils.observable.ObservableValue;
import org.danilkha.utils.observable.Observer;

import java.util.Arrays;
import java.util.List;

public class LobbyModel extends BaseScreen<LobbyController> {


    final LobbyDto lobbyDto;
    private final String playerName;
    private final LobbyApi api;
    private final Navigator<AppScreen> navigator;

    public final LiveData<String[]> players;

    public LobbyModel(LobbyApi api, LobbyDto lobbyDto, String playerName, Navigator<AppScreen> navigator) {
        super("lobby.fxml");
        this.navigator = navigator;
        this.playerName = playerName;
        this.lobbyDto = lobbyDto;
        this.api = api;
        players = new LiveData<>();

        api.subscribeLobbyPlayers().addObserver(this.players::postValue);

        api.awaitGameStart().addObserver(s -> {
            System.out.println(Arrays.toString(s));
            if(s != null && s.length > 0){
                Platform.runLater(() -> {
                    navigator.navigate(new AppScreen.Game());
                });
            }
        });

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
