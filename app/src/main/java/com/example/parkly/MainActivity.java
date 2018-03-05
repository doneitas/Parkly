package com.example.parkly;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.*;
import android.view.View;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button btn_Cars;

    public void init() {
        Button btn_Cars = (Button) findViewById(R.id.btn_cars);
        btn_Cars.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Cars.class));
            }
        });
    }


    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        checkCarRegistration();
    }

    public void checkCarRegistration(){
        if (!getFileStreamPath("LicensePlateNumbers.txt").exists()){
            startActivity(new Intent(MainActivity.this, addCar.class));
        }
    }
}
