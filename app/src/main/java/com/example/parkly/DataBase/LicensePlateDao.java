package com.example.parkly.DataBase;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.parkly.DataBase.Tables.LicensePlate;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface LicensePlateDao {
    @Query("SELECT * FROM LicensePlates")
    Flowable<List<LicensePlate>> getAll();

    @Query("SELECT number FROM LicensePlates")
    Flowable<List<String>> getAllNumbers();

    @Query("SELECT * FROM LicensePlates WHERE id IN (:licensePlateIds)")
    Flowable<LicensePlate> loadAllByIds(int[] licensePlateIds);

    @Query("SELECT * FROM LicensePlates WHERE current ")
    LicensePlate findDefault();

    @Query("SELECT * FROM LicensePlates WHERE number IN (:lNumbers)")
    Flowable<List<LicensePlate>> findAllByNumber(List<String> lNumbers);

    @Insert
    void insertAll(LicensePlate... numbers);

    @Update
    void updateLicensePlate(LicensePlate... numbers);

    @Delete
    void delete(LicensePlate number);

    @Query("DELETE FROM LicensePlates")
    void clear();
}
