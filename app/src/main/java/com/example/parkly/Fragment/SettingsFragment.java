package com.example.parkly.Fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.parkly.Activity.MainActivity;
import com.example.parkly.R;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

/**
 * Created by Marius on 2018-03-12.
 */

public class SettingsFragment extends Fragment {

    Switch aSwitch;

    Spinner spinLanguage;
    ArrayAdapter<String> spinAdapter;
    List<String> languageList = new ArrayList<String>();
    public static String selectedLanguage;
    TextView languageLabel;
    TextView soundLabel;
    TextView notificationsLabel;
    TextView SMSnotificationsLabel;
    TextView carLabel;
    TextView zoneLabel;
    TextView endsLabel;
    TextView remainingLabel;
    TextView zoneShowLabel;

    Switch notificationSwitch;
    Switch notificationSMSswitch;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.settings_fragment, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        aSwitch = view.findViewById(R.id.switch_sound);

        spinLanguage = view.findViewById(R.id.spin_Language);
        languageLabel = view.findViewById(R.id.txt_language);
        soundLabel = view.findViewById(R.id.switch_sound);
        notificationsLabel = view.findViewById(R.id.switch_notification);
        SMSnotificationsLabel = view.findViewById(R.id.switch2);
        carLabel = getActivity().findViewById(R.id.car);
        zoneLabel = getActivity().findViewById(R.id.color);
        endsLabel = getActivity().findViewById(R.id.ends);
        remainingLabel = getActivity().findViewById(R.id.remaining);
        zoneShowLabel = getActivity().findViewById(R.id.colorText);

        notificationSwitch = getActivity().findViewById(R.id.switch_notification);
        notificationSMSswitch = getActivity().findViewById(R.id.switch2);

        changeLanguage();

        checkState();
        onSoundClick();

