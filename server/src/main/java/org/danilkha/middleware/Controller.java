package org.danilkha.middleware;

import org.danilkha.connection.Server;
import org.danilkha.game.Game;
import org.danilkha.game.Player;
import org.danilkha.middleware.utils.GamePackageReceiver;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Controller extends GamePackageReceiver {

    private final Game game;
    private final Server server;

    public Controller(Game game, Server server) {
        this.game = game;
        this.server = server;
    }

    @Override
    public void receiveData(int clientId, String data) {
        if(data.equals("getDate")){
            System.out.println("got data: "+data);
            server.receiveData(clientId, new SimpleDateFormat().format(new Date()));
        }
    }

    @Override
    public boolean createNewLobby(int clientId, String name, String playerName) {
        return game.createNewLobby(clientId, name, playerName);
    }

    @Override
    public boolean connectToLobby(int playerId, String name) {
        return game.connectToLobby(playerId, name);
    }

    @Override
    public void moveTo(int playerId, float x, float y) {
        Player player = game.getPlayerByClientId(playerId);
        player.getConnectedLobby().getCurrentRound().moveTo(playerId, x, y);
    }

    @Override
    public void shoot(int playerId, float directionAngle) {
        Player player = game.getPlayerByClientId(playerId);
        player.getConnectedLobby().getCurrentRound().shoot(playerId, directionAngle);
    }

    @Override
    public void hit(int fromId, int toId) {
        Player player = game.getPlayerByClientId(fromId);
        player.getConnectedLobby().getCurrentRound().hit(fromId, toId);
    }
}
