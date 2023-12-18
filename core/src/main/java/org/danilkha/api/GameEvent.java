package org.danilkha.api;

import org.danilkha.config.GameConfig;
import org.danilkha.utils.coding.BooleanEncodingUtil;
import org.danilkha.utils.coding.EncodingUtil;

public sealed interface GameEvent {

    String serialize();

    record StartRound(
            int[] score,
            boolean[][] walls
    ) implements GameEvent{
        @Override
        public String serialize() {
            return "SR&%s&%s".formatted(
                    EncodingUtil.encodeShortArrayToString(score),
                    BooleanEncodingUtil.encodeBooleanArrayToString(walls)
            );
        }
    }

    record PlayerMove(
            int playerIndex,
            float x,
            float y,
            float angle
    ) implements GameEvent{
        @Override
        public String serialize() {
            int[] data = new int[]{
                    playerIndex,
                    Float.floatToRawIntBits(x),
                    Float.floatToRawIntBits(y),
                    Float.floatToRawIntBits(angle)
            };
            return "PM&%s".formatted(
                EncodingUtil.encodeIntArrayToString(data)
            );
        }
    }

    record Shoot(
            int playerIndex,
            float x,
            float y,
            float angle
    )implements GameEvent{
        @Override
        public String serialize() {
            return null;
        }
    }

    record HitTank(
            int from,
            int to
    )implements GameEvent{
        @Override
        public String serialize() {
            return null;
        }
    }

    record HitWall(
            int wallCode
    )implements GameEvent{
        @Override
        public String serialize() {
            return null;
        }
    }

    static GameEvent deserialize(String rawData){
        if(rawData.startsWith("SR")){
            String[] parts = rawData.split("&");
            boolean[][] walls = new boolean[GameConfig.MAP_SIZE][GameConfig.MAP_SIZE];
            BooleanEncodingUtil.decodeStringToBooleanArray(parts[2], walls);
            return new StartRound(
                    EncodingUtil.decodeStringToShortArray(parts[1]),
                    walls
            );
        }
        if(rawData.startsWith("PM")){
            String[] parts = rawData.split("&");
            int[] data = EncodingUtil.decodeStringToIntArray(parts[1]);
            return new PlayerMove(
                    data[0],
                    Float.intBitsToFloat(data[1]),
                    Float.intBitsToFloat(data[2]),
                    Float.intBitsToFloat(data[3])
            );
        }
        return null;
    }
}
