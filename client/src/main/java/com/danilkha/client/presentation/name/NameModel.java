package com.danilkha.client.presentation.name;

import com.danilkha.client.App;
import com.danilkha.client.presentation.AppScreen;
import com.danilkha.client.presentation.Navigator;
import com.danilkha.client.utils.BaseScreen;
import com.danilkha.client.utils.LiveData;
import org.danilkha.api.LobbyApi;
import org.danilkha.game.LobbyDto;

import java.util.Arrays;
import java.util.regex.Pattern;

public class NameModel extends BaseScreen<NameController> {

    private final LobbyDto joiningLobbyDto;
    private final Pattern namePattern = Pattern.compile("[A-z0-9_]+");

    private final Navigator<AppScreen> navigator;

    public LiveData<String> error = new LiveData<>();

    private final LobbyApi api;
    public NameModel(LobbyApi lobbyApi, LobbyDto lobbyDto, Navigator<AppScreen> navigator) {
        super("name.fxml");
        this.navigator = navigator;
        api = lobbyApi;
        controller.init(this);
        this.joiningLobbyDto = lobbyDto;
    }

    public void onJoinClicked(String name){
        System.out.println("on button click "+name+" ");
        if(!isNameFree(name)){
            error.setValue("Имя уже занято");
            return;
        }
        if (!isNameValid(name)) {
            error.setValue("Некорректное имя пользователя");
            return;
        }
        error.setValue(null);
        if(joiningLobbyDto == null){
            api.createNewLobby(name).addObserver(b ->{
                navigator.navigate(new AppScreen.LobbyRoom(new LobbyDto(
                        name,
                        new String[]{name}
                ), name));
            });
        }else{
            api.connectToLobby(joiningLobbyDto.hostName(), name).addObserver(b -> {
                navigator.navigate(new AppScreen.LobbyRoom(joiningLobbyDto, name));
            });
        }
    }

    private boolean isNameValid(String name){
        return namePattern.matcher(name).matches();
    }

    private boolean isNameFree(String name){
        if(joiningLobbyDto != null){
            return Arrays.stream(joiningLobbyDto.playerNames()).noneMatch(it -> it.equals(name));
        }
        return true;
    }
}
