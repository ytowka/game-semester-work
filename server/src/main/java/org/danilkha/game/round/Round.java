package org.danilkha.game.round;

import org.danilkha.api.GameEvent;
import org.danilkha.config.GameConfig;
import org.danilkha.game.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Round {
    private int index;

    private Map<Integer, PlayerInfo> players;
    private final List<GameEvent> singleEvents;
    private PlayerInfo winner = null;

    public Round(List<PlayerInfo> players){
        singleEvents = new ArrayList<>();
        singleEvents.add(new GameEvent.StartRound(
                new int[players.size()],
                new boolean[GameConfig.MAP_SIZE][GameConfig.MAP_SIZE]
        ));
        this.players = new HashMap<>();
        for (PlayerInfo player : players) {
            this.players.put(player.getPlayer().getId(), player);
        }
    }

    public synchronized void moveTo(int clientId, float x, float y, float angle) {
        PlayerInfo playerInfo = players.get(clientId);
        playerInfo.setAngle(angle);
        playerInfo.setX(x);
        playerInfo.setY(y);
    }

    public synchronized void shoot(int clientId, float x, float y, float directionAngle) {
        singleEvents.add(new GameEvent.Shoot(
                players.get(clientId).getIndex(),
                x,
                y,
                directionAngle
        ));
    }

    public synchronized void hit(int fromId, int toIndex) {
        if(winner == null){
            for (PlayerInfo player : players.values()) {
                if(player.getIndex() == toIndex){
                    if(player.hit()){
                        singleEvents.add(new GameEvent.Destroy(player.getIndex()));
                    };
                    int alivePlayersCount = 0;
                    PlayerInfo lastAlivePlayer = null;
                    for (PlayerInfo value : players.values()) {
                        if(value.isAlive()){
                            alivePlayersCount += 1;
                            lastAlivePlayer = value;
                        }
                    }
                    if(alivePlayersCount == 1){
                        winner = lastAlivePlayer;
                        winner.addScore();
                        resetRound();
                    }
                }
            }
            singleEvents.add(new GameEvent.HitTank(
                    players.get(fromId).getIndex(), toIndex
            ));
        }
    }

    private void resetRound(){
        int[] scores = new int[players.size()];
        for (PlayerInfo value : players.values()) {
            value.reset();
            scores[value.getIndex()] = value.getScore();
        }

        singleEvents.clear();
        singleEvents.add(new GameEvent.StartRound(
                scores,
                new boolean[GameConfig.MAP_SIZE][GameConfig.MAP_SIZE]
        ));
    }

    public synchronized void resetSingleEvents(){
        singleEvents.clear();
    }
    public List<GameEvent> getSingleEvents(){
        return singleEvents;
    }

    public Map<Integer, PlayerInfo> getPlayers() {
        return players;
    }

    public PlayerInfo getWinner() {
        return winner;
    }
}
