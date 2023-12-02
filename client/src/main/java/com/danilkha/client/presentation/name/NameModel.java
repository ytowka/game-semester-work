package com.danilkha.client.presentation.name;

import com.danilkha.client.utils.BaseScreen;
import com.danilkha.client.utils.LiveData;
import org.danilkha.api.LobbyApi;
import org.danilkha.game.Lobby;

import java.util.Arrays;
import java.util.Map;
import java.util.regex.Pattern;

public class NameModel extends BaseScreen<NameController> {

    private final Callback callback;
    private final Lobby joiningLobby;
    private final Pattern namePattern = Pattern.compile("[A-z0-9_]");

    public LiveData<String> error = new LiveData<>();

    private final LobbyApi api;
    public NameModel(Callback callback, LobbyApi lobbyApi, Lobby lobby) {
        super("name.fxml");
        this.callback = callback;
        api = lobbyApi;
        controller.init(this);
        this.joiningLobby = lobby;
    }

    public void onJoinClicked(String name){
        if(!isNameFree(name)){
            error.setValue("Имя уже занято");
            return;
        }
        if (!isNameValid(name)) {
            error.setValue("Некорректное имя пользователя");
            return;
        }
        callback.onJoinClicked(name);
        if(joiningLobby == null){
            api.connectToLobby(name, name);
        }else{
            api.connectToLobby(joiningLobby.hostName(), name);
        }
    }

    private boolean isNameValid(String name){
        return namePattern.matcher(name).matches();
    }

    private boolean isNameFree(String name){
        if(joiningLobby != null){
            return Arrays.stream(joiningLobby.playerNames()).noneMatch(it -> it.equals(name));
        }
        return true;
    }

    public interface Callback{
        void onJoinClicked(String name);
    }
}
