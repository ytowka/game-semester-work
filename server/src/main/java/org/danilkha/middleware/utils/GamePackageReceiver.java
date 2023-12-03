package org.danilkha.middleware.utils;

import org.danilkha.api.LobbyApi;
import org.danilkha.connection.api.ClientPackageReceiver;
import org.danilkha.game.api.GameLobbyListener;
import org.danilkha.game.api.GameRound;
import org.danilkha.middleware.RouterController;

public abstract class GamePackageReceiver extends RouterController implements GameLobbyListener, GameRound {

    public GamePackageReceiver(){
        addRoute(LobbyApi.START_GAME, request -> {

        });


    }
}
