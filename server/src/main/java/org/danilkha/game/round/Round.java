package org.danilkha.game.round;

import org.danilkha.api.GameEvent;
import org.danilkha.game.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Round {
    private int index;

    private Map<Integer, PlayerInfo> players;
    private List<GameEvent> singleEvents = new ArrayList<>();

    public Round(List<PlayerInfo> players){
        this.players = new HashMap<>();
        for (PlayerInfo player : players) {
            this.players.put(player.getPlayer().getId(), player);
        }
    }

    public void moveTo(int clientId, float x, float y, float angle) {
        PlayerInfo playerInfo = players.get(clientId);
        playerInfo.setAngle(angle);
        playerInfo.setX(x);
        playerInfo.setY(y);
    }

    public void shoot(int clientId, float x, float y, float directionAngle) {
        singleEvents.add(new GameEvent.Shoot(
                players.get(clientId).getIndex(),
                x,
                y,
                directionAngle
        ));
    }

    public void hit(int fromId, int toId) {

    }

    public void resetSingleEvents(){
        singleEvents.clear();
    }
    public List<GameEvent> getSingleEvents(){
        return singleEvents;
    }

    public Map<Integer, PlayerInfo> getPlayers() {
        return players;
    }
}
