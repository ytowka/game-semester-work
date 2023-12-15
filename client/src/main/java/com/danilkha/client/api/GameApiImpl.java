package com.danilkha.client.api;

import org.danilkha.api.GameRoundApi;
import org.danilkha.connection.PackageReceiver;
import org.danilkha.utils.observable.ObservableValue;

public class GameApiImpl extends Api implements GameRoundApi {
    public GameApiImpl(PackageReceiver server) {
        super(server);
    }

    @Override
    public void moveTo(float x, float y) {

    }

    @Override
    public void shoot(float directionAngle) {

    }

    @Override
    public void hitWall(int id) {

    }

    @Override
    public void hitPlayer(int id) {

    }

    @Override
    public ObservableValue<String> subscribeGameEvents() {
        return null;
    }

    @Override
    public ObservableValue<Boolean> subscribeRoundRestart() {
        return null;
    }
}
