package org.danilkha.game;

import org.danilkha.game.api.GameLobby;
import org.danilkha.game.round.Round;

import java.util.ArrayList;
import java.util.List;

public class Lobby implements GameLobby {

    private static final int maxPlayers = 4;

    private final String name;
    private final int hostUserId;

    private final List<Player> members;

    private Round currentRound = null;


    public Lobby(String name, Player host) {
        this.name = name;
        this.hostUserId = host.getId();
        members = new ArrayList<>();
        members.add(host);
    }

    @Override
    public boolean startGame() {
        return false;
    }

    @Override
    public boolean isStarted() {
        return false;
    }

    @Override
    public boolean joinPlayer(Player player) {
        if(members.size() > maxPlayers){
            return false;
        }
        members.add(player);
        return false;
    }

    public Round getCurrentRound(){
        return currentRound;
    }
}
