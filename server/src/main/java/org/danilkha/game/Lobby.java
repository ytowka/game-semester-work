package org.danilkha.game;

import java.util.ArrayList;
import java.util.List;

public class Lobby {

    private final String name;
    private final int hostUserId;

    private final List<Integer> memberUserIds;

    public Lobby(String name, int hostUserId) {
        this.name = name;
        this.hostUserId = hostUserId;
        memberUserIds = new ArrayList<>();
        memberUserIds.add(hostUserId);
    }

    public boolean addUser(int userId){
        if (memberUserIds.size() >= 4){
            return false;
        }
        memberUserIds.add(userId);
        return true;
    }
}
