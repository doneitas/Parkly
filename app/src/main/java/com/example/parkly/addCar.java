package com.example.parkly;

import android.os.Bundle;
import android.app.Activity;
import android.util.DisplayMetrics;

/**
 * Created by Marius on 2018-03-04.
 */

public class addCar extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.add_car);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width*.8), (int) (height*.6));
    }
}
