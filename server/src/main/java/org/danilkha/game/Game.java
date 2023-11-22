package org.danilkha.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Game implements GameActionListener{

    private final Map<String, Lobby> lobbies;

    public Game(){
        lobbies = new HashMap<>();
    }

    @Override
    public boolean createNewLobby(String name, int hostUserId) {
        if(lobbies.containsKey(name)) {
            return false;
        }
        Lobby lobby = new Lobby(name, hostUserId);
        lobbies.put(name, lobby);
        return true;
    }

    @Override
    public boolean connectToLobby(String name, int userId) {
        return false;
    }
}
