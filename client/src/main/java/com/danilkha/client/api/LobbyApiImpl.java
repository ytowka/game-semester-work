package com.danilkha.client.api;

import org.danilkha.api.LobbyApi;
import org.danilkha.connection.Call;
import org.danilkha.connection.PackageReceiver;
import org.danilkha.game.LobbyDto;
import org.danilkha.utils.observable.Completable;
import org.danilkha.utils.observable.ObservableValue;

import java.util.Arrays;
import java.util.List;

public class LobbyApiImpl extends Api implements LobbyApi{

    public LobbyApiImpl(PackageReceiver server) {
        super(server);
    }

    @Override
    public ObservableValue<List<LobbyDto>> subscribeActiveLobbies() {
        return subscribe(SUBSCRIBE_LOBBIES).map(data -> Arrays.stream(data).map(LobbyDto::fromString).toList());
    }

    @Override
    public ObservableValue<String[]> subscribeLobbyPlayers() {
        return subscribe(LOBBY_PLAYERS);
    }

    @Override
    public Completable<List<LobbyDto>> getAllLobbies() {
            Completable<List<LobbyDto>> lobbies = new Completable<>();
         get(SUBSCRIBE_LOBBIES, new Call<>() {
            @Override
            public void onSuccess(String[] data) {
                lobbies.complete(Arrays.stream(data[0].split(","))
                        .map(LobbyDto::fromString).toList());
            }

            @Override
            public void onError(Throwable t) {
                t.printStackTrace();
                lobbies.complete(null);
            }
        });
         return lobbies;
    }

    @Override
    public Completable<Boolean> createNewLobby(String playerName) {
        return post(CREATE_NEW_LOBBY, playerName);
    }

    @Override
    public Completable<Boolean> connectToLobby(String name, String playerName) {
        System.out.println("connect to lobby");
        return post(CONNECT_TO_LOBBY, name, playerName);
    }

    @Override
    public Completable<Boolean> leaveLobby() {
        return post(LEAVE_LOBBY);
    }

    @Override
    public Completable<Boolean> startGame() {
        return post(START_GAME);
    }
}
