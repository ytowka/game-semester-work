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

    public String getName() {
        return name;
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

    public ObservableValue<List<Player>> getObservableMembers() {
        return observableMembers;
    }

    public boolean joinPlayer(Player player) {
        if(members.size() > maxPlayers){
            return false;
        }
        members.add(player);
        observableMembers.invalidate();
        return false;
    }

    public boolean leavePlayer(int userId){
        if(userId ==  hostUserId){
            return true;
        }else{
            Player player = null;
            for (Player p : members){
                if(p.getId() == userId){
                    player = p;
                    break;
                }
            }
            if(player != null){
                members.remove(player);
                observableMembers.invalidate();
            }
            return false;
        }
    }

    public Round getCurrentRound(){
        return currentRound;
    }

    public LobbyDto toDto(){
        return new LobbyDto(
                name,
                members.stream()
                        .map(Player::getName)
                        .toArray(String[]::new)
        );
    }
}
