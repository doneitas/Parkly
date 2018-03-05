package com.example.parkly;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Marius on 2018-03-04.
 */

public class addCar extends Activity {

    public static EditText et;
    Button btn_confirm;

    public static boolean isNumberCorrect(String number) {
        String expression = "[a-zA-Z]{3}+[0-9]{3}";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(number);
        return matcher.matches();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.add_car);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width*.8), (int) (height*.6));

        init();


    }

    public void init(){
        btn_confirm = (Button) findViewById(R.id.btn_confirm);
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et = (EditText) findViewById(R.id.txtb_plate);
                String value = et.getText().toString();
                if(isNumberCorrect(value)) {
                    try {
                        FileOutputStream fileOutputStream = openFileOutput("LicensePlateNumbers.txt", MODE_APPEND);
                        fileOutputStream.write(value.getBytes());
                        fileOutputStream.write("\n".getBytes());
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    startActivity(new Intent(addCar.this, Cars.class));
                    et.setText(null);
                }
                else {
                    Toast.makeText(getApplicationContext(), "Wrong format", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
