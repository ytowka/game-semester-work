package org.danilkha.game;

import org.danilkha.game.api.GameLobbyListener;

import java.util.HashMap;
import java.util.Map;

public class Game implements GameLobbyListener {

    private final Map<String, Lobby> lobbies;

    private Map<String, Lobby> getLobbies() {
        return lobbies;
    }

    private Map<Integer, Player> players;

    public Game(){
        lobbies = new HashMap<>();
        players = new HashMap<>();
    }

    @Override
    public boolean createNewLobby(int playerId, String roomName, String playerName) {
        if(lobbies.containsKey(roomName)) {
            return false;
        }
        Player host = new Player(playerName, playerId);
        Lobby lobby = new Lobby(roomName, host);
        host.setConnectedLobby(lobby);
        lobbies.put(roomName, lobby);
        players.put(playerId, host);
        return true;
    }

    @Override
    public boolean connectToLobby(int playerId, String name) {
        Player player = new Player(name, playerId);
        Lobby lobby = lobbies.get(name);
        lobby.joinPlayer(player);
        player.setConnectedLobby(lobby);
        players.put(playerId, player);
        return false;
    }

    public Map<Integer, Player> getPlayers() {
        return players;
    }

    public Player getPlayerByClientId(int id){
        return players.get(id);
    }
}
