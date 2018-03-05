package com.example.parkly;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Marius on 2018-03-04.
 */

public class addCar extends Activity {
    EditText txt_plate;
    Button btn_confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.add_car);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * .8), (int) (height * .6));


        btn_confirm = (Button) findViewById(R.id.btn_confirm);
        txt_plate = (EditText) findViewById(R.id.txt_plate);

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String value = txt_plate.getText().toString();
                Intent myIntent = new Intent(getBaseContext(),Cars.class);
                myIntent.putExtra("number",value);
                startActivity(myIntent);
            }
        });
    }
}
