package com.example.parkly;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.*;
import android.view.View;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static ArrayList<String> arrayList;
    public static ArrayAdapter<String> adapter;

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
        declaration();
        init();
        //checkCarRegistration();
    }

    public void declaration(){
        arrayList = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(MainActivity.this,  android.R.layout.simple_list_item_1, arrayList);
    }

    /*public void checkCarRegistration(){
        if(arrayList.size() == 0){
            startActivity(new Intent(MainActivity.this, addCar.class));
        }
    }*/
}
