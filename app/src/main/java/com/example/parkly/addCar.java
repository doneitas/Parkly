package com.example.parkly;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.parkly.DataBase.LicensePlate;

import static com.example.parkly.Cars.AddLicensePlate;

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


        Button btn_confirm = findViewById(R.id.btn_confirm);
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = txt_plate.getText().toString();
                if (text != null) {
                    LicensePlate licensePlate = new LicensePlate();
                    licensePlate.setNumber(text);
                    AddLicensePlate(licensePlate);
                    startActivity(new Intent(addCar.this, Cars.class));
                }
            }
        });
    }
}
