package com.danilkha.client.api;

import org.danilkha.api.GameEvent;
import org.danilkha.api.GameRoundApi;
import org.danilkha.connection.PackageReceiver;
import org.danilkha.utils.coding.EncodingUtil;
import org.danilkha.utils.observable.ObservableValue;

import java.util.Arrays;

public class GameApiImpl extends Api implements GameRoundApi {
    public GameApiImpl(PackageReceiver server) {
        super(server);
    }

    @Override
    public void moveTo(float x, float y, float angle) {
        drop(MOVE_TO, EncodingUtil.encodeIntArrayToString(new int[]{
                Float.floatToRawIntBits(x),
                Float.floatToRawIntBits(y),
                Float.floatToRawIntBits(angle),
        }));
    }

    @Override
    public void shoot(float x, float y, float directionAngle) {
        drop(SHOOT, EncodingUtil.encodeIntArrayToString(new int[]{
                Float.floatToRawIntBits(x),
                Float.floatToRawIntBits(y),
                Float.floatToRawIntBits(directionAngle),
        }));
    }

    @Override
    public void hitWall(int x, int y) {
        drop(HIT_WALL, EncodingUtil.encodeIntArrayToString(new int[]{x, y}));
    }

    @Override
    public void hitPlayer(int index) {
        drop(HIT_PLAYER, EncodingUtil.encodeIntArrayToString(new int[]{
               index,
        }));
    }

    @Override
    public ObservableValue<GameEvent[]> subscribeGameEvents() {
        return subscribe(SUBSCRIBE_GAME_EVENTS).map(raw -> {
            GameEvent[] gameEvents = new GameEvent[raw.length];
            for (int i = 0; i < gameEvents.length; i++) {
                gameEvents[i] = GameEvent.deserialize(raw[i]);
            }
            return gameEvents;
        });
    }

}
