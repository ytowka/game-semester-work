package org.danilkha.game;

public class MapGenerator {

    public static boolean[][] generateMap(int size) {
        boolean[][] maze = new boolean[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                maze[i][j] = Math.random() < 0.6;
            }
        }
        clearBox(maze, 3, 0,0);
        clearBox(maze, 3, size-3,0);
        clearBox(maze, 3, 0,size-3);
        clearBox(maze, 3, size-3,size-3);
        return maze;
    }

    private static void clearBox(boolean[][] map, int size, int x, int y){
        for (int i = x; i < x + size; i++) {
            for (int j = y; j < y + size; j++) {
                map[i][j] = false;
            }
        }
    }
}
