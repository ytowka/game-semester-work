package org.danilkha.api;

import org.danilkha.game.LobbyDto;
import org.danilkha.utils.observable.Completable;
import org.danilkha.utils.observable.ObservableValue;

import java.util.List;

public interface LobbyApi {

    String CREATE_NEW_LOBBY = "lobby/new";

    String SUBSCRIBE_LOBBIES = "lobby/all";
    String CONNECT_TO_LOBBY = "lobby/connect";
    String LOBBY_PLAYERS = "lobby/players";
    String LEAVE_LOBBY = "lobby/leave";
    String START_GAME = "lobby/start";
    String AWAIT_GAME_START = "lobby/game_start";

    ObservableValue<List<LobbyDto>> subscribeActiveLobbies();
    ObservableValue<String[]> subscribeLobbyPlayers();
    Completable<Boolean> createNewLobby(String playerName);
    Completable<Boolean> connectToLobby(String name, String playerName);
    Completable<Boolean> leaveLobby();
    Completable<Boolean> startGame();
    ObservableValue<String[]> awaitGameStart();
}
