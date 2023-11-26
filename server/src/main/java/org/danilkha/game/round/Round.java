package org.danilkha.game.round;

import org.danilkha.game.Player;
import org.danilkha.game.api.GameRound;

import java.util.List;
import java.util.Map;

public class Round implements GameRound {
    private int index;

    private Map<Integer, PlayerInfo> players;

    public Round(List<Player> players){

    }

    @Override
    public void moveTo(int playerId, float x, float y) {

        PlayerInfo playerInfo = players.get(playerId);
    }

    @Override
    public void shoot(int playerId, float directionAngle) {

    }

    @Override
    public void hit(int fromId, int toId) {

    }
}
