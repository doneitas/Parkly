package com.example.parkly.Fragment;

import android.content.Context;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
        carLabel = view.findViewById(R.id.car);
        zoneLabel = view.findViewById(R.id.color);
        endsLabel = view.findViewById(R.id.ends);
        remainingLabel = view.findViewById(R.id.remaining);


        changeLanguage();
        checkState();
        onSoundClick();
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

                if(spinLanguage.getSelectedItem().toString().compareTo("LT") == 0){
                    selectedLanguage = "lt";
                    setLanguageForApp();
                    languageLabel.setText("Kalba");
                    soundLabel.setText("Garso efektai");
                    notificationsLabel.setText("Pranešimai apie stovėjimo laiką");
                    SMSnotificationsLabel.setText("SMS apie stovėjimo laiką");
                    carLabel.setText("Auto");
                    zoneLabel.setText("Zona");
                    endsLabel.setText("Baigiasi");
                    remainingLabel.setText("Liko");

                }
                else if(spinLanguage.getSelectedItem().toString().compareTo("EN") == 0){
                    selectedLanguage = "en";
                    setLanguageForApp();
                    languageLabel.setText("Language");
                    soundLabel.setText("Sound effects");
                    notificationsLabel.setText("Parking time notifications");
                    SMSnotificationsLabel.setText("SMS parking time notifications");
                    carLabel.setText("Car");
                    zoneLabel.setText("Zone");
                    endsLabel.setText("Ends");
                    remainingLabel.setText("Remaining");
                }
                else if(spinLanguage.getSelectedItem().toString().compareTo("Default") == 0){
                    selectedLanguage = "not-set";
                    setLanguageForApp();
                    /*languageLabel.setText("Language");
                    soundLabel.setText("Sound effects");
                    notificationsLabel.setText("Parking time notifications");
                    SMSnotificationsLabel.setText("SMS parking time notifications");*/
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

}
