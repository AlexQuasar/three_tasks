package com.alexquasar.threeTasks.firstTask;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BitParserTest {

    BitParser bitParser = new BitParser();

    @Test
    public void countBitsChangedTest() {
        int firstNumber = 123; // 123 = 1111011
        int secondNumber = 89; // 89 = 1011001

        int countBit;

        countBit = bitParser.countBitsChanged(firstNumber, secondNumber);
        assertEquals(2, countBit);

        firstNumber = 36885; // 36885 = 00001001000000010101
        secondNumber = 570804; // 570804 = 10001011010110110100

        countBit = bitParser.countBitsChanged(firstNumber, secondNumber);
        assertEquals(7, countBit);

        firstNumber = 100001; // 100001 = 11000011010100001
        secondNumber = 77; // 77 = 00000000001001101

        countBit = bitParser.countBitsChanged(firstNumber, secondNumber);
        assertEquals(9, countBit);
    }
}