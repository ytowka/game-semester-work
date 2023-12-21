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
            int[] data = new int[]{
                    playerIndex,
                    Float.floatToRawIntBits(x),
                    Float.floatToRawIntBits(y),
                    Float.floatToRawIntBits(angle)
            };
            return "SHOOT&%s".formatted(
                    EncodingUtil.encodeIntArrayToString(data)
            );
        }
    }

    record HitTank(
            int from,
            int to
    )implements GameEvent{
        @Override
        public String serialize() {
            int[] data = new int[]{
                    from,
                    to,
            };
            return "HITP&%s".formatted(
                    EncodingUtil.encodeIntArrayToString(data)
            );
        }
    }

    record HitWall(
            int wallCode
    )implements GameEvent{
        @Override
        public String serialize() {
            int[] data = new int[]{ wallCode };
            return "HITW&%s".formatted(
                    EncodingUtil.encodeIntArrayToString(data)
            );
        }
    }

    record Destroy(
            int playerIndex
    )implements GameEvent{
        @Override
        public String serialize() {
            int[] data = new int[]{playerIndex};
            return "DIE&%s".formatted(
                    EncodingUtil.encodeIntArrayToString(data)
            );
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
        if(rawData.startsWith("SHOOT")){
            String[] parts = rawData.split("&");
            int[] data = EncodingUtil.decodeStringToIntArray(parts[1]);
            return new Shoot(
                    data[0],
                    Float.intBitsToFloat(data[1]),
                    Float.intBitsToFloat(data[2]),
                    Float.intBitsToFloat(data[3])
            );
        }
        if(rawData.startsWith("HITP")){
            String[] parts = rawData.split("&");
            int[] data = EncodingUtil.decodeStringToIntArray(parts[1]);
            return new HitTank(
                    data[0],
                    data[1]
            );
        }
        if(rawData.startsWith("HITW")){
            String[] parts = rawData.split("&");
            int[] data = EncodingUtil.decodeStringToIntArray(parts[1]);
            return new HitWall(data[0]);
        }
        if(rawData.startsWith("DIE")){
            String[] parts = rawData.split("&");
            int[] data = EncodingUtil.decodeStringToIntArray(parts[1]);
            return new Destroy(data[0]);
        }
        return null;
    }
}
