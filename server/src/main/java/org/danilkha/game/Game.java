package org.danilkha.game;

import org.danilkha.utils.observable.EqualityPolicy;
import org.danilkha.utils.observable.MutableObservableValue;
import org.danilkha.utils.observable.ObservableValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Game {

    private final Map<String, Lobby> lobbies;

    private MutableObservableValue<List<Lobby>> listObservableValue = new MutableObservableValue<>(new ArrayList<>(), EqualityPolicy.NEVER);

    private Map<Integer, Player> players;

    public Game(){
        lobbies = new HashMap<>();
        players = new HashMap<>();
    }

    public boolean createNewLobby(int playerId, String roomName, String playerName) {
        if(lobbies.containsKey(roomName)) {
            return false;
        }
        Player host = new Player(playerName, playerId);
        Lobby lobby = new Lobby(roomName, host);
        host.setConnectedLobby(lobby);
        lobbies.put(roomName, lobby);
        players.put(playerId, host);
        listObservableValue.getValue().add(lobby);
        listObservableValue.invalidate();
        return true;
    }

    public boolean connectToLobby(int playerId, String host, String playerName) {
        Player player = new Player(playerName, playerId);
        Lobby lobby = lobbies.get(host);
        lobby.joinPlayer(player);
        player.setConnectedLobby(lobby);
        players.put(playerId, player);
        listObservableValue.invalidate();
        return false;
    }

    public boolean disconnect(int playerId){
        Player player = players.get(playerId);
        boolean shouldDeleteLobby = false;
        if(player != null){
            Lobby lobby = player.getConnectedLobby();
            if(lobby != null){
                shouldDeleteLobby = lobby.leavePlayer(playerId);
                if(shouldDeleteLobby){
                    lobbies.remove(lobby.getName());
                    listObservableValue.getValue().remove(lobby);
                    listObservableValue.invalidate();
                }
            }
            players.remove(playerId);
        }
        return shouldDeleteLobby;
    }

    public Map<Integer, Player> getPlayers() {
        return players;
    }

    private Map<String, Lobby> getLobbies() {
        return lobbies;
    }

    public ObservableValue<List<Lobby>> subscribeLobbies(){
        return listObservableValue;
    }

    public Player getPlayerByClientId(int id){
        return players.get(id);
    }
}
