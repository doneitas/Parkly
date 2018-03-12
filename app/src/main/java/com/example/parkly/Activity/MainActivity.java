package com.example.parkly.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.parkly.DataBase.LicensePlate;
import com.example.parkly.DataBase.LicensePlateDatabase;
import com.example.parkly.DataBase.LicensePlateRepository;
import com.example.parkly.DataBase.LocalUserDataSource;
import com.example.parkly.Fragment.CarsFragment;
import com.example.parkly.Fragment.HomeFragment;
import com.example.parkly.R;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    public static boolean isCurrentFragmentIsHomeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadFragment(new HomeFragment(), "HOME_FRAGMENT");
        navigation();
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
                loadFragment(new HomeFragment(), "HOME_FRAGMENT");
        }
        else super.onBackPressed();
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
            isCurrentFragmentIsHomeFragment = true;
        } else if (id == R.id.nav_cars) {
            selectedFragment = new CarsFragment();
            fragmentTag = "CARS_FRAGMENT";
        } else if (id == R.id.nav_parking) {

        } else if (id == R.id.nav_settings) {

        } else if (id == R.id.nav_aboutUs) {

        }

        Fragment currentFragment = getSupportFragmentManager().findFragmentByTag(fragmentTag);

        if (selectedFragment != null && currentFragment == null){
            loadFragment(selectedFragment, fragmentTag);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}