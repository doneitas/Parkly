package com.example.parkly.DataBase;

import com.example.parkly.DataBase.Tables.LicensePlate;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by Giedrius on 2018-03-05.
 */

public class LicensePlateRepository implements LicensePlateDao{

    private LicensePlateDao mLicensePlateDao;

    private static LicensePlateRepository mInstance;

    private LicensePlateRepository(LicensePlateDao mLicensePlateDao)
    {
        this.mLicensePlateDao = mLicensePlateDao;
    }

    public static LicensePlateRepository getInstance(LicensePlateDao mLicensePlateDao)
    {
        if (mInstance == null)
        {
            mInstance = new LicensePlateRepository(mLicensePlateDao);
        }
        return mInstance;
    }

    @Override
    public Flowable<List<LicensePlate>> getAll() {
        return mLicensePlateDao.getAll();
    }

    @Override
    public Flowable<LicensePlate> loadAllByIds(int[] licensePlateIds) {
        return mLicensePlateDao.loadAllByIds(licensePlateIds);
    }

    @Override
    public LicensePlate findDefault() {
        return mLicensePlateDao.findDefault();
    }

    @Override
    public void insertAll(LicensePlate... numbers) {
        mLicensePlateDao.insertAll(numbers);
    }

    @Override
    public void updateLicensePlate(LicensePlate... numbers) {
        mLicensePlateDao.updateLicensePlate(numbers);
    }

    @Override
    public void delete(LicensePlate number) {
        mLicensePlateDao.delete(number);
    }

    @Override
    public void clear() {
        mLicensePlateDao.clear();
    }


}
