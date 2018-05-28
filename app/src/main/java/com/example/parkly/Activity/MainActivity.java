package com.example.parkly.Activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.parkly.Fragment.AboutFragment;
import com.example.parkly.Fragment.CarsFragment;
import com.example.parkly.Fragment.HomeFragment;
import com.example.parkly.Fragment.ParkingFragment;
import com.example.parkly.Fragment.SettingsFragment;
import com.example.parkly.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.support.v4.content.ContextCompat.checkSelfPermission;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    public static boolean isCurrentFragmentIsHomeFragment;
    public static boolean isTimerCreated = false;
    public static CountDownTimer countDownTimer;

    public static Locale defaultDeviceLocale;

    public static MenuItem homeLabel;
    public static MenuItem carsLabel;
    public static MenuItem parkingAreasLabel;
    public static MenuItem settingsLabel;
    public static MenuItem informationLabel;
    public static MenuItem aboutLabel;

    public static boolean isNotificationsOn;
    public static boolean isSmsNotifiationsOn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        defaultDeviceLocale = Locale.getDefault();
        checkSelectedLanguage();
        setLanguageForApp();

        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");

        setNavigationLabelsValues();
        setNotificationSettingsValues();

        loadFragment(new HomeFragment(), "HOME_FRAGMENT");
        NavigationView navigationView = findViewById(R.id.nav_view);
        MenuItem item = navigationView.getMenu().findItem(R.id.nav_home);
        navigationView.setCheckedItem(item.getItemId());
        navigation();
    }

    public void setNotificationSettingsValues(){

        isNotificationsOn = true;
        isSmsNotifiationsOn = false;

        try {
            FileInputStream fileInputStream = openFileInput("notificationFile");
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String state = "";

            if ((state = bufferedReader.readLine()) != null) {

                if (Boolean.valueOf(state))
                {
                    isNotificationsOn = true;
                }
                else
                {
                    isNotificationsOn = false;
                    isSmsNotifiationsOn = false;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            FileInputStream fileInputStream = openFileInput("SMSnotificationFile");
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String state = "";

            if ((state = bufferedReader.readLine()) != null) {

                if (Boolean.valueOf(state))
                {
                    isSmsNotifiationsOn = true;
                }
                else
                {
                    isSmsNotifiationsOn = false;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void setNavigationLabelsValues(){
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu menu = navigationView.getMenu();
        homeLabel = menu.findItem(R.id.nav_home);
        carsLabel = menu.findItem(R.id.nav_cars);
        parkingAreasLabel = menu.findItem(R.id.nav_parking);
        settingsLabel = menu.findItem(R.id.nav_settings);
        informationLabel = menu.findItem(R.id.information);
        aboutLabel = menu.findItem(R.id.nav_aboutUs);
    }

    public void loadFragment(Fragment loadingFragment, String loadingFragmentTag){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();

        ft.replace(R.id.frame, loadingFragment, loadingFragmentTag);

        ft.commit();

        if (loadingFragmentTag == "HOME_FRAGMENT"){
            isCurrentFragmentIsHomeFragment = true;
        }
    }

    public void navigation(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if(getSupportFragmentManager().findFragmentByTag("HOME_FRAGMENT") == null) {
            if(getSupportFragmentManager().findFragmentByTag("CARS_FRAGMENT") != null && CarsFragment.deleteClicked){
                CarsFragment.deleteClicked = false;
                loadFragment(new CarsFragment(), "CARS_FRAGMENT");
            }
            else {
                loadFragment(new HomeFragment(), "HOME_FRAGMENT");
                NavigationView navigationView = findViewById(R.id.nav_view);
                MenuItem item = navigationView.getMenu().findItem(R.id.nav_home);
                navigationView.setCheckedItem(item.getItemId());
            }
        }
        else {

            String closeAppAlertText = getString(R.string.close_app_alert);

            new AlertDialog.Builder(this, R.style.AlertDialog)
                    .setMessage(closeAppAlertText)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                            System.exit(0);
                        }
                    }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).create().show();
        }
    }

    public void hideKeyboard(){
        View view = this.getCurrentFocus();
        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        Fragment selectedFragment = null;
        String fragmentTag = null;
        isCurrentFragmentIsHomeFragment = false;

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            selectedFragment = new HomeFragment();
            fragmentTag = "HOME_FRAGMENT";
            hideKeyboard();
            isCurrentFragmentIsHomeFragment = true;
        } else if (id == R.id.nav_cars) {
            selectedFragment = new CarsFragment();
            hideKeyboard();
            fragmentTag = "CARS_FRAGMENT";
        } else if (id == R.id.nav_parking) {
            selectedFragment = new ParkingFragment();
            hideKeyboard();
            fragmentTag = "PARKING_FRAGMENT";
        } else if (id == R.id.nav_settings) {
            selectedFragment = new SettingsFragment();
            hideKeyboard();
            fragmentTag = "SETTINGS_FRAGMENT";
        } else if (id == R.id.nav_aboutUs) {
            selectedFragment = new AboutFragment();
            hideKeyboard();
            fragmentTag = "ABOUT_FRAGMENT";
        }

        Fragment currentFragment = getSupportFragmentManager().findFragmentByTag(fragmentTag);

        if (selectedFragment != null && currentFragment == null){
            loadFragment(selectedFragment, fragmentTag);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void checkSelectedLanguage(){

        String selectedLanguageTemp = null;

        try {
            FileInputStream fileInputStream = openFileInput("languageFile");
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            if ((selectedLanguageTemp = bufferedReader.readLine()) != null) {
                SettingsFragment.selectedLanguage = selectedLanguageTemp;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setLanguageForApp(){

        Locale locale;

        if(SettingsFragment.selectedLanguage == null){
            SettingsFragment.selectedLanguage = "not-set";
        }
        if(SettingsFragment.selectedLanguage.equals("not-set")){ //use any value for default
            locale = defaultDeviceLocale;
        }
        else {
            locale = new Locale(SettingsFragment.selectedLanguage);
        }
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
    }

}