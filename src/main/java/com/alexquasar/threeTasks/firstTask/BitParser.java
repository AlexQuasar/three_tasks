package com.alexquasar.threeTasks.firstTask;

// TODO: Напишите функцию, определяющую количество битов, которые необходимо изменить,
//  чтобы из целого числа А получить целое число B. Числа, допустим, 32-битные.

public class BitParser {

    public int countBitsChanged(int firstNumber, int secondNumber) {
        int difference = 0;

        char[] firstBinary = Integer.toBinaryString(firstNumber).toCharArray();
        char[] secondBinary = Integer.toBinaryString(secondNumber).toCharArray();

        int differenceLength;
        int firstLength = firstBinary.length;
        int secondLength = secondBinary.length;
        if (firstLength > secondLength) {
            differenceLength = firstLength - secondLength;
            secondBinary = addCharsZero(secondBinary, differenceLength);
        } else if (firstLength < secondLength) {
            differenceLength = secondLength - firstLength;
            firstBinary = addCharsZero(firstBinary, differenceLength);
        }

        for (int i = 0; i < firstBinary.length; i++) {
            char firstChar = firstBinary[i];
            char secondChar = secondBinary[i];
            if (firstChar != secondChar) {
                difference++;
            }
        }

        return difference;
    }

    private char[] addCharsZero(char[] binaryArray, int countZero) {
        char[] newBinaryArray = new char[countZero + binaryArray.length];

        for (int i = 0; i < countZero; i++) {
            newBinaryArray[i] = '0';
        }
        for (int i = 0; i < binaryArray.length; i++) {
            newBinaryArray[i + countZero] = binaryArray[i];
        }

        return newBinaryArray;
    }
}
