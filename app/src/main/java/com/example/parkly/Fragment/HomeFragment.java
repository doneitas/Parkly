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
import java.util.Scanner;

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
    public ListView listZones;
    public ListView listTime;
    public int chosenMinutes = -1;
    public String chosenZone = "";
    public String finalPrice;
    public String parkingEnds;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_fragment, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        showPriceAndParkingEnding(view);
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

    private void showPriceAndParkingEnding(View view)
    {
        final ArrayList<String> zones = new ArrayList<String>();
        zones.add("Green");
        zones.add("Blue");
        zones.add("Red");
        zones.add("Yellow");
        zones.add("Orange");
        final ArrayList<String> time = new ArrayList<String>();


        listZones = (ListView)view.findViewById(R.id.list_zones);
        listTime = (ListView)view.findViewById(R.id.list_time);
        final ArrayAdapter<String> zonesAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_single_choice, zones);
        final ArrayAdapter<String> timeAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_single_choice, time);
        listZones.setAdapter(zonesAdapter);
        listTime.setAdapter(timeAdapter);


        TextView outputPrice = (TextView)view.findViewById(R.id.txt_outputPrice);
        tempPrice = outputPrice;
        TextView outputTime = (TextView)view.findViewById(R.id.txt_outputTime);
        tempTime = outputTime;


        listZones.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                finalPrice = "...";
                parkingEnds = "...";

                String previousZone = chosenZone;
                chosenZone = ((TextView) view).getText().toString();

                if(previousZone == chosenZone){
                    listZones.setItemChecked(listZones.getCheckedItemPosition(), false);
                    chosenZone = "";
                }

                listTime.clearChoices();


                switch (chosenZone){
                    case "Green":{
                        time.removeAll(time);
                        time.add("1  h  0 min");
                        time.add("2  h  0 min");
                        time.add("3  h  0 min");
                        time.add("4  h  0 min");
                        time.add("5  h  0 min");
                        time.add("6  h  0 min");
                        time.add("7  h  0 min");
                        time.add("8  h  0 min");
                        time.add("9  h  0 min");
                        time.add("10 h  0 min");
                        chosenMinutes = -1;
                        break;
                    }
                    case "Blue":{
                        time.removeAll(time);
                        time.add("0  h 30 min");
                        time.add("1  h  0 min");
                        time.add("1  h 30 min");
                        time.add("2  h  0 min");
                        time.add("3  h  0 min");
                        time.add("4  h  0 min");
                        time.add("5  h  0 min");
                        time.add("6  h  0 min");
                        time.add("7  h  0 min");
                        time.add("8  h  0 min");
                        time.add("9  h  0 min");
                        time.add("10 h  0 min");
                        chosenMinutes = -1;
                        break;
                    }
                    default:
                    {
                        time.removeAll(time);
                        time.add("0  h 15 min");
                        time.add("0  h 30 min");
                        time.add("0  h 45 min");
                        time.add("1  h  0 min");
                        time.add("1  h 15 min");
                        time.add("1  h 30 min");
                        time.add("2  h  0 min");
                        time.add("3  h  0 min");
                        time.add("4  h  0 min");
                        time.add("5  h  0 min");
                        time.add("6  h  0 min");
                        time.add("7  h  0 min");
                        time.add("8  h  0 min");
                        time.add("9  h  0 min");
                        time.add("10 h  0 min");
                        chosenMinutes = -1;
                        break;
                    }
                    case "": {
                        time.removeAll(time);
                    }
                }

                timeAdapter.notifyDataSetChanged();

                tempPrice.setText(finalPrice);
                tempTime.setText(parkingEnds);
            }
        });

        listTime.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                finalPrice = "...";
                parkingEnds = "...";

                String tempChosenMinutes = ((TextView) view).getText().toString();

                Scanner scan = new Scanner(tempChosenMinutes).useDelimiter("\\s+");
                int hour = scan.nextInt();
                scan.next();
                int minute = scan.nextInt();
                scan.close();

                int previousMinutes = chosenMinutes;
                chosenMinutes = (hour*60)+minute;

                if (previousMinutes == chosenMinutes){
                    listTime.setItemChecked(listTime.getCheckedItemPosition(), false);
                    chosenMinutes = -1;
                }
                else if (chosenZone != ""){
                    parkingEnds = estimatedTime(hour, minute);
                    finalPrice = estimatedPrice(chosenZone, hour, minute);
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

        String totalString = String.valueOf(total) + "0" + " \u20ac";
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

        String totalTime = (currentTime.get(Calendar.HOUR_OF_DAY) < 10? ("0"+currentTime.get(Calendar.HOUR_OF_DAY)) : currentTime.get(Calendar.HOUR_OF_DAY)) + ":" + (currentTime.get(Calendar.MINUTE) < 10? ("0"+currentTime.get(Calendar.MINUTE)) : currentTime.get(Calendar.MINUTE));

        return totalTime;
    }
}
