package com.example.parkly.Fragment;

import org.junit.Test;
import org.mockito.Mockito;

import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

/**
 * Created by donvel on 2018-03-20.
 */
public class HomeFragmentTest {
    @Test
    public void estimatedPrice() throws Exception {
        String input1 = "Red";
        int input2 = 1;
        int input3 = 15;
        String output;
        String expected = "1.50" + " \u20ac";

        HomeFragment homeFragment = new HomeFragment();
        output = homeFragment.estimatedPrice(input1, input2, input3);

        assertEquals(expected, output);

        //---------------------------------------------------------------------------

        input1 = "Blue";
        input2 = 0;
        input3 = 30;
        expected = "0.30" + " \u20ac";

        output = homeFragment.estimatedPrice(input1, input2, input3);

        assertEquals(expected, output);

        //---------------------------------------------------------------------------

        input1 = "Yellow";
        input2 = 10;
        input3 = 0;
        expected = "20.00" + " \u20ac";

        output = homeFragment.estimatedPrice(input1, input2, input3);

        assertEquals(expected, output);

        //---------------------------------------------------------------------------

        input1 = "Orange";
        input2 = 0;
        input3 = 30;
        expected = "1.00" + " \u20ac";

        output = homeFragment.estimatedPrice(input1, input2, input3);

        assertEquals(expected, output);

        //---------------------------------------------------------------------------

        input1 = "Green";
        input2 = 3;
        input3 = 0;
        expected = "0.90" + " \u20ac";

        output = homeFragment.estimatedPrice(input1, input2, input3);

        assertEquals(expected, output);

        //---------------------------------------------------------------------------

        input1 = "Blue";
        input2 = 6;
        input3 = 0;
        expected = "3.60" + " \u20ac";

        output = homeFragment.estimatedPrice(input1, input2, input3);

        assertEquals(expected, output);

        //---------------------------------------------------------------------------

        input1 = "Orange";
        input2 = 0;
        input3 = 45;
        expected = "1.50" + " \u20ac";

        output = homeFragment.estimatedPrice(input1, input2, input3);

        assertEquals(expected, output);

        //---------------------------------------------------------------------------

        input1 = "Red";
        input2 = 5;
        input3 = 0;
        expected = "6.00" + " \u20ac";

        output = homeFragment.estimatedPrice(input1, input2, input3);

        assertEquals(expected, output);

        //---------------------------------------------------------------------------

        input1 = "Yellow";
        input2 = 3;
        input3 = 0;
        expected = "6.00" + " \u20ac";

        output = homeFragment.estimatedPrice(input1, input2, input3);

        assertEquals(expected, output);

        //---------------------------------------------------------------------------

        input1 = "Green";
        input2 = 10;
        input3 = 0;
        expected = "3.00" + " \u20ac";

        output = homeFragment.estimatedPrice(input1, input2, input3);

        assertEquals(expected, output);

        //---------------------------------------------------------------------------

    }

