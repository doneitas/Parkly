package com.example.parkly.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.parkly.Activity.addCarActivity;
import com.example.parkly.DataBase.Tables.LicensePlate;
import com.example.parkly.DataBase.LicensePlateDatabase;
import com.example.parkly.DataBase.LicensePlateRepository;
import com.example.parkly.DataBase.LocalUserDataSource;
import com.example.parkly.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by donvel on 2018-03-12.
 */

public class HomeFragment extends Fragment {

    public TextView tempPrice;
    public TextView tempTime;
    public int chosenMinutes = 0;
    public String chosenZone = null;
    public String finalPrice = "";
    public String parkingEnds = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_fragment, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        createTimeZoneLists(view);
        checkCarRegistration();
    }
    //Database
    public LicensePlateRepository licensePlateRepository;

    private void checkCarRegistration() {
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        LicensePlateDatabase licensePlateDatabase = LicensePlateDatabase.getInstance(getActivity());
        licensePlateRepository = LicensePlateRepository.getInstance(LocalUserDataSource.getInstance(licensePlateDatabase.licensePlateDao()));
        final HomeFragment homeFragment = (HomeFragment) getFragmentManager().findFragmentByTag("HOME_FRAGMENT");

        final Disposable disposable = licensePlateRepository.getAll()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<List<LicensePlate>>() {
                    @Override
                    public void accept(List<LicensePlate> licensePlates) throws Exception {
                        if (licensePlates.isEmpty() && homeFragment != null && homeFragment.isVisible())
                            startActivity(new Intent(getActivity(), addCarActivity.class));
                    }
                });
        compositeDisposable.add(disposable);
    }

    private void createTimeZoneLists(View view)
    {
        final ListView listZones;
        ListView listTime;
        ArrayList<String> zones = new ArrayList<String>();
        zones.add("Green");
        zones.add("Blue");
        zones.add("Red");
        zones.add("Yellow");
        zones.add("Orange");
        ArrayList<Integer> time = new ArrayList<Integer>();
        time.add(15);
        time.add(30);
        time.add(45);
        time.add(60);
        time.add(75);
        time.add(90);
        time.add(120);
        time.add(180);
        time.add(240);
        time.add(300);
        time.add(360);
        time.add(420);
        time.add(480);
        time.add(540);
        time.add(600);


        listZones = (ListView)view.findViewById(R.id.list_zones);
        listTime = (ListView)view.findViewById(R.id.list_time);
        ArrayAdapter<String> zonesAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, zones);
        ArrayAdapter<Integer> timeAdapter = new ArrayAdapter<Integer>(getActivity(), android.R.layout.simple_list_item_1, time);
        listZones.setAdapter(zonesAdapter);
        listTime.setAdapter(timeAdapter);


        TextView outputPrice = (TextView)view.findViewById(R.id.txt_outputPrice);
        tempPrice = outputPrice;
        TextView outputTime = (TextView)view.findViewById(R.id.txt_outputTime);
        tempTime = outputTime;


        listZones.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                chosenZone = ((TextView) view).getText().toString();
                if (chosenMinutes != 0){
                    parkingEnds = estimatedTime(chosenMinutes/60, chosenMinutes%60);
                    finalPrice = estimatedPrice(chosenZone, chosenMinutes/60, chosenMinutes%60);
                }
                tempPrice.setText(finalPrice);
                tempTime.setText(parkingEnds);
            }
        });

        listTime.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String tempChosenZone = ((TextView) view).getText().toString();
                chosenMinutes = Integer.parseInt(tempChosenZone);
                if (chosenZone != null){
                    parkingEnds = estimatedTime(chosenMinutes/60, chosenMinutes%60);
                    finalPrice = estimatedPrice(chosenZone, chosenMinutes/60, chosenMinutes%60);
                }
                tempPrice.setText(finalPrice);
                tempTime.setText(parkingEnds);
            }
        });


    }

    private String estimatedPrice(String color, int chosenHour, int chosenMinute)
    {
        double total;
        double price = 0;
        switch(color)
        {
            case "Orange":
            {
                price = 2 / 60d;
                total = ((chosenHour*60) + chosenMinute) * price;
                break;
            }
            case "Yellow":
            {
                price = 2 / 60d;
                total = ((chosenHour*60) + chosenMinute) * price;
                break;
            }
            case "Blue":
            {
                price = 0.6 / 60d;
                total = ((chosenHour*60) + chosenMinute) * price;
                break;
            }
            case "Red":
            {
                price = 1.2 / 60d;
                total = ((chosenHour*60) + chosenMinute) * price;
                break;
            }
            case "Green":
            {
                price = 0.3 / 60d;
                total = ((chosenHour*60) + chosenMinute) * price;
                break;
            }
            default:
            {
                total = 0;
                break;
            }
        }

        String totalString = String.valueOf(total);
        return totalString;
    }

    private String estimatedTime(int chosenHour, int chosenMinute)
    {
        //numatoma parkavimosi pabaiga
        Calendar currentTime = GregorianCalendar.getInstance();
        //nustatomas esamas laikas
        currentTime.setTime(new Date());
        currentTime.add(Calendar.HOUR_OF_DAY, chosenHour);
        currentTime.add(Calendar.MINUTE, chosenMinute);

        String totalTime = currentTime.get(Calendar.HOUR_OF_DAY) + " " + currentTime.get(Calendar.MINUTE);

        return totalTime;
    }
}
