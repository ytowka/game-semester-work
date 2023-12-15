package org.danilkha.middleware;

import org.danilkha.api.LobbyApi;
import org.danilkha.connection.ClientRequest;
import org.danilkha.connection.Response;
import org.danilkha.connection.Server;
import org.danilkha.game.Game;
import org.danilkha.game.LobbyDto;
import org.danilkha.game.Player;
import org.danilkha.protocol.Protocol;
import org.danilkha.utils.observable.ObservableValue;

public class GameController extends RouterController {

    private final Game game;
    private final Server server;

    public GameController(Game game, Server server) {
        super(server);
        this.game = game;
        this.server = server;


        addGetMapping(LobbyApi.START_GAME, request -> {
            System.out.println("start game in lobby "+ request.clientId());
            Player player = game.getPlayerByClientId(request.clientId());
            player.getConnectedLobby().startGame();
            return new String[]{};
        });

        addGetMapping(LobbyApi.CREATE_NEW_LOBBY, request -> {
            System.out.println("create new lobby: "+request.data()[0]);
            String playerName = request.data()[0];
            game.createNewLobby(request.clientId(), playerName, playerName);
            return new String[]{};
        });

        addGetMapping(LobbyApi.CONNECT_TO_LOBBY, request -> {
            System.out.println("connect to lobby: "+request.clientId());
            String host = request.data()[0];
            String playerName = request.data()[1];
            game.connectToLobby(request.clientId(), host, playerName);
            return new String[]{};
        });

        addHandler(LobbyApi.LEAVE_LOBBY, request -> {

        });

        addHandler(LobbyApi.LOBBY_PLAYERS, request -> {
            System.out.println("request all lobbies");

        });

        addHandler(LobbyApi.SUBSCRIBE_LOBBIES, request -> {
            System.out.println("subscribe lobbies");
            server.disposeOnDisconnect(request.clientId(), game.subscribeLobbies(), lobbies -> {
                server.receiveData(request.clientId(), Protocol.buildResponse(new Response(
                        Response.Type.EMIT,
                        request.path(),
                        lobbies.stream().map(lobby -> lobby.toDto().serialize()).toArray(String[]::new)
                )));
            });
        });

        addHandler(LobbyApi.LOBBY_PLAYERS, request -> {
            System.out.println("subscribe player");
            Player player = game.getPlayerByClientId(request.clientId());
            if(player != null && player.getConnectedLobby() != null){
                server.disposeOnDisconnect(request.clientId(), player.getConnectedLobby().getObservableMembers(), members ->{
                    server.receiveData(request.clientId(), Protocol.buildResponse(new Response(
                            Response.Type.EMIT,
                            request.path(),
                            members.stream().map(Player::getName).toArray(String[]::new)
                    )));
                });
            }
        });

        addHandler(LobbyApi.AWAIT_GAME_START, request -> {
            System.out.println("await game start");
            Player player = game.getPlayerByClientId(request.clientId());
            if(player != null && player.getConnectedLobby() != null){
                server.disposeOnDisconnect(request.clientId(), player.getConnectedLobby().isStarted(), isStarted ->{
                    if(isStarted){
                        server.receiveData(request.clientId(), Protocol.buildResponse(new Response(
                                Response.Type.EMIT,
                                request.path(),
                                new String[]{"true"}
                        )));
                    }
                });
            }
        });

        server.addClientDisconnectLister(((clientId, e) -> {
            game.disconnect(clientId);
        }));
    }
}
