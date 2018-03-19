package com.example.parkly.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.parkly.Activity.addCarActivity;
import com.example.parkly.DataBase.Tables.LicensePlate;
import com.example.parkly.DataBase.LicensePlateDatabase;
import com.example.parkly.DataBase.LicensePlateRepository;
import com.example.parkly.DataBase.LocalUserDataSource;
import com.example.parkly.R;

import java.util.ArrayList;
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_fragment, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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

    private void createTimeZoneLists()
    {
        ListView listZones;
        ListView listTime;
        List<String> zones = new ArrayList<String>();
        zones.add("Orange zone");
        zones.add("Yellow zone");
        zones.add("Red zone");
        List<Integer> time = new ArrayList<Integer>();
        time.add(15);
        time.add(30);
        time.add(45);
        time.add(60);
        time.add(75);
        time.add(90);

        listZones = (ListView)getView().findViewById(R.id.list_zones);
        listTime = (ListView)getView().findViewById(R.id.list_time);
        ArrayAdapter<String> zonesAdapter = new ArrayAdapter<String>(getActivity(), zones);
        ArrayAdapter<String> timeAdapter = new ArrayAdapter<String>(getActivity(), time);
    }

}
