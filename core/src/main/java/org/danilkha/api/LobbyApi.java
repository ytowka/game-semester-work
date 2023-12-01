package org.danilkha.api;

import org.danilkha.game.Lobby;
import org.danilkha.utils.observable.Completable;
import org.danilkha.utils.observable.MutableObservableValue;
import org.danilkha.utils.observable.ObservableValue;

import java.util.List;

public interface LobbyApi {

    public static final String CREATE_NEW_LOBBY = "lobby/new";
    public static final String SUBSCRIBE_LOBBIES = "lobby/all";
    public static final String CONNECT_TO_LOBBY = "lobby/connect";
    public static final String LEAVE_LOBBY = "lobby/leave";
    public static final String START_GAME = "lobby/start";

    ObservableValue<List<Lobby>> subscribeActiveLobbies();
    Completable<Boolean> createNewLobby(String name, String playerName);
    Completable<Boolean> connectToLobby(String name, String playerName);
    Completable<Boolean> leaveLobby();
    Completable<Boolean> startGame();
}
