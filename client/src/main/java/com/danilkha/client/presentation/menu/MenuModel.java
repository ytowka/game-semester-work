package com.danilkha.client.presentation.menu;

import com.danilkha.client.utils.BaseScreen;
import com.danilkha.client.utils.LiveData;
import org.danilkha.api.LobbyApi;
import org.danilkha.game.Lobby;

import java.util.ArrayList;
import java.util.List;

public class MenuModel extends BaseScreen<MenuController> {

    private final LobbyApi api;
    private final Callback listener;

    public LiveData<List<Lobby>> lobbies = new LiveData<>(new ArrayList<>());

    public MenuModel(LobbyApi lobbyApi, Callback listener) {
        super("menu.fxml");
        controller.init(this);

        this.listener = listener;
        this.api = lobbyApi;

        api.subscribeActiveLobbies().addObserver(value -> {
            lobbies.postValue(value);
        });
    }

    public void onLobbySelected(int index){
        Lobby lobby = lobbies.getValue().get(index);
        listener.onLobbySelected(lobby);
    }

    public void onCreateNewLobbyClicked(){
        listener.onCreateNewLobbyClicked();
    }

    public interface Callback {
        void onLobbySelected(Lobby lobby);
        void onCreateNewLobbyClicked();
    }
}
