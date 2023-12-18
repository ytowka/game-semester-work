package org.danilkha;

import org.danilkha.config.ServerConfig;
import org.danilkha.connection.Server;
import org.danilkha.game.Game;
import org.danilkha.middleware.LobbyController;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            Game game = new Game();
            Server server = new Server(ServerConfig.PORT, ServerConfig.TICK_RATE);
            LobbyController controller = new LobbyController(game, server);

            server.setListener(controller);
            server.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}