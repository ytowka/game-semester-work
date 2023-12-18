package org.danilkha.utils.coding;

import java.util.Base64;

public class EncodingUtil {
    public static String encodeShortArrayToString(int[] array) {
        byte[] byteArray = new byte[array.length ];
        for (int i = 0; i < array.length; i++) {
            byteArray[i ] = (byte) array[i];
        }
        return Base64.getEncoder().encodeToString(byteArray);
    }

    public static int[] decodeStringToShortArray(String encodedString) {
        byte[] byteArray = Base64.getDecoder().decode(encodedString);

        int[] intArray = new int[byteArray.length];
        for (int i = 0; i < intArray.length; i++) {
            intArray[i] = (byteArray[i] & 0xFF);
        }

        return intArray;
    }


    public static void main(String[] args) {

        int[] testArray = {10, 20, 30, 40, 50};
        String encodedString = encodeShortArrayToString(testArray);
        System.out.println("Encoded string: " + encodedString);
        int[] decoded = decodeStringToShortArray(encodedString);
        System.out.print("Decoded array: ");
        for (int value : decoded) {
            System.out.print(value + " ");
        }
    }

    public static String encodeIntArrayToString(int[] array) {
        byte[] byteArray = new byte[array.length * 4];
        for (int i = 0; i < array.length; i++) {
            byteArray[i * 4] = (byte) (array[i] >> 24);
            byteArray[i * 4 + 1] = (byte) (array[i] >> 16);
            byteArray[i * 4 + 2] = (byte) (array[i] >> 8);
            byteArray[i * 4 + 3] = (byte) array[i];
        }


        return  Base64.getEncoder().encodeToString(byteArray);
    }

    public static int[] decodeStringToIntArray(String encodedString) {
        byte[] byteArray = Base64.getDecoder().decode(encodedString);

        int[] intArray = new int[byteArray.length / 4];
        for (int i = 0; i < intArray.length; i++) {
            intArray[i] = (byteArray[i * 4] << 24) | ((byteArray[i * 4 + 1] & 0xFF) << 16) | ((byteArray[i * 4 + 2] & 0xFF) << 8) | (byteArray[i * 4 + 3] & 0xFF);
        }

        return intArray;
    }

}
