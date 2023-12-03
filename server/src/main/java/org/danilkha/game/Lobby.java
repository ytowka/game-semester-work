package org.danilkha.game;

import org.danilkha.game.round.Round;
import org.danilkha.utils.observable.EqualityPolicy;
import org.danilkha.utils.observable.MutableObservableValue;
import org.danilkha.utils.observable.ObservableValue;

import java.util.ArrayList;
import java.util.List;

public class Lobby{

    private static final int maxPlayers = 4;

    private final String name;
    private final int hostUserId;

    private final List<Player> members;
    private final MutableObservableValue<List<Player>> observableMembers;

    private final MutableObservableValue<Boolean> isGameStarted;

    private Round currentRound = null;


    public Lobby(String name, Player host) {
        this.name = name;
        this.hostUserId = host.getId();
        members = new ArrayList<>();
        members.add(host);
        observableMembers = new MutableObservableValue<>(members, EqualityPolicy.NEVER);
        isGameStarted = new MutableObservableValue<>(false);
    }


    public boolean startGame() {
        if(members.size() > 1){
            isGameStarted.setValue(true);
            return true;
        }
        return false;
    }


    public ObservableValue<Boolean> isStarted() {
        return isGameStarted;
    }


    public boolean joinPlayer(Player player) {
        if(members.size() > maxPlayers){
            return false;
        }
        members.add(player);
        observableMembers.invalidate();
        return false;
    }

    public Round getCurrentRound(){
        return currentRound;
    }
}
