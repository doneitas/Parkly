package com.example.parkly.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatSpinner;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.parkly.Activity.MainActivity;
import com.example.parkly.Activity.addCarActivity;
import com.example.parkly.DataBase.LicensePlateAdapter;
import com.example.parkly.DataBase.Tables.LicensePlate;
import com.example.parkly.DataBase.LicensePlateDatabase;
import com.example.parkly.DataBase.LicensePlateRepository;
import com.example.parkly.DataBase.LocalUserDataSource;
import com.example.parkly.DataBase.Tables.LicensePlate;
import com.example.parkly.R;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.Buffer;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Observable;
import java.util.Scanner;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
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
    public String defaultNumber;
    public List<LicensePlate> tempLicensePlate;
    public boolean isDefaultSelected;
    private long timeLeftInMilliseconds;
    private TextView remaining;
    private TextView timeLeft;
    private TextView ends;
    private TextView timeEnds;
    private TextView confirm;

    //Adapter
    private Spinner spin_DefaultCar;
    List<String> licensePlateList = new ArrayList<String>();
    ArrayAdapter<String> adapter;

    //Database
    private CompositeDisposable compositeDisposable;
    private LicensePlateRepository licensePlateRepository;
    LicensePlateDatabase licensePlateDatabase;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_fragment, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init(view);
        database(view);
        loadData();
        showPriceAndParkingEnding(view);
        checkCarRegistration();
        confirmParking(view);
    }

    public void init(View view){
        compositeDisposable = new CompositeDisposable();
        licensePlateDatabase = LicensePlateDatabase.getInstance(getActivity());
        licensePlateRepository = LicensePlateRepository.getInstance(LocalUserDataSource.getInstance(licensePlateDatabase.licensePlateDao()));

        confirm = view.findViewById(R.id.btn_homeConfirm);
    }

    public void disableEnableConfirm(){
        if (tempPrice.getText().toString().compareTo("-") == 0 || !isDefaultSelected){
            confirm.setClickable(false);
            confirm.setEnabled(false);
        }
        else{
            confirm.setClickable(true);
            confirm.setEnabled(true);
        }
    }

    private void checkCarRegistration() {
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

    public void showPriceAndParkingEnding(View view)
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

        disableEnableConfirm();

        listZones.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                finalPrice = "-";
                parkingEnds = "-";

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

                disableEnableConfirm();
            }
        });

        listTime.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                finalPrice = "-";
                parkingEnds = "-";

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

                disableEnableConfirm();
            }
        });


    }

    public String estimatedPrice(String color, int chosenHour, int chosenMinute)
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

    public String estimatedTime(int chosenHour, int chosenMinute)
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

    public void needsPopUp (String color)
    {
        Calendar currentTime = GregorianCalendar.getInstance();
        currentTime.setTime(new Date());
        switch(color)
        {
            case "Orange":
            {
                if (currentTime.get(Calendar.HOUR_OF_DAY) >= 24 || currentTime.get(Calendar.HOUR_OF_DAY) < 8)
                {
                    Toast.makeText(getActivity(), "Jūsų pasirinktoje vietoje šiuo metu parkavimas yra nemokas!", Toast.LENGTH_LONG).show();
                }
                break;
            }
            case "Green":
            case "Blue":
            case "Red":
            case "Yellow":
            {
                if (currentTime.get(Calendar.HOUR_OF_DAY) >= 18 || currentTime.get(Calendar.HOUR_OF_DAY) <= 8)
                {
                    Toast.makeText(getActivity(), "Jūsų pasirinktoje vietoje šiuo metu parkavimas yra nemokas!", Toast.LENGTH_LONG).show();
                }
                break;
            }
            default:
            {
                break;
            }
        }
    }


    //Everything for database --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    public void database(View view){

        //init View
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, licensePlateList);
        spin_DefaultCar = (Spinner) view.findViewById(R.id.spin_DefaultCar);
        registerForContextMenu(spin_DefaultCar);
        spin_DefaultCar.setAdapter(adapter);
    }

    private void loadData()
    {

        Disposable disposable = licensePlateRepository.getAll()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<List<LicensePlate>>() {
                    @Override
                    public void accept(List<LicensePlate> licensePlates) throws Exception {
                        onGetAllLicensePlateSuccess(licensePlates);
                        tempLicensePlate = licensePlates;
                    }

                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(getActivity(), ""+throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        compositeDisposable.add(disposable);

        getAndSetDefault();

        spin_DefaultCar.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                for(int j=0; j < tempLicensePlate.size(); j++){
                    if(tempLicensePlate.get(j).getNumber().compareTo(spin_DefaultCar.getSelectedItem().toString()) == 0){
                        setDefault(tempLicensePlate.get(j));
                        disableEnableConfirm();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                //do nothing
            }

        });
    }

    private void getAndSetDefault(){
        Disposable disposable = io.reactivex.Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> e) throws Exception {

                defaultNumber = licensePlateRepository.findDefault().getNumber();
                isDefaultSelected = licensePlateRepository.findDefault().getCurrent();

                spin_DefaultCar.post(new Runnable() {
                    @Override
                    public void run() {

                        spin_DefaultCar.clearFocus();

                        for (int i=0; i < licensePlateList.size(); i++){
                            if (defaultNumber.compareTo(licensePlateList.get(i)) == 0){
                                spin_DefaultCar.setSelection(i);
                            }
                        }
                    }

                });

                adapter.notifyDataSetChanged();

                e.onComplete();
            }
        })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer() {
                    @Override
                    public void accept(Object o) throws Exception {}
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                    }
                });
        compositeDisposable.add(disposable);
    }

    public void onGetAllLicensePlateSuccess(List<LicensePlate> licensePlates)
    {
        licensePlateList.clear();

        if(!isDefaultSelected){
            licensePlateList.add("Not selected");
        }

        for (int i = 0; i < licensePlates.size(); i++) {
            licensePlateList.add(licensePlates.get(i).getNumber());
        }

        if (licensePlateList.size() == 2){
            licensePlateList.remove("Not selected");
        }

        adapter.notifyDataSetChanged();
    }

    private void setDefault(final LicensePlate licensePlate) {
        Disposable disposable = io.reactivex.Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> e) throws Exception {
                LicensePlate oldLicensePlate = licensePlateRepository.findDefault();
                if (oldLicensePlate != null)
                {
                    oldLicensePlate.setCurrent(false);
                    licensePlateRepository.updateLicensePlate(oldLicensePlate);
                }
                licensePlate.setCurrent(true);
                licensePlateRepository.updateLicensePlate(licensePlate);
                isDefaultSelected = licensePlateRepository.findDefault().getCurrent();
                e.onComplete();
            }
        })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer() {
                    @Override
                    public void accept(Object o) throws Exception {}
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });
        compositeDisposable.add(disposable);
    }



    //--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    private void confirmParking(View view){

        remaining = getActivity().findViewById(R.id.remaining);
        timeLeft = getActivity().findViewById(R.id.remainingText);
        ends = getActivity().findViewById(R.id.ends);
        timeEnds = getActivity().findViewById(R.id.endsText);

        File file = getContext().getFileStreamPath("Countdown");

        if (file.exists()) {
            if(!MainActivity.isTimerCreated) {
                try {
                    FileInputStream fileInputStream = getActivity().openFileInput("Countdown");
                    InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    int parkingEndsMinutes = Integer.parseInt(bufferedReader.readLine());

                    startParking(parkingEndsMinutes);

                    if(MainActivity.isTimerCreated) {

                        String timeEndsText;

                        if ((parkingEndsMinutes / 60) < 10) timeEndsText = "0";
                        timeEndsText = "" + parkingEndsMinutes / 60;
                        timeEndsText += ":";
                        if ((parkingEndsMinutes % 60) < 10) timeEndsText += "0";
                        timeEndsText += parkingEndsMinutes % 60;

                        timeEnds.setText(timeEndsText);
                    }
                    else{
                        remaining.setVisibility(View.INVISIBLE);
                        timeLeft.setVisibility(View.INVISIBLE);
                        ends.setVisibility(View.INVISIBLE);
                        timeEnds.setVisibility(View.INVISIBLE);
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        else {
            remaining.setVisibility(View.INVISIBLE);
            timeLeft.setVisibility(View.INVISIBLE);
            ends.setVisibility(View.INVISIBLE);
            timeEnds.setVisibility(View.INVISIBLE);
        }

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                remaining.setVisibility(View.VISIBLE);
                timeLeft.setVisibility(View.VISIBLE);
                ends.setVisibility(View.VISIBLE);
                timeEnds.setVisibility(View.VISIBLE);

                File file = getContext().getFileStreamPath("Countdown");

                if (file.exists()) {
                    file.delete();
                    MainActivity.countDownTimer.cancel();
                }

                Scanner scan = new Scanner(tempTime.getText().toString()).useDelimiter(":");

                int parkingEndsMinutes = scan.nextInt() * 60 + scan.nextInt() % 60;

                startParking(parkingEndsMinutes);

                String fileName = "Countdown";

                try {
                    FileOutputStream fileOutputStream = getActivity().openFileOutput(fileName, Context.MODE_APPEND);
                    fileOutputStream.write(String.valueOf(parkingEndsMinutes).getBytes());
                    fileOutputStream.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                timeEnds.setText(tempTime.getText().toString());
            }
        });
    }

    public void startParking(int parkingEndsMinutes){

        Calendar currentTime = GregorianCalendar.getInstance();

        currentTime.setTime(new Date());

        timeLeftInMilliseconds = (parkingEndsMinutes - (currentTime.get(Calendar.HOUR_OF_DAY) * 60 + currentTime.get(Calendar.MINUTE))) * 60000;

        if(timeLeftInMilliseconds <= 0){
            timeLeftInMilliseconds = -1;
            return;
        }

        MainActivity.isTimerCreated = true;

        MainActivity.countDownTimer = new CountDownTimer(timeLeftInMilliseconds, 1000) {
            @Override
            public void onTick(long l) {
                timeLeftInMilliseconds = l;
                updateTimer(timeLeftInMilliseconds);
            }

            @Override
            public void onFinish() {
                File file = getContext().getFileStreamPath("Countdown");
                file.delete();
                remaining.setVisibility(View.INVISIBLE);
                timeLeft.setVisibility(View.INVISIBLE);
                ends.setVisibility(View.INVISIBLE);
                timeEnds.setVisibility(View.INVISIBLE);
            }
        }.start();
    }

    public void updateTimer(long timeLeftInMilliseconds){

        int timeLeftInMinutes = (int) timeLeftInMilliseconds / 60000;

        int hours = (int) timeLeftInMinutes / 60;
        int minutes = (int) timeLeftInMinutes % 60;
        int seconds = (int) timeLeftInMilliseconds % 60000 / 1000;

        String timeLeftText;

        timeLeftText = "" + hours;
        timeLeftText += ":";
        if(minutes < 10) timeLeftText += "0";
        timeLeftText += minutes;
        timeLeftText += ":";
        if(seconds < 10) timeLeftText += "0";
        timeLeftText += seconds;

        timeLeft.setText(timeLeftText);
    }
}
