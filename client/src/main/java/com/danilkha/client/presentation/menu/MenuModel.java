package com.danilkha.client.presentation.menu;

import com.danilkha.client.presentation.AppScreen;
import com.danilkha.client.presentation.Navigator;
import com.danilkha.client.utils.BaseScreen;
import com.danilkha.client.utils.LiveData;
import org.danilkha.api.LobbyApi;
import org.danilkha.game.LobbyDto;

import java.util.ArrayList;
import java.util.List;

public class MenuModel extends BaseScreen<MenuController> {

    private final LobbyApi api;
    private final Navigator<AppScreen> navigator;

    public LiveData<List<LobbyDto>> lobbies = new LiveData<>(new ArrayList<>());

    public MenuModel(LobbyApi lobbyApi, Navigator<AppScreen> navigator) {
        super("menu.fxml");
        controller.init(this);

        this.api = lobbyApi;
        this.navigator = navigator;

        api.subscribeActiveLobbies().addObserver(value -> {
            lobbies.postValue(value);
        });
    }

    public void onLobbySelected(int index){
        LobbyDto lobbyDto = lobbies.getValue().get(index);
        navigator.navigate(new AppScreen.LobbyRoom(lobbyDto));
    }

    public void onCreateNewLobbyClicked(){
        navigator.navigate(new AppScreen.LobbyRoom(null));
    }
}
