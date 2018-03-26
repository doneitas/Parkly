package com.example.parkly.DataBase;

import android.arch.persistence.room.*;
import android.content.Context;

import com.example.parkly.DataBase.Tables.LicensePlate;

import java.util.List;

import io.reactivex.Flowable;

import static com.example.parkly.DataBase.LicensePlateDatabase.DATABASE_VERSION;

/**
 * Created by Giedrius on 2018-03-04.
 */

@Database(entities = LicensePlate.class, version = DATABASE_VERSION)
public abstract class LicensePlateDatabase extends RoomDatabase {

    static final int DATABASE_VERSION = 3;
    private static final String DATABASE_NAME = "Database";
    public abstract LicensePlateDao licensePlateDao();
    private static LicensePlateDatabase mInstance;



    public static LicensePlateDatabase getInstance(Context context)
    {
        if (mInstance == null) {
            mInstance = Room.databaseBuilder(context, LicensePlateDatabase.class, DATABASE_NAME)
            .fallbackToDestructiveMigration().build();
        }
        return mInstance;
    }
}

@Dao
interface LicensePlateDao {
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



