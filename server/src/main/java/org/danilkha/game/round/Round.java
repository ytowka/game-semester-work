package org.danilkha.game.round;

import org.danilkha.game.Player;
import java.util.List;
import java.util.Map;

public class Round {
    private int index;

    private Map<Integer, PlayerInfo> players;

    public Round(List<Player> players){

    }

    public void moveTo(int playerId, float x, float y) {

        PlayerInfo playerInfo = players.get(playerId);
    }

    public void shoot(int playerId, float directionAngle) {

    }

    public void hit(int fromId, int toId) {

    }
}
