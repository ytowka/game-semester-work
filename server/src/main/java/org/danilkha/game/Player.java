package org.danilkha.game;

public class Player {
    private final String name;
    private final int id;
    private int score;

    private Lobby connectedLobby = null;

    public Player(String name, int id) {
        this.name = name;
        this.id = id;
        score = 0;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public Lobby getConnectedLobby() {
        return connectedLobby;
    }

    public void setConnectedLobby(Lobby connectedLobby) {
        this.connectedLobby = connectedLobby;
    }
}
