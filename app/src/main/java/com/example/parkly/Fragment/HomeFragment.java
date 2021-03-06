package com.example.parkly.Fragment;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.telephony.SmsManager;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.parkly.Activity.MainActivity;
import com.example.parkly.Activity.addCarActivity;
import com.example.parkly.DataBase.LicensePlateDatabase;
import com.example.parkly.DataBase.LicensePlateRepository;
import com.example.parkly.DataBase.LocalUserDataSource;
import com.example.parkly.DataBase.Tables.LicensePlate;
import com.example.parkly.Notifications.NotificationReceiver_First;
import com.example.parkly.Notifications_lt.NotificationReceiver_First_Lt;
import com.example.parkly.Notifications.NotificationReceiver_Second;
import com.example.parkly.Notifications_lt.NotificationReceiver_Second_Lt;
import com.example.parkly.Notifications.NotificationReceiver_Third;
import com.example.parkly.Notifications_lt.NotificationReceiver_Third_Lt;
import com.example.parkly.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Scanner;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static android.support.v4.content.ContextCompat.checkSelfPermission;

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
    public String chosenDefaultNumber = "";
    public String currentDefaultNumber = "";
    public List<LicensePlate> tempLicensePlate;
    public boolean isDefaultSelected;
    private long timeLeftInMilliseconds;
    private TextView car;
    private TextView showCar;
    private TextView zone;
    private TextView showZone;
    private TextView remaining;
    private TextView timeLeft;
    private TextView ends;
    private TextView timeEnds;
    private TextView confirm;
    private File file;
    private String currentZone = "";
    private int parkingEndsMinutes = -1;
    private boolean needAlert = false;
    private String confirmButtonLabel;
    private int choosingZoneAndTimeClockInMinutes = -1;

    private String Green;
    private String Blue;
    private String Red;
    private String Yellow;
    private String Orange;

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

        setZonesValues();
        init(view);
        database(view);
        loadData();
        checkCarRegistration();
        confirmParking();
        showPriceAndParkingEnding(view);

    }

    public void setZonesValues(){
        Green = getString(R.string.green_zone_home);
        Blue = getString(R.string.blue_zone_home);
        Red = getString(R.string.red_zone_home);
        Yellow = getString(R.string.yellow_zone_home);
        Orange = getString(R.string.orange_zone_home);
    }

    public void init(View view){
        compositeDisposable = new CompositeDisposable();
        licensePlateDatabase = LicensePlateDatabase.getInstance(getActivity());
        licensePlateRepository = LicensePlateRepository.getInstance(LocalUserDataSource.getInstance(licensePlateDatabase.licensePlateDao()));

        confirm = view.findViewById(R.id.btn_homeConfirm);
    }

    public void disableEnableConfirm(){
        if (tempPrice.getText().toString().compareTo("-") == 0){
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
                    public void accept(List<LicensePlate> licensePlates) {
                        if (licensePlates.isEmpty() && homeFragment != null && homeFragment.isVisible())
                            startActivity(new Intent(getActivity(), addCarActivity.class));
                    }
                });
        compositeDisposable.add(disposable);
    }


    public void showPriceAndParkingEnding(View view)
    {
        final ArrayList<String> zones = new ArrayList<>();
        zones.add(Green);
        zones.add(Blue);
        zones.add(Red);
        zones.add(Yellow);
        zones.add(Orange);
        final ArrayList<String> time = new ArrayList<>();


        listZones = view.findViewById(R.id.list_zones);
        listTime = view.findViewById(R.id.list_time);
        final ArrayAdapter<String> zonesAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_single_choice, zones);
        final ArrayAdapter<String> timeAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_single_choice, time);
        listZones.setAdapter(zonesAdapter);
        listTime.setAdapter(timeAdapter);


        TextView outputPrice = view.findViewById(R.id.txt_outputPrice);
        tempPrice = outputPrice;
        TextView outputTime = view.findViewById(R.id.txt_outputTime);
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

                String h = getString(R.string.hour);

                if(chosenZone.compareTo(Green) == 0){
                    time.removeAll(time);
                    time.add("1  " + h + "   0 min");
                    time.add("2  " + h + "   0 min");
                    time.add("3  " + h + "   0 min");
                    time.add("4  " + h + "   0 min");
                    time.add("5  " + h + "   0 min");
                    time.add("6  " + h + "   0 min");
                    time.add("7  " + h + "   0 min");
                    time.add("8  " + h + "   0 min");
                    time.add("9  " + h + "   0 min");
                    time.add("10 " + h + "  0 min");
                    chosenMinutes = -1;
                } else if(chosenZone.compareTo(Blue) == 0){
                    time.removeAll(time);
                    time.add("0  " + h + " 30 min");
                    time.add("1  " + h + "   0 min");
                    time.add("1  " + h + " 30 min");
                    time.add("2  " + h + "   0 min");
                    time.add("3  " + h + "   0 min");
                    time.add("4  " + h + "   0 min");
                    time.add("5  " + h + "   0 min");
                    time.add("6  " + h + "   0 min");
                    time.add("7  " + h + "   0 min");
                    time.add("8  " + h + "   0 min");
                    time.add("9  " + h + "   0 min");
                    time.add("10 " + h + "  0 min");
                    chosenMinutes = -1;
                } else if(chosenZone.compareTo("") == 0) {
                    time.removeAll(time);
                } else {
                    time.removeAll(time);
                    time.add("0  " + h + " 15 min");
                    time.add("0  " + h + " 30 min");
                    time.add("0  " + h + " 45 min");
                    time.add("1  " + h + "   0 min");
                    time.add("1  " + h + " 15 min");
                    time.add("1  " + h + " 30 min");
                    time.add("2  " + h + "   0 min");
                    time.add("3  " + h + "   0 min");
                    time.add("4  " + h + "   0 min");
                    time.add("5  " + h + "   0 min");
                    time.add("6  " + h + "   0 min");
                    time.add("7  " + h + "   0 min");
                    time.add("8  " + h + "   0 min");
                    time.add("9  " + h + "   0 min");
                    time.add("10 " + h + " 0 min");
                    chosenMinutes = -1;
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

                rememberTime();

                String tempChosenMinutes = ((TextView) view).getText().toString();

                Scanner scan = new Scanner(tempChosenMinutes).useDelimiter("\\s+");
                int minutes = scan.nextInt() * 60;
                scan.next();
                minutes += scan.nextInt();
                scan.close();

                int previousMinutes = chosenMinutes;
                chosenMinutes = minutes;

                if (previousMinutes == chosenMinutes){
                    listTime.setItemChecked(listTime.getCheckedItemPosition(), false);
                    chosenMinutes = -1;
                    choosingZoneAndTimeClockInMinutes = -1;
                }
                else if (chosenZone != "") {
                    parkingEnds = estimatedTime(chosenMinutes / 60, chosenMinutes % 60);
                    finalPrice = estimatedPrice(chosenZone, chosenMinutes / 60, chosenMinutes % 60);
                }

                tempPrice.setText(finalPrice);
                tempTime.setText(parkingEnds);

                disableEnableConfirm();
            }
        });


    }

    public void rememberTime(){
        Calendar currentTime = GregorianCalendar.getInstance();
        currentTime.setTime(new Date());

        int hours = currentTime.get(Calendar.HOUR_OF_DAY);
        int minutes = currentTime.get(Calendar.MINUTE);

        choosingZoneAndTimeClockInMinutes = (hours * 60) + minutes;
    }

    public String estimatedPrice(String color, int chosenHour, int chosenMinute)
    {
        double total;
        double price = 0;

        if(color.compareTo(Green) == 0){
            price = 0.3 / 60d;
            total = ((chosenHour*60) + chosenMinute) * price;
        } else if(color.compareTo(Blue) == 0){
            price = 0.6 / 60d;
            total = ((chosenHour*60) + chosenMinute) * price;
        } else if(color.compareTo(Red) == 0) {
            price = 1.2 / 60d;
            total = ((chosenHour*60) + chosenMinute) * price;
        } else if(color.compareTo(Yellow) == 0) {
            price = 2 / 60d;
            total = ((chosenHour*60) + chosenMinute) * price;
        } else if(color.compareTo(Orange) == 0) {
            price = 2 / 60d;
            total = ((chosenHour*60) + chosenMinute) * price;
        } else total = 0;

        String totalString = String.valueOf(total) + "0" + " \u20ac";
        return totalString;
    }

    public String estimatedTime(int chosenHour, int chosenMinute)
    {
        Calendar currentTime = GregorianCalendar.getInstance();
        Date date = new Date();
        if(chosenZone.compareTo(currentZone) == 0 && chosenDefaultNumber.compareTo(currentDefaultNumber) == 0){
            currentTime.set(Calendar.HOUR_OF_DAY, parkingEndsMinutes/60);
            currentTime.set(Calendar.MINUTE, parkingEndsMinutes%60);
            currentTime.set(Calendar.SECOND,0);
            currentTime.set(Calendar.MILLISECOND,0);

            date = currentTime.getTime();
        }
        currentTime.setTime(date);
        currentTime.add(Calendar.HOUR_OF_DAY, chosenHour);
        currentTime.add(Calendar.MINUTE, chosenMinute);

        String totalTime = (currentTime.get(Calendar.HOUR_OF_DAY) < 10? ("0"+currentTime.get(Calendar.HOUR_OF_DAY)) : currentTime.get(Calendar.HOUR_OF_DAY)) + ":" + (currentTime.get(Calendar.MINUTE) < 10? ("0"+currentTime.get(Calendar.MINUTE)) : currentTime.get(Calendar.MINUTE));

        Calendar c = GregorianCalendar.getInstance();
        c.setTime(new Date());

        if(chosenZone == Orange){
            if((timeEnds.getText().toString().compareTo("0:00") == 0 && chosenDefaultNumber.compareTo(currentDefaultNumber) == 0) || currentTime.get(Calendar.DAY_OF_MONTH) != c.get(Calendar.DAY_OF_MONTH)){
                totalTime = "0:00";
                needAlert = true;
            }
            else needAlert = false;
        } else if ( (currentTime.get(Calendar.HOUR_OF_DAY)*60+currentTime.get(Calendar.MINUTE)) > (18*60) || currentTime.get(Calendar.DAY_OF_MONTH) != c.get(Calendar.DAY_OF_MONTH)) {
            if(c.get(Calendar.HOUR_OF_DAY) <= 18 && c.get(Calendar.HOUR_OF_DAY) >= 8) {
                totalTime = "18:00";
                needAlert = true;
            }
        }
        else needAlert = false;

        return totalTime;
    }


    public boolean needsPopUp (String color, Calendar currentTime )
    {
        //perkelti data i calla
        //Calendar currentTime = GregorianCalendar.getInstance();
        //currentTime.setTime(new Date());

        if(color.compareTo(Orange) == 0){
            if (currentTime.get(Calendar.HOUR_OF_DAY) >= 24 || currentTime.get(Calendar.HOUR_OF_DAY) < 8)
            {
                String toastNotification = getString(R.string.toastFreeTime);
                Toast.makeText(getActivity(), toastNotification, Toast.LENGTH_LONG).show();
                return true;
            }
        } else if(color.compareTo(Green) == 0 || color.compareTo(Blue) == 0 || color.compareTo(Red) == 0 || color.compareTo(Yellow) == 0) {
            //https://coderanch.com/t/491207/certification/Confusion-understanding-DAY-WEEK
            if (currentTime.get(Calendar.DAY_OF_WEEK) == 7 || currentTime.get(Calendar.DAY_OF_WEEK) == 1) {
                String toastNotification = getString(R.string.toastFreeDay);
                Toast.makeText(getActivity(), toastNotification, Toast.LENGTH_LONG).show();
                return true;
            }
            else if (currentTime.get(Calendar.HOUR_OF_DAY) >= 18 || currentTime.get(Calendar.HOUR_OF_DAY) < 8) {
                String toastNotification = getString(R.string.toastFreeTime);
                Toast.makeText(getActivity(), toastNotification, Toast.LENGTH_LONG).show();
                return true;
            }
        }
        return false;
    }


    //Everything for database --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    public void database(View view){

        //init View
        adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_layout, licensePlateList);
        spin_DefaultCar = view.findViewById(R.id.spin_DefaultCar);
        registerForContextMenu(spin_DefaultCar);
        spin_DefaultCar.setAdapter(adapter);
    }

    private void loadData()
    {

        confirmButtonLabel = getString(R.string.confirm);

        Disposable disposable = licensePlateRepository.getAll()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<List<LicensePlate>>() {
                    @Override
                    public void accept(List<LicensePlate> licensePlates) {
                        onGetAllLicensePlateSuccess(licensePlates);
                        tempLicensePlate = licensePlates;
                    }

                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        Toast.makeText(getActivity(), ""+throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        compositeDisposable.add(disposable);

        getAndSetDefault();

        spin_DefaultCar.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                boolean notSelected = false;

                String notSelectedText = getString(R.string.not_selected);

                if(licensePlateList.get(0).toString().compareTo(notSelectedText) == 0) notSelected = true;
                for(int j=0; j < tempLicensePlate.size(); j++) {
                    if (tempLicensePlate.get(j).getNumber().compareTo(spin_DefaultCar.getSelectedItem().toString()) == 0) {
                        setDefault(tempLicensePlate.get(j));
                        if(notSelected && spin_DefaultCar.getSelectedItemId() < licensePlateList.size() - 1) spin_DefaultCar.setSelection((int)spin_DefaultCar.getSelectedItemId() - 1);
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
            public void subscribe(ObservableEmitter<Object> e) {

                chosenDefaultNumber = licensePlateRepository.findDefault().getNumber();
                isDefaultSelected = licensePlateRepository.findDefault().getCurrent();

                spin_DefaultCar.post(new Runnable() {
                    @Override
                    public void run() {

                        spin_DefaultCar.clearFocus();

                        for (int i=0; i < licensePlateList.size(); i++){
                            if (chosenDefaultNumber.compareTo(licensePlateList.get(i)) == 0){
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
                    public void accept(Object o) {}
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                    }
                });
        compositeDisposable.add(disposable);
    }

    public void onGetAllLicensePlateSuccess(List<LicensePlate> licensePlates)
    {
        licensePlateList.clear();

        if(!isDefaultSelected){

            if(confirmButtonLabel.compareTo("Patvirtinti") == 0){
                licensePlateList.add("Nepasirinktas");
            } else licensePlateList.add("Not selected");

            chosenDefaultNumber = "";
        }

        for (int i = 0; i < licensePlates.size(); i++) {
            licensePlateList.add(licensePlates.get(i).getNumber());
        }

        adapter.notifyDataSetChanged();
    }

    private void setDefault(final LicensePlate licensePlate) {
        Disposable disposable = io.reactivex.Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> e) {
                LicensePlate oldLicensePlate = licensePlateRepository.findDefault();
                if (oldLicensePlate != null)
                {
                    oldLicensePlate.setCurrent(false);
                    licensePlateRepository.updateLicensePlate(oldLicensePlate);
                }
                licensePlate.setCurrent(true);
                licensePlateRepository.updateLicensePlate(licensePlate);
                isDefaultSelected = licensePlateRepository.findDefault().getCurrent();
                chosenDefaultNumber = licensePlateRepository.findDefault().getNumber();

                if (chosenMinutes != -1) {
                    chosenDefaultNumber = licensePlateRepository.findDefault().getNumber();
                    parkingEnds = estimatedTime(chosenMinutes / 60, chosenMinutes % 60);
                    tempTime.setText(parkingEnds);
                }

                e.onComplete();
            }
        })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer() {
                    @Override
                    public void accept(Object o) {}
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {

                    }
                });
        compositeDisposable.add(disposable);
    }

    //--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    private void confirmParking(){

        car = getActivity().findViewById(R.id.car);
        showCar = getActivity().findViewById(R.id.carText);
        zone = getActivity().findViewById(R.id.color);
        showZone = getActivity().findViewById(R.id.colorText);
        remaining = getActivity().findViewById(R.id.remaining);
        timeLeft = getActivity().findViewById(R.id.remainingText);
        ends = getActivity().findViewById(R.id.ends);
        timeEnds = getActivity().findViewById(R.id.endsText);

        file = getContext().getFileStreamPath("Countdown");

        if (file.exists()) {
            try {

                FileInputStream fileInputStream = getActivity().openFileInput("Countdown");
                InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                parkingEndsMinutes = Integer.parseInt(bufferedReader.readLine());
                String parkingDate = bufferedReader.readLine();
                currentZone = bufferedReader.readLine();
                currentDefaultNumber = bufferedReader.readLine();

                if(currentZone.compareTo("Z") == 0){
                    currentZone = Green;
                } else if(currentZone.compareTo("M") == 0){
                    currentZone = Blue;
                } else if(currentZone.compareTo("R") == 0) {
                    currentZone = Red;
                } else if(currentZone.compareTo("G") == 0) {
                    currentZone = Yellow;
                } else if(currentZone.compareTo("A") == 0) {
                    currentZone = Orange;
                }

                if(currentZone.compareTo(Green) == 0){
                    showZone.setTextColor(Color.parseColor("#7FFF00"));
                } else if(currentZone.compareTo(Blue) == 0){
                    showZone.setTextColor(Color.parseColor("#73C2FB"));
                } else if(currentZone.compareTo(Red) == 0) {
                    showZone.setTextColor(Color.parseColor("#FF0000"));
                } else if(currentZone.compareTo(Yellow) == 0) {
                    showZone.setTextColor(Color.parseColor("#FFFB00"));
                } else if(currentZone.compareTo(Orange) == 0) {
                    showZone.setTextColor(Color.parseColor("#F9A602"));
                }

                if (!MainActivity.isTimerCreated) {
                    startParking(parkingDate);

                    if (MainActivity.isTimerCreated) {

                        showCar.setText(currentDefaultNumber);

                        Scanner scan = new Scanner(currentZone).useDelimiter("\\s+");
                        showZone.setText(scan.next());

                        String timeEndsText;

                        if ((parkingEndsMinutes / 60) < 10) timeEndsText = "0";
                        timeEndsText = "" + parkingEndsMinutes / 60;
                        timeEndsText += ":";
                        if ((parkingEndsMinutes % 60) < 10) timeEndsText += "0";
                        timeEndsText += parkingEndsMinutes % 60;

                        timeEnds.setText(timeEndsText);
                    } else {
                        car.setVisibility(View.INVISIBLE);
                        showCar.setVisibility(View.INVISIBLE);
                        zone.setVisibility(View.INVISIBLE);
                        showZone.setVisibility(View.INVISIBLE);
                        remaining.setVisibility(View.INVISIBLE);
                        timeLeft.setVisibility(View.INVISIBLE);
                        ends.setVisibility(View.INVISIBLE);
                        timeEnds.setVisibility(View.INVISIBLE);
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            car.setVisibility(View.INVISIBLE);
            showCar.setVisibility(View.INVISIBLE);
            zone.setVisibility(View.INVISIBLE);
            showZone.setVisibility(View.INVISIBLE);
            remaining.setVisibility(View.INVISIBLE);
            timeLeft.setVisibility(View.INVISIBLE);
            ends.setVisibility(View.INVISIBLE);
            timeEnds.setVisibility(View.INVISIBLE);
        }

        confirm.setOnClickListener(new View.OnClickListener() {

            String confirmParkingMessage = getString(R.string.confirm_parking_message);

            @Override
            public void onClick(View view) {

                Calendar currentTime = GregorianCalendar.getInstance();
                if(!needsPopUp(chosenZone, currentTime)) {
                    if(isDefaultSelected) {
                        currentTime = GregorianCalendar.getInstance();
                        currentTime.setTime(new Date());

                        int currentTimeInMinutes = currentTime.get(Calendar.HOUR_OF_DAY) * 60 + currentTime.get(Calendar.MINUTE);

                        if(currentTimeInMinutes != choosingZoneAndTimeClockInMinutes && tempTime.getText().toString() != "0:00" && tempTime.getText().toString() != "18:00" && !(chosenZone.compareTo(currentZone) == 0 && chosenDefaultNumber.compareTo(currentDefaultNumber) == 0)){


                            String unfortunatelyMessage = getResources().getString(R.string.unfortunately_message);

                            new AlertDialog.Builder(getActivity(), R.style.AlertDialog)
                                    .setMessage(unfortunatelyMessage)
                                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            //continue

                                            confirmButtonAction(confirmParkingMessage);

                                        }
                                    }).create().show();

                        }
                        else confirmButtonAction(confirmParkingMessage);
                    }
                    else {
                        String pleaseSelectDefault = getString(R.string.please_select_default);
                        Toast.makeText(getActivity(), pleaseSelectDefault, Toast.LENGTH_LONG).show();
                    }
                }

            }
        });
    }

    public void confirmButtonAction(String confirmParkingMessage) {

        new AlertDialog.Builder(getActivity(), R.style.AlertDialog)
                .setMessage(confirmParkingMessage)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        checkSMSPermissions();
                        if (checkSelfPermission(getActivity(), Manifest.permission.SEND_SMS)
                                == PackageManager.PERMISSION_GRANTED) {

                            if (needAlert) {

                                String attentionConfirmMessage = getResources().getString(R.string.attention_confirm_message);

                                new AlertDialog.Builder(getActivity(), R.style.AlertDialog)
                                        .setMessage(Html.fromHtml(attentionConfirmMessage))
                                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                                confirmAndSend();
                                                setNotifications();
                                                confirmButtonSound();

                                                Calendar currentTime = GregorianCalendar.getInstance();
                                                currentTime.setTime(new Date());

                                                int hours = currentTime.get(Calendar.HOUR_OF_DAY);
                                                int minutes = currentTime.get(Calendar.MINUTE);

                                                choosingZoneAndTimeClockInMinutes = (hours * 60) + minutes;

                                            }
                                        }).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }).create().show();
                            } else {
                                confirmAndSend();
                                setNotifications();
                                confirmButtonSound();

                                Calendar currentTime = GregorianCalendar.getInstance();
                                currentTime.setTime(new Date());

                                int hours = currentTime.get(Calendar.HOUR_OF_DAY);
                                int minutes = currentTime.get(Calendar.MINUTE);

                                choosingZoneAndTimeClockInMinutes = (hours * 60) + minutes;
                            }
                        }
                    }
                }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).create().show();
    }

    public void confirmButtonSound()
    {
        MediaPlayer MPconfirmSound = MediaPlayer.create(getActivity(), R.raw.sound);
        MPconfirmSound.start();
    }

    public void setNotifications(){

        Intent intent = new Intent(getActivity().getApplicationContext(), NotificationReceiver_First.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity().getApplicationContext(), 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);

        intent = new Intent(getActivity().getApplicationContext(), NotificationReceiver_Second.class);
        pendingIntent = PendingIntent.getBroadcast(getActivity().getApplicationContext(), 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);

        intent = new Intent(getActivity().getApplicationContext(), NotificationReceiver_Third.class);
        pendingIntent = PendingIntent.getBroadcast(getActivity().getApplicationContext(), 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);

        intent = new Intent(getActivity().getApplicationContext(), NotificationReceiver_First_Lt.class);
        pendingIntent = PendingIntent.getBroadcast(getActivity().getApplicationContext(), 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);;

        intent = new Intent(getActivity().getApplicationContext(), NotificationReceiver_Second_Lt.class);
        pendingIntent = PendingIntent.getBroadcast(getActivity().getApplicationContext(), 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);

        intent = new Intent(getActivity().getApplicationContext(), NotificationReceiver_Third_Lt.class);
        pendingIntent = PendingIntent.getBroadcast(getActivity().getApplicationContext(), 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);



        if(!MainActivity.isNotificationsOn || MainActivity.isSmsNotifiationsOn){
            return;
        }

        Calendar calendar = Calendar.getInstance();

        long notificationTime;



        if(parkingEndsMinutes == 0){
                notificationTime = ((parkingEndsMinutes + ((23 * 60) + 59)) * 60000) - (10 * 60000);
        }
        else notificationTime = (parkingEndsMinutes * 60000) - (10 * 60000);
        int hours = (int) notificationTime / 3600000;
        int minutes = (int) (notificationTime % 3600000) / 60000;

        calendar.set(Calendar.HOUR_OF_DAY, hours);
        calendar.set(Calendar.MINUTE, minutes);

        intent = new Intent(getActivity().getApplicationContext(), getString(R.string.confirm).compareTo("Patvirtinti") == 0 ? NotificationReceiver_First_Lt.class : NotificationReceiver_First.class);

        pendingIntent = PendingIntent.getBroadcast(getActivity().getApplicationContext(), 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

        //-----------------------------------------------------------------------------------------------------------------------------------------------------------

        calendar = Calendar.getInstance();

        if(parkingEndsMinutes == 0){
            notificationTime = ((parkingEndsMinutes + ((23 * 60) + 59)) * 60000) - (5 * 60000);
        }
        else notificationTime = (parkingEndsMinutes * 60000) - (5 * 60000);
        hours = (int) notificationTime / 3600000;
        minutes = (int) (notificationTime % 3600000) / 60000;

        calendar.set(Calendar.HOUR_OF_DAY, hours);
        calendar.set(Calendar.MINUTE, minutes);

        intent = new Intent(getActivity().getApplicationContext(), getString(R.string.confirm).compareTo("Patvirtinti") == 0 ? NotificationReceiver_Second_Lt.class : NotificationReceiver_Second.class);

        pendingIntent = PendingIntent.getBroadcast(getActivity().getApplicationContext(), 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

        //-----------------------------------------------------------------------------------------------------------------------------------------------------------

        calendar = Calendar.getInstance();

        if(parkingEndsMinutes == 0){
            calendar.set(Calendar.HOUR_OF_DAY, ((parkingEndsMinutes + ((23 * 60) + 59) / 60)));
            calendar.set(Calendar.MINUTE, ((parkingEndsMinutes + ((23 * 60) + 59) % 60)));
        } else {
            calendar.set(Calendar.HOUR_OF_DAY, (parkingEndsMinutes / 60));
            calendar.set(Calendar.MINUTE, (parkingEndsMinutes % 60));
        }

        intent = new Intent(getActivity().getApplicationContext(), getString(R.string.confirm).compareTo("Patvirtinti") == 0 ? NotificationReceiver_Third_Lt.class : NotificationReceiver_Third.class);

        pendingIntent = PendingIntent.getBroadcast(getActivity().getApplicationContext(), 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

    }

    public void confirmAndSend() {

        car.setVisibility(View.VISIBLE);
        showCar.setVisibility(View.VISIBLE);
        zone.setVisibility(View.VISIBLE);
        showZone.setVisibility(View.VISIBLE);
        remaining.setVisibility(View.VISIBLE);
        timeLeft.setVisibility(View.VISIBLE);
        ends.setVisibility(View.VISIBLE);
        timeEnds.setVisibility(View.VISIBLE);

        if (file.exists()) {
            file.delete();
            if (MainActivity.isTimerCreated) {
                MainActivity.countDownTimer.cancel();
                MainActivity.isTimerCreated = false;
            }
        }

        Scanner scan = new Scanner(tempTime.getText().toString()).useDelimiter(":");

        Calendar currentTime = GregorianCalendar.getInstance();
        currentTime.setTime(new Date());

        int currentTimeInMinutes = currentTime.get(Calendar.HOUR_OF_DAY) * 60 + currentTime.get(Calendar.MINUTE);

        if(currentTimeInMinutes != choosingZoneAndTimeClockInMinutes && tempTime.getText().toString() != "0:00" && tempTime.getText().toString() != "18:00" && !(chosenZone.compareTo(currentZone) == 0 && chosenDefaultNumber.compareTo(currentDefaultNumber) == 0)){
            parkingEndsMinutes = scan.nextInt() * 60 + scan.nextInt() % 60 + (currentTimeInMinutes - choosingZoneAndTimeClockInMinutes);

            currentTime.add(Calendar.HOUR_OF_DAY, chosenMinutes / 60);
            currentTime.add(Calendar.MINUTE, chosenMinutes % 60);

            tempTime.setText((currentTime.get(Calendar.HOUR_OF_DAY) < 10? ("0"+currentTime.get(Calendar.HOUR_OF_DAY)) : currentTime.get(Calendar.HOUR_OF_DAY)) + ":" + (currentTime.get(Calendar.MINUTE) < 10? ("0"+currentTime.get(Calendar.MINUTE)) : currentTime.get(Calendar.MINUTE)));
        } else parkingEndsMinutes = scan.nextInt() * 60 + scan.nextInt() % 60;

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c);

        startParking(formattedDate);

        String fileName = "Countdown";

        String chosenZoneTemp = "";

        if(chosenZone.compareTo(Green) == 0){
            chosenZoneTemp = "Z";
        } else if(chosenZone.compareTo(Blue) == 0){
            chosenZoneTemp = "M";
        } else if(chosenZone.compareTo(Red) == 0) {
            chosenZoneTemp = "R";
        } else if(chosenZone.compareTo(Yellow) == 0) {
            chosenZoneTemp = "G";
        } else if(chosenZone.compareTo(Orange) == 0) {
            chosenZoneTemp = "A";
        }

        try {
            FileOutputStream fileOutputStream = getActivity().openFileOutput(fileName, Context.MODE_APPEND);
            fileOutputStream.write(String.valueOf(parkingEndsMinutes).getBytes());
            fileOutputStream.write("\n".getBytes());
            fileOutputStream.write(formattedDate.getBytes());
            fileOutputStream.write("\n".getBytes());
            fileOutputStream.write(chosenZoneTemp.getBytes());
            fileOutputStream.write("\n".getBytes());
            fileOutputStream.write(chosenDefaultNumber.getBytes());
            fileOutputStream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        currentZone = chosenZone;
        currentDefaultNumber = chosenDefaultNumber;

        showCar.setText(currentDefaultNumber);

        scan = new Scanner(currentZone).useDelimiter("\\s+");
        String color = scan.next();
        showZone.setText(color);

        if(currentZone.compareTo(Green) == 0){
            showZone.setTextColor(Color.parseColor("#7FFF00"));
        } else if(currentZone.compareTo(Blue) == 0){
            showZone.setTextColor(Color.parseColor("#73C2FB"));
        } else if(currentZone.compareTo(Red) == 0) {
            showZone.setTextColor(Color.parseColor("#FF0000"));
        } else if(currentZone.compareTo(Yellow) == 0) {
            showZone.setTextColor(Color.parseColor("#FFFB00"));
        } else if(currentZone.compareTo(Orange) == 0) {
            showZone.setTextColor(Color.parseColor("#F9A602"));
        } else showZone.setTextColor(Color.parseColor("#FFFFFF"));

        timeEnds.setText(tempTime.getText().toString());

        if (chosenMinutes != -1) {
            parkingEnds = estimatedTime(chosenMinutes / 60, chosenMinutes % 60);
            tempTime.setText(parkingEnds);
        }

        send();
    }

    public void checkSMSPermissions(){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {

            if (checkSelfPermission(getActivity(), Manifest.permission.SEND_SMS)
                    == PackageManager.PERMISSION_DENIED) {

                Log.d("permission", "permission denied to SEND_SMS - requesting it");
                String[] permissions = {Manifest.permission.SEND_SMS};

                requestPermissions(permissions, 1);

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == 1){

            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                confirmAndSend();
            } else{
                String permissionNotGranted = getString(R.string.permission_not_granted);
                Toast.makeText(getActivity(), permissionNotGranted, Toast.LENGTH_SHORT).show();
            }

        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void send(){
        SmsManager sms = SmsManager.getDefault();

        String zone = "";

        if(currentZone.compareTo(Green) == 0){
            zone = "Z";
        } else if(currentZone.compareTo(Blue) == 0){
            zone = "M";
        } else if(currentZone.compareTo(Red) == 0) {
            zone = "R";
        } else if(currentZone.compareTo(Yellow) == 0) {
            zone = "G";
        } else if(currentZone.compareTo(Orange) == 0) {
            zone = "A";
        }

        int hour = chosenMinutes / 60;
        int minute = chosenMinutes % 60;

        if(minute != 0 && minute % 2 == 0){
            minute = minute / 10;
        }

        String parkingTime = minute == 0 ? String.valueOf(hour) : String.valueOf(hour) + "." + String.valueOf(minute);

        String smsNotifications =  !MainActivity.isSmsNotifiationsOn ? " NP" : "";
        String message = "PK " + parkingTime + " " + zone + " " + currentDefaultNumber + smsNotifications;

        sms.sendTextMessage("1332", null, message, null, null);

        String confirmedSuccesfully = getString(R.string.confirmed_successfully);

        Toast.makeText(getActivity(), confirmedSuccesfully, Toast.LENGTH_SHORT).show();
    }

    public long calculateTimeLeft(int parkingEndsMinutes, int hours, int minutes){

        if(parkingEndsMinutes == 0){
            parkingEndsMinutes = 24 * 60;
        }

        long timeLeftInMilliseconds = (parkingEndsMinutes - (hours * 60 + minutes)) * 60000;

        return  timeLeftInMilliseconds;
    }

    public void startParking(String parkingDate){

        Calendar currentTime = GregorianCalendar.getInstance();
        currentTime.setTime(new Date());
        timeLeftInMilliseconds = calculateTimeLeft(parkingEndsMinutes, currentTime.get(Calendar.HOUR_OF_DAY), currentTime.get(Calendar.MINUTE));

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c);

        if(timeLeftInMilliseconds <= 0 || parkingDate.compareTo(formattedDate) != 0){
            if(file.exists()) {
                file.delete();
            }
            timeLeftInMilliseconds = -1;
            currentZone = "";
            parkingEndsMinutes = -1;
            currentDefaultNumber = "";
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
                if(file.exists()) {
                    file.delete();
                    timeLeftInMilliseconds = -1;
                    currentZone = "";
                    parkingEndsMinutes = -1;
                    currentDefaultNumber = "";
                    MainActivity.isTimerCreated = false;
                }
                car.setVisibility(View.INVISIBLE);
                showCar.setVisibility(View.INVISIBLE);
                zone.setVisibility(View.INVISIBLE);
                showZone.setVisibility(View.INVISIBLE);
                remaining.setVisibility(View.INVISIBLE);
                timeLeft.setVisibility(View.INVISIBLE);
                ends.setVisibility(View.INVISIBLE);
                timeEnds.setVisibility(View.INVISIBLE);
            }
        }.start();
    }

    public void updateTimer(long timeLeftInMilliseconds){

        int timeLeftInMinutes = (int) timeLeftInMilliseconds / 60000;

        int hours = timeLeftInMinutes / 60;
        int minutes = timeLeftInMinutes % 60;
        int seconds = (int) timeLeftInMilliseconds % 60000 / 1000;

        String timeLeftText;

        timeLeftText = "" + hours;
        timeLeftText += ":";
        if(minutes < 10) timeLeftText += "0";
        timeLeftText += minutes;
        timeLeftText += ":";
        if(seconds < 10) timeLeftText += "0";
        timeLeftText += seconds;

        if(timeLeftInMinutes >= 10){
            timeLeft.setTextColor(-1275068417);
        }

        if(timeLeftInMinutes < 10 && timeLeftInMinutes >= 5){
            timeLeft.setTextColor(Color.parseColor("#FF0000"));
        }

        if(timeLeftInMinutes < 5){
            if(timeLeft.getCurrentTextColor() == Color.parseColor("#FF0000") || timeLeft.getCurrentTextColor() == -1275068417){
                timeLeft.setTextColor(Color.parseColor("#FFFB00"));
            }
            else if(timeLeft.getCurrentTextColor() == Color.parseColor("#FFFB00")){
                timeLeft.setTextColor(Color.parseColor("#FF0000"));
            }
        }
        timeLeft.setText(timeLeftText);
    }

}
