package com.example.parkly;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Cars extends AppCompatActivity {

    ListView listView;
    ArrayList<String> arrayList;
    ArrayAdapter<String> adapter;
    Button btn_home;
    Button btn_add;

    public void init(){
        Button btn_home = (Button)findViewById(R.id.btn_home);
        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Cars.this, MainActivity.class));
            }
        });
        Button btn_add = (Button)findViewById(R.id.btn_add);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Cars.this, addCar.class));
            }
        });
    }

    public void readLicencePlates(){
        listView =(ListView) findViewById(R.id.lst_Cars);
        arrayList = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(Cars.this, android.R.layout.simple_list_item_1, arrayList);
        listView.setAdapter(adapter);

        try {
            String item;
            FileInputStream fileInputStream = openFileInput("LicensePlateNumbers.txt");
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            while((item = bufferedReader.readLine()) != null){
                arrayList.add(item);
            }
            adapter.notifyDataSetChanged();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cars);
        init();
        readLicencePlates();
    }
}
