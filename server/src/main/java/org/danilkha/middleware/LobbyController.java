package org.danilkha.middleware;

import org.danilkha.api.GameEvent;
import org.danilkha.api.GameRoundApi;
import org.danilkha.api.LobbyApi;
import org.danilkha.connection.Response;
import org.danilkha.connection.Server;
import org.danilkha.game.Game;
import org.danilkha.game.Lobby;
import org.danilkha.game.Player;
import org.danilkha.protocol.Protocol;
import org.danilkha.utils.coding.EncodingUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Float.intBitsToFloat;

public class LobbyController extends RouterController {

    private final Game game;
    private final Server server;

    public LobbyController(Game game, Server server) {
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
                Player player = game.getPlayerByClientId(request.clientId());
                if(player == null){
                    server.receiveData(request.clientId(), Protocol.buildResponse(new Response(
                            Response.Type.EMIT,
                            request.path(),
                            lobbies.stream().map(lobby -> lobby.toDto().serialize()).toArray(String[]::new)
                    )));
                }
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

        addHandler(GameRoundApi.SUBSCRIBE_GAME_EVENTS, request -> {
            Lobby lobby = game.getPlayerByClientId(request.clientId()).getConnectedLobby();

            lobby.currentRound.addObserver(round -> {
                if(round != null){
                    List<GameEvent> gameEvents = new ArrayList<>(round.getSingleEvents());
                    round.getPlayers().forEach((id, playerInfo) -> {
                        if(id != request.clientId()){
                            gameEvents.add(new GameEvent.PlayerMove(playerInfo.getIndex(), playerInfo.getX(), playerInfo.getY(), playerInfo.getAngle()));
                        }
                    });

                    String[] data = new String[gameEvents.size()];
                    for (int i = 0; i < gameEvents.size(); i++) {
                        data[i] = gameEvents.get(i).serialize();
                    }

                    server.receiveData(request.clientId(), Protocol.buildResponse(new Response(
                            Response.Type.EMIT,
                            request.path(),
                            data
                    )));
                }
            });
        });

        addHandler(GameRoundApi.MOVE_TO, request -> {
            Player player = game.getPlayerByClientId(request.clientId());
            Lobby lobby = player.getConnectedLobby();
            List<Float> data = Arrays.stream(EncodingUtil.decodeStringToIntArray(request.data()[0]))
                    .mapToObj(Float::intBitsToFloat)
                    .toList();
            lobby.currentRound.getValue().moveTo(request.clientId(), data.get(0), data.get(1), data.get(2));
        });

        addHandler(GameRoundApi.SHOOT, request -> {
            Player player = game.getPlayerByClientId(request.clientId());
            Lobby lobby = player.getConnectedLobby();
            List<Float> data = Arrays.stream(EncodingUtil.decodeStringToIntArray(request.data()[0]))
                    .mapToObj(Float::intBitsToFloat)
                    .toList();
            lobby.currentRound.getValue().shoot(request.clientId(), data.get(0), data.get(1), data.get(2));
        });

        addHandler(GameRoundApi.HIT_PLAYER, request -> {
            Player player = game.getPlayerByClientId(request.clientId());
            Lobby lobby = player.getConnectedLobby();
            int[] data = EncodingUtil.decodeStringToIntArray(request.data()[0]);
            lobby.currentRound.getValue().hit(request.clientId(), data[0]);
        });

        addHandler(GameRoundApi.HIT_WALL, request -> {
            Player player = game.getPlayerByClientId(request.clientId());
            Lobby lobby = player.getConnectedLobby();
            int[] data = EncodingUtil.decodeStringToIntArray(request.data()[0]);
            lobby.currentRound.getValue().hitWall(data[0], data[1]);
        });


        server.addClientDisconnectLister(((clientId, e) -> {
            game.disconnect(clientId);
        }));
    }
}
