package org.danilkha.api;

import org.danilkha.game.Lobby;
import org.danilkha.utils.observable.Completable;
import org.danilkha.utils.observable.MutableObservableValue;
import org.danilkha.utils.observable.ObservableValue;

import java.util.List;

public interface LobbyApi {

    String CREATE_NEW_LOBBY = "lobby/new";
    String SUBSCRIBE_LOBBIES = "lobby/all";
    String CONNECT_TO_LOBBY = "lobby/connect";
    String LEAVE_LOBBY = "lobby/leave";
    String START_GAME = "lobby/start";

    ObservableValue<List<Lobby>> subscribeActiveLobbies();
    Completable<List<Lobby>> getAllLobbies();
    Completable<Boolean> createNewLobby(String name, String playerName);
    Completable<Boolean> connectToLobby(String name, String playerName);
    Completable<Boolean> leaveLobby();
    Completable<Boolean> startGame();
}
