package com.example.parkly.DataBase;

import com.example.parkly.DataBase.Tables.LicensePlate;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by Giedrius on 2018-03-04.
 */

public class LocalUserDataSource implements LicensePlateDao{

    private LicensePlateDao licensePlateDao;
    private static LocalUserDataSource mInstance;

    private LocalUserDataSource(LicensePlateDao licensePlateDao)
    {
        this.licensePlateDao = licensePlateDao;
    }

    public static LocalUserDataSource getInstance(LicensePlateDao licensePlateDao)
    {
        if (mInstance == null)
        {
            mInstance = new LocalUserDataSource(licensePlateDao);
        }
        return mInstance;
    }

    @Override
    public Flowable<List<LicensePlate>> getAll() {
        return licensePlateDao.getAll();
    }

    @Override
    public Flowable<LicensePlate> loadAllByIds(int[] licensePlateIds) {
        return licensePlateDao.loadAllByIds(licensePlateIds);
    }

    @Override
    public void insertAll(LicensePlate... numbers) {
        licensePlateDao.insertAll(numbers);
    }

    @Override
    public void delete(LicensePlate number) {
        licensePlateDao.delete(number);
    }

    @Override
    public void clear() {
        licensePlateDao.clear();
    }
}
