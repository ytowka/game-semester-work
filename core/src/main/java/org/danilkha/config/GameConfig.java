package org.danilkha.config;

public class GameConfig {
    public static final int PLAYER_HP = 3;
    public static final int DEFAULT_DAMAGE = 1;
    public static final int MAP_SIZE = 17;
    public static final int WALL_HP = 3;

    public static final float TANK_SIZE = 0.75f; // relative to map block
    public static final float MISSILE_SIZE = 0.75f; // relative to map block

    public static final float TANK_MOVE_SPEED = 80f; // px per sec
    public static final float MISSILE_SPEED = 260f; // px per sec
    public static final float GUN_ROTATE_SPEED = 150f; // degree per sec
    public static final int RELOAD_PERIOD = 500; // ms

    public static final float[][] PLAYER_START_POSITIONS = new float[][]{
            {1f ,1f}, {MAP_SIZE - 1f - TANK_SIZE, 1f},
            {1f ,MAP_SIZE - 1f - TANK_SIZE}, {MAP_SIZE - 1f - TANK_SIZE, MAP_SIZE - 1f - TANK_SIZE},
    };


}
