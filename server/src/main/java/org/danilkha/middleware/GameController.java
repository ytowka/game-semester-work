package org.danilkha.middleware;

import org.danilkha.api.LobbyApi;
import org.danilkha.connection.Server;
import org.danilkha.game.Game;
import org.danilkha.game.Player;

public class GameController extends RouterController {

    private final Game game;
    private final Server server;

    public GameController(Game game, Server server) {
        this.game = game;
        this.server = server;

        addRoute(LobbyApi.START_GAME, request -> {
            System.out.println("start game in lobby "+ request.clientId());
            Player player = game.getPlayerByClientId(request.clientId());
            player.getConnectedLobby().startGame();
            server.receiveData(request.clientId());
        });

        addRoute(LobbyApi.CREATE_NEW_LOBBY, request -> {
            System.out.println("create new lobby: "+request.data()[0]);
            String playerName = request.data()[0];
            game.createNewLobby(request.clientId(), playerName, playerName);
        });

        addRoute(LobbyApi.CONNECT_TO_LOBBY, request -> {
            System.out.println("connect to lobby: "+request.clientId());
            String host = request.data()[0];
            String playerName = request.data()[1];
            game.connectToLobby(request.clientId(), host, playerName);
        });

        addRoute(LobbyApi.LEAVE_LOBBY, request -> {

        });

        addRoute(LobbyApi.LOBBY_PLAYERS, request -> {
            System.out.println("request all lobbies");

        });

        addRoute(LobbyApi.SUBSCRIBE_LOBBIES, request -> {

        });
    }
}
