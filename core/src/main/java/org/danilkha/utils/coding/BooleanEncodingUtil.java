package org.danilkha.utils.coding;

import java.util.Base64;

public class BooleanEncodingUtil {

    public static String encodeBooleanArrayToString(boolean[][] array) {
        int numRows = array.length;
        int numCols = array[0].length;
        int numBytes = numRows * ((numCols + 7) / 8);


        byte[] byteArray = new byte[numBytes];


        int byteIndex = 0;
        int bitIndex = 0;
        for (boolean[] booleans : array) {
            for (int j = 0; j < numCols; j++) {
                if (booleans[j]) {
                    byteArray[byteIndex] |= (byte) (1 << bitIndex);
                }
                bitIndex++;
                if (bitIndex == 8) {
                    bitIndex = 0;
                    byteIndex++;
                }
            }
        }


        return Base64.getEncoder().encodeToString(byteArray);
    }

    public static void decodeStringToBooleanArray(String encodedString, boolean[][] array) {

        byte[] byteArray = Base64.getDecoder().decode(encodedString);

        int numRows = array.length;
        int numCols = array[0].length;
        int byteIndex = 0;
        int bitIndex = 0;
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                array[i][j] = (byteArray[byteIndex] & (1 << bitIndex)) != 0;
                bitIndex++;
                if (bitIndex == 8) {
                    bitIndex = 0;
                    byteIndex++;
                }
            }
        }
    }

    public static void main(String[] args) {

        boolean[][] testArray = {{true, false, true}, {false, true, false}, {true, true, true}};
        String encoded = encodeBooleanArrayToString(testArray);
        System.out.println("Encoded string: " + encoded);


        boolean[][] decodedArray = new boolean[testArray.length][testArray[0].length];
        decodeStringToBooleanArray(encoded, decodedArray);

        System.out.println("Decoded array:");
        for (boolean[] row : decodedArray) {
            for (boolean value : row) {
                System.out.print(value+" ");
            }
            System.out.println();
        }
    }
}