        checkNotificationsState();
        changeNotificationSetting();
        changeSMSnotificationSetting();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
        menu.setHeaderTitle("Select action:");
        menu.add(Menu.NONE, 0, Menu.NONE, "Mark as default");
        menu.add(Menu.NONE, 1, Menu.NONE, "Delete");
        //menu.add(Menu.NONE, 0, Menu.NONE, "DELETE");
    }

    //--------------------------------------Sound setting-beginning--------------------------------------------------------------------------

    public void modifySound()
    {
        String soundSetting = String.valueOf(aSwitch.isChecked());
        String fileName = "soundFile";
        try {
            FileOutputStream fileOutputStream = getActivity().openFileOutput(fileName, Context.MODE_PRIVATE);
            fileOutputStream.write(soundSetting.getBytes());
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onSoundClick()
    {

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                modifySound();

                if (isChecked)
                {
                    UnMuteAudio();
                }
                else
                {
                    MuteAudio();
                }
            }
        });
    }

    public void checkState()
    {
        String state;
        try {
            FileInputStream fileInputStream = getActivity().openFileInput("soundFile");
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            if ((state = bufferedReader.readLine()) != null) {
                aSwitch.setChecked(Boolean.valueOf(state));

                if (Boolean.valueOf(state))
                {
                    UnMuteAudio();
                }
                else
                {
                    MuteAudio();
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void MuteAudio() {
        AudioManager mAlramMAnager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mAlramMAnager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_MUTE, 0);
            mAlramMAnager.adjustStreamVolume(AudioManager.STREAM_RING, AudioManager.ADJUST_MUTE, 0);
            mAlramMAnager.adjustStreamVolume(AudioManager.STREAM_SYSTEM, AudioManager.ADJUST_MUTE, 0);
        } else {
            mAlramMAnager.setStreamMute(AudioManager.STREAM_MUSIC, true);
            mAlramMAnager.setStreamMute(AudioManager.STREAM_RING, true);
            mAlramMAnager.setStreamMute(AudioManager.STREAM_SYSTEM, true);
        }
    }

    public void UnMuteAudio(){
        AudioManager mAlramMAnager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mAlramMAnager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_UNMUTE,0);
            mAlramMAnager.adjustStreamVolume(AudioManager.STREAM_RING, AudioManager.ADJUST_UNMUTE, 0);
            mAlramMAnager.adjustStreamVolume(AudioManager.STREAM_SYSTEM, AudioManager.ADJUST_UNMUTE, 0);
        } else {
            mAlramMAnager.setStreamMute(AudioManager.STREAM_MUSIC, false);
            mAlramMAnager.setStreamMute(AudioManager.STREAM_RING, false);
            mAlramMAnager.setStreamMute(AudioManager.STREAM_SYSTEM, false);
        }
    }

    //--------------------------------------Sound setting-ending--------------------------------------------------------------------------


    //--------------------------------------Language setting-beginning--------------------------------------------------------------------

    public void changeLanguage(){
        spinAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_layout, languageList);
        registerForContextMenu(spinLanguage);
        spinLanguage.setAdapter(spinAdapter);

        languageList.add("Default");
        languageList.add("LT");
        languageList.add("EN");

        if(selectedLanguage.compareTo("lt") == 0){
            spinLanguage.setSelection(1);
        }
        else if(selectedLanguage.compareTo("en") == 0){
            spinLanguage.setSelection(2);
        }
        else if(selectedLanguage.compareTo("not-set") == 0){
            spinLanguage.setSelection(0);
        }

        spinAdapter.notifyDataSetChanged();

        spinLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                String chosenZone = "";

                try {

                    FileInputStream fileInputStream = getActivity().openFileInput("Countdown");
                    InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    bufferedReader.readLine();
                    bufferedReader.readLine();
                    chosenZone = bufferedReader.readLine();
                    fileInputStream.close();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if(spinLanguage.getSelectedItem().toString().compareTo("LT") == 0){

                    selectedLanguage = "lt";

                    if(chosenZone.compareTo("Z") == 0){
                        zoneShowLabel.setText("Žalia");
                    }
                    else if(chosenZone.compareTo("M") == 0){
                        zoneShowLabel.setText("Mėlyna");
                    }
                    else if(chosenZone.compareTo("R") == 0){
                        zoneShowLabel.setText("Raudona");
                    }
                    else if(chosenZone.compareTo("G") == 0){
                        zoneShowLabel.setText("Geltona");
                    }
                    else if(chosenZone.compareTo("A") == 0) {
                        zoneShowLabel.setText("Oranžinė");
                    }

                    setLanguageForApp();


                    languageLabel.setText("Kalba");
                    soundLabel.setText("Garso efektai");
                    notificationsLabel.setText("Pranešimai apie stovėjimo laiką");
                    SMSnotificationsLabel.setText("SMS apie stovėjimo laiką");
                    carLabel.setText("Auto:");
                    zoneLabel.setText("Zona:");
                    endsLabel.setText("Pabaiga:");
                    remainingLabel.setText("Liko:");
                    MainActivity.homeLabel.setTitle("Pagrindinis");
                    MainActivity.carsLabel.setTitle("Automobiliai");
                    MainActivity.parkingAreasLabel.setTitle("Stovėjimo zonos");
                    MainActivity.settingsLabel.setTitle("Nustatymai");
                    MainActivity.informationLabel.setTitle("Informacija");
                    MainActivity.aboutLabel.setTitle("Apie");
                }
                else if(spinLanguage.getSelectedItem().toString().compareTo("EN") == 0) {

                    selectedLanguage = "en";


                    if (chosenZone.compareTo("Z") == 0) {
                        zoneShowLabel.setText("Green");
                    } else if (chosenZone.compareTo("M") == 0) {
                        zoneShowLabel.setText("Blue");
                    } else if (chosenZone.compareTo("R") == 0) {
                        zoneShowLabel.setText("Red");
                    } else if (chosenZone.compareTo("G") == 0) {
                        zoneShowLabel.setText("Yellow");
                    } else if (chosenZone.compareTo("A") == 0) {
                        zoneShowLabel.setText("Orange");
                    }

                    setLanguageForApp();

                    languageLabel.setText("Language");
                    soundLabel.setText("Sound effects");
                    notificationsLabel.setText("Parking time notifications");
                    SMSnotificationsLabel.setText("SMS parking time notifications");
                    carLabel.setText("Car:");
                    zoneLabel.setText("Zone:");
                    endsLabel.setText("Ends:");
                    remainingLabel.setText("Remaining:");
                    MainActivity.homeLabel.setTitle("Home");
                    MainActivity.carsLabel.setTitle("Cars");
                    MainActivity.parkingAreasLabel.setTitle("Parking areas");
                    MainActivity.settingsLabel.setTitle("Settings");
                    MainActivity.informationLabel.setTitle("Information");
                    MainActivity.aboutLabel.setTitle("About");
                }
                else if(spinLanguage.getSelectedItem().toString().compareTo("Default") == 0){
                    selectedLanguage = "not-set";
                    setLanguageForApp();

                    if(getString(R.string.confirm).compareTo("Patvirtinti") == 0) {

                        if (chosenZone.compareTo("Z") == 0) {
                            zoneShowLabel.setText("Žalia");
                        } else if (chosenZone.compareTo("M") == 0) {
                            zoneShowLabel.setText("Mėlyna");
                        } else if (chosenZone.compareTo("R") == 0) {
                            zoneShowLabel.setText("Raudona");
                        } else if (chosenZone.compareTo("G") == 0) {
                            zoneShowLabel.setText("Geltona");
                        } else if (chosenZone.compareTo("A") == 0) {
                            zoneShowLabel.setText("Oranžinė");
                        }

                        setLanguageForApp();


                        languageLabel.setText("Kalba");
                        soundLabel.setText("Garso efektai");
                        notificationsLabel.setText("Pranešimai apie stovėjimo laiką");
                        SMSnotificationsLabel.setText("SMS apie stovėjimo laiką");
                        carLabel.setText("Auto:");
                        zoneLabel.setText("Zona:");
                        endsLabel.setText("Pabaiga:");
                        remainingLabel.setText("Liko:");
                        MainActivity.homeLabel.setTitle("Pagrindinis");
                        MainActivity.carsLabel.setTitle("Automobiliai");
                        MainActivity.parkingAreasLabel.setTitle("Stovėjimo zonos");
                        MainActivity.settingsLabel.setTitle("Nustatymai");
                        MainActivity.informationLabel.setTitle("Informacija");
                        MainActivity.aboutLabel.setTitle("Apie");
                    } else {

                        if (chosenZone.compareTo("Z") == 0) {
                            zoneShowLabel.setText("Green");
                        } else if (chosenZone.compareTo("M") == 0) {
                            zoneShowLabel.setText("Blue");
                        } else if (chosenZone.compareTo("R") == 0) {
                            zoneShowLabel.setText("Red");
                        } else if (chosenZone.compareTo("G") == 0) {
                            zoneShowLabel.setText("Yellow");
                        } else if (chosenZone.compareTo("A") == 0) {
                            zoneShowLabel.setText("Orange");
                        }

                        setLanguageForApp();

                        languageLabel.setText("Language");
                        soundLabel.setText("Sound effects");
                        notificationsLabel.setText("Parking time notifications");
                        SMSnotificationsLabel.setText("SMS parking time notifications");
                        carLabel.setText("Car:");
                        zoneLabel.setText("Zone:");
                        endsLabel.setText("Ends:");
                        remainingLabel.setText("Remaining:");
                        MainActivity.homeLabel.setTitle("Home");
                        MainActivity.carsLabel.setTitle("Cars");
                        MainActivity.parkingAreasLabel.setTitle("Parking areas");
                        MainActivity.settingsLabel.setTitle("Settings");
                        MainActivity.informationLabel.setTitle("Information");
                        MainActivity.aboutLabel.setTitle("About");
                    }
                }

                modifyLanguageInFile();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                //do nothing
            }

        });
    }

    public void modifyLanguageInFile(){
        String fileName = "languageFile";
        try {
            FileOutputStream fileOutputStream = getActivity().openFileOutput(fileName, Context.MODE_PRIVATE);
            fileOutputStream.write(selectedLanguage.getBytes());
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setLanguageForApp(){
        Locale locale;
        if(selectedLanguage.equals("not-set")){ //use any value for default
            locale = MainActivity.defaultDeviceLocale;
        }
        else {
            locale = new Locale(selectedLanguage);
        }
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getActivity().getBaseContext().getResources().updateConfiguration(config, getActivity().getBaseContext().getResources().getDisplayMetrics());
    }

    //--------------------------------------Language setting-ending---------------------------------------------------------------------------



    //--------------------------------------Notifications setting-beginning--------------------------------------------------------------------

    public void checkNotificationsState() {

        if(MainActivity.isNotificationsOn){
            notificationSwitch.setChecked(true);
            notificationSMSswitch.setEnabled(true);

            if(MainActivity.isSmsNotifiationsOn){
                notificationSMSswitch.setChecked(true);
            } else notificationSMSswitch.setChecked(false);

        } else {
            notificationSwitch.setChecked(false);
            notificationSMSswitch.setChecked(false);
            notificationSMSswitch.setEnabled(false);
        }

    }

    public void changeNotificationSetting(){

        notificationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked)
                {
                    setNotifications(true);
                    notificationSMSswitch.setEnabled(true);
                }
                else
                {
                    setNotifications(false);
                    setSMSnotifications(false);
                    notificationSMSswitch.setChecked(false);
                    notificationSMSswitch.setEnabled(false);
                }
            }
        });

    }

    public void changeSMSnotificationSetting(){

        notificationSMSswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked)
                {

                    String SMSnotificationsAlert  = getString(R.string.sms_notification_alert);

                    new AlertDialog.Builder(getActivity(), R.style.AlertDialog)
                            .setMessage(SMSnotificationsAlert)
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    setSMSnotifications(true);

                                }
                            }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            setSMSnotifications(false);
                            notificationSMSswitch.setChecked(false);
                            dialog.dismiss();
                        }
                    }).create().show();
                }
                else
                {
                    setSMSnotifications(false);
                }

            }
        });

    }

    public void setNotifications(boolean setting){

        MainActivity.isNotificationsOn = setting;

        String fileName = "notificationFile";
        try {
            FileOutputStream fileOutputStream = getActivity().openFileOutput(fileName, Context.MODE_PRIVATE);
            fileOutputStream.write(String.valueOf(MainActivity.isNotificationsOn).getBytes());
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setSMSnotifications(boolean setting){

        MainActivity.isSmsNotifiationsOn = setting;

        String fileName = "SMSnotificationFile";
        try {
            FileOutputStream fileOutputStream = getActivity().openFileOutput(fileName, Context.MODE_PRIVATE);
            fileOutputStream.write(String.valueOf(MainActivity.isSmsNotifiationsOn).getBytes());
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //--------------------------------------Notifications setting-ending-----------------------------------------------------------------------


}
