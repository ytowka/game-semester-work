package org.danilkha.api;

import org.danilkha.game.GameDto;
import org.danilkha.utils.observable.ObservableValue;

public interface GameRoundApi {


    String MOVE_TO = "game/move";
    String SHOOT = "game/shoot";
    String HIT_WALL = "game/hit_wall";
    String HIT_PLAYER = "game/hit_player";

    String SUBSCRIBE_GAME_EVENTS = "game/events";
    String SUBSCRIBE_ROUND = "game/round";

    void moveTo(float x, float y, float angle);
    void shoot(float x, float y, float directionAngle);
    void hitWall(int id);
    void hitPlayer(int index);
    ObservableValue<GameEvent[]> subscribeGameEvents();
}
