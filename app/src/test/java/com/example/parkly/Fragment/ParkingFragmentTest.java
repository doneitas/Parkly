package com.example.parkly.Fragment;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Karolis on 5/7/2018.
 */

public class ParkingFragmentTest {

    @Test
    public void trimTest() throws Exception {

        String input1 = "   Testing Space";
        String expected = "Testing Space";
        String output;

        ParkingFragment parkingFragment = new ParkingFragment();
        output = parkingFragment.trimText(input1);

        assertEquals(expected, output);

        //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

        input1 = "Testing Space          ";

        output = parkingFragment.trimText(input1);

        assertEquals(expected, output);

        //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

        input1 = "Testing    Space";

        output = parkingFragment.trimText(input1);

        assertEquals(expected, output);

        //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

        input1 = "       Testing    Space";

        output = parkingFragment.trimText(input1);

        assertEquals(expected, output);

        //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

        input1 = "Testing    Space            ";

        output = parkingFragment.trimText(input1);

        assertEquals(expected, output);

        //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    }
}
