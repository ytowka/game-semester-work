package org.danilkha.middleware.utils;

import org.danilkha.connection.api.ClientPackageReceiver;
import org.danilkha.game.api.GameLobbyListener;
import org.danilkha.game.api.GameRound;

public abstract class GamePackageReceiver implements ClientPackageReceiver, GameLobbyListener, GameRound {
    @Override
    public void receiveData(int clientId, String data) {

    }
}