    @Test
    public void calculateTimeLeft() throws Exception {

        int input1 = 15 * 60 + 15;
        int input2 = 15;
        int input3 = 00;
        long output;
        long expected = 15 * 60000;

        HomeFragment homeFragment = new HomeFragment();
        //
        output = homeFragment.calculateTimeLeft(input1, input2, input3);

        assertEquals(expected, output);

        //---------------------------------------------------------------------------

        input1 = (17) * 60 + (38);
        input2 = 12;
        input3 = 15;
        expected = ((5) * 60 + (23)) * 60000;

        homeFragment = new HomeFragment();
        output = homeFragment.calculateTimeLeft(input1, input2, input3);

        assertEquals(expected, output);

        //---------------------------------------------------------------------------

        input1 = (12) * 60 + (00);
        input2 = 9;
        input3 = 49;
        expected = ((2) * 60 + (11)) * 60000;

        homeFragment = new HomeFragment();
        output = homeFragment.calculateTimeLeft(input1, input2, input3);

        assertEquals(expected, output);

        //---------------------------------------------------------------------------

        input1 = (9) * 60 + (45);
        input2 = 8;
        input3 = 30;
        expected = ((1) * 60 + (15)) * 60000;

        homeFragment = new HomeFragment();
        output = homeFragment.calculateTimeLeft(input1, input2, input3);

        assertEquals(expected, output);

        //---------------------------------------------------------------------------

        input1 = (18) * 60 + (59);
        input2 = 9;
        input3 = 0;
        expected = ((9) * 60 + (59)) * 60000;

        homeFragment = new HomeFragment();
        output = homeFragment.calculateTimeLeft(input1, input2, input3);

        assertEquals(expected, output);

        //---------------------------------------------------------------------------

        input1 = (21) * 60 + (34);
        input2 = 17;
        input3 = 15;
        expected = ((4) * 60 + (19)) * 60000;

        homeFragment = new HomeFragment();
        output = homeFragment.calculateTimeLeft(input1, input2, input3);

        assertEquals(expected, output);

        //---------------------------------------------------------------------------

        input1 = (16) * 60 + (02);
        input2 = 15;
        input3 = 43;
        expected = ((0) * 60 + (19)) * 60000;

        homeFragment = new HomeFragment();
        output = homeFragment.calculateTimeLeft(input1, input2, input3);

        assertEquals(expected, output);

        //---------------------------------------------------------------------------

        input1 = (12) * 60 + (00);
        input2 = 9;
        input3 = 0;
        expected = ((3) * 60 + (0)) * 60000;

        homeFragment = new HomeFragment();
        output = homeFragment.calculateTimeLeft(input1, input2, input3);

        assertEquals(expected, output);

        //---------------------------------------------------------------------------

        input1 = (8) * 60 + (30);
        input2 = 8;
        input3 = 3;
        expected = ((0) * 60 + (27)) * 60000;

        homeFragment = new HomeFragment();
        output = homeFragment.calculateTimeLeft(input1, input2, input3);

        assertEquals(expected, output);

        //---------------------------------------------------------------------------

        input1 = (13) * 60 + (28);
        input2 = 10;
        input3 = 33;
        expected = ((2) * 60 + (55)) * 60000;

        homeFragment = new HomeFragment();
        output = homeFragment.calculateTimeLeft(input1, input2, input3);

        assertEquals(expected, output);

        //---------------------------------------------------------------------------

    }

    @Test
    public void needsPopUp() throws Exception {

        //----------------TESTING WITH DAYS----------------------------------------
        HomeFragment service = Mockito.mock(HomeFragment.class);
        Calendar currentTime = GregorianCalendar.getInstance();
        currentTime.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        currentTime.set(Calendar.HOUR_OF_DAY, 15);
        String input1 = "Orange 2€/h";
        boolean output;

        HomeFragment homeFragment = new HomeFragment();
        when(service.needsPopUp(input1, currentTime)).thenReturn(false);



        //---------------------------------------------------------------------------

        input1 = "Green 0.3€/h";
        currentTime.add(Calendar.DAY_OF_WEEK, 1);


        when(service.needsPopUp(input1, currentTime)).thenReturn(false);



        //---------------------------------------------------------------------------

        input1 = "Blue 0.6€/h";
        currentTime.add(Calendar.DAY_OF_WEEK, 1);

        when(service.needsPopUp(input1, currentTime)).thenReturn(false);



        //---------------------------------------------------------------------------

        input1 = "Red 1.2€/h";
        currentTime.add(Calendar.DAY_OF_WEEK, 1);

        when(service.needsPopUp(input1, currentTime)).thenReturn(false);



        //---------------------------------------------------------------------------

        input1 = "Yellow 2€/h";
        currentTime.add(Calendar.DAY_OF_WEEK, 1);

        when(service.needsPopUp(input1, currentTime)).thenReturn(false);



        //---------------------------------------------------------------------------

        currentTime.add(Calendar.DAY_OF_WEEK, 1);

        when(service.needsPopUp(input1, currentTime)).thenReturn(false);



        //---------------------------------------------------------------------------

        currentTime.add(Calendar.DAY_OF_WEEK, 1);

        when(service.needsPopUp(input1, currentTime)).thenReturn(true);



        //---------------------------------------------------------------------------

        //----------------TESTING WITH DAYS----------------------------------------

        //---------------------------------------------------------------------------

        currentTime.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
        currentTime.set(Calendar.HOUR_OF_DAY, 5);
        input1 = "Yellow 2€/h";

        when(service.needsPopUp(input1, currentTime)).thenReturn(true);



        //---------------------------------------------------------------------------

        currentTime.set(Calendar.HOUR_OF_DAY, 15);

        when(service.needsPopUp(input1, currentTime)).thenReturn(false);



        //---------------------------------------------------------------------------

        input1 = "Orange 2€/h";
        currentTime.set(Calendar.HOUR_OF_DAY, 5);

        when(service.needsPopUp(input1, currentTime)).thenReturn(true);



        //---------------------------------------------------------------------------

        currentTime.set(Calendar.HOUR_OF_DAY, 15);

        when(service.needsPopUp(input1, currentTime)).thenReturn(false);


        //---------------------------------------------------------------------------

    }
}