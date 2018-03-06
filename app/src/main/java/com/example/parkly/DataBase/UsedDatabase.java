package com.example.parkly.DataBase;

import android.widget.Toast;

import com.example.parkly.Cars;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Giedrius on 2018-03-06.
 */
/*
public static class UsedDatabase {

    //Database
    private static CompositeDisposable compositeDisposable  = LicensePlateDatabase.getInstance(this);
    public static LicensePlateRepository licensePlateRepository  = LicensePlateRepository.getInstance(LocalUserDataSource.getInstance(licensePlateDatabase.licensePlateDao()));
    //Init
    compositeDisposable = new CompositeDisposable();

    //Database
    //LicensePlateDatabase licensePlateDatabase
    //licensePlateRepository
    //loadData();


    public static void loadData()
    {
        Disposable disposable = licensePlateRepository.getAll()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<List<LicensePlate>>() {
                    @Override
                    public void accept(List<LicensePlate> licensePlates) throws Exception {
                        onGetAllLicensePlateSuccess(licensePlates);
                    }

                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(Cars.this, ""+throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        compositeDisposable.add(disposable);
    }

    private void onGetAllLicensePlateSuccess(List<LicensePlate> licensePlates)
    {
        licensePlateList.clear();
        licensePlateList.addAll(licensePlates);
        adapter.notifyDataSetChanged();
    }
}
*/