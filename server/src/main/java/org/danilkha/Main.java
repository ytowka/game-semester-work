package org.danilkha;

import org.danilkha.connection.Server;
import org.danilkha.game.Game;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            Game game = new Game();
            Server server = new Server(6060, game, 64);
            server.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}