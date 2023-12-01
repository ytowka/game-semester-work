package com.danilkha.client.api;

import org.danilkha.api.LobbyApi;
import org.danilkha.connection.Call;
import org.danilkha.connection.PackageReceiver;
import org.danilkha.game.Lobby;
import org.danilkha.utils.observable.Completable;
import org.danilkha.utils.observable.ObservableValue;

import java.util.Arrays;
import java.util.List;

public class LobbyApiImpl extends Api implements LobbyApi{

    public LobbyApiImpl(PackageReceiver server) {
        super(server);
    }

    @Override
    public ObservableValue<List<Lobby>> subscribeActiveLobbies() {
        return subscribe(SUBSCRIBE_LOBBIES).map(string -> Arrays.
                stream(string.split(","))
                .map(Lobby::fromString).toList()
        );
    }

    @Override
    public Completable<Boolean> createNewLobby(String name, String playerName) {
        return post(CREATE_NEW_LOBBY, name, playerName);
    }

    @Override
    public Completable<Boolean> connectToLobby(String name, String playerName) {
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
