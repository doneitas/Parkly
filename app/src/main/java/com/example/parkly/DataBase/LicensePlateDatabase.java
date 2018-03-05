package com.example.parkly.DataBase;

import android.arch.persistence.room.*;
import android.content.Context;

import java.util.List;

import io.reactivex.Flowable;

import static com.example.parkly.DataBase.LicensePlateDatabase.DATABASE_VERSION;

/**
 * Created by Giedrius on 2018-03-04.
 */

@Database(entities = LicensePlate.class, version = DATABASE_VERSION)
public abstract class LicensePlateDatabase extends RoomDatabase {

    static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "LicensePlateDataBase";
    public abstract LicensePlateDao licensePlateDao();
    private static LicensePlateDatabase mLPDataBase;

    public static LicensePlateDatabase getInstance(Context context)
    {
        if (mLPDataBase == null) {
            mLPDataBase = Room.databaseBuilder(context, LicensePlateDatabase.class, DATABASE_NAME)
            .fallbackToDestructiveMigration().build();
            //mLPDataBase = Room.databaseBuilder(context, LicensePlateDatabase.class, "Sample.db").addMigrations(MIGRATION_1_2).build();
        }
        return mLPDataBase;
    }
/*
    @Before
    public void initDb() throws Exception {
        database = Room.inMemoryDatabaseBuilder(
                InstrumentationRegistry.getContext(),
                UsersDatabase.class)
                .build();
    }

    public LicensePlateDatabase getDatabase() {
        return database;
    }

    @After
    public void closeDb() throws Exception {
        mDatabase.close();
    }

    @Test
    public void insertAndGetUser() {
        // When inserting a new user in the data source
        mDatabase.userDao().insertUser(USER);

        //The user can be retrieved
        List<User> users = mDatabase.userDao().getUsers();
        assertThat(users.size(), is(1));
        User dbUser = users.get(0);
        assertEquals(dbUser.getId(), USER.getId());
        assertEquals(dbUser.getUserName(), USER.getUserName());
    }

        static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabasese dataBase) {
// Since we didn't alter the table, there's nothing else to do here.
        }
    };

    */
}

@Dao
interface LicensePlateDao {
    @Query("SELECT * FROM LicensePlates")
    Flowable<List<LicensePlate>> getAll();

    @Query("SELECT * FROM LicensePlates WHERE id IN (:licensePlateIds)")
    Flowable<LicensePlate> loadAllByIds(int[] licensePlateIds);

    //@Query("SELECT * FROM LicensePlates WHERE number LIMIT 1")
    //LicensePlate findByNumber(String number);

    @Insert
    void insertAll(LicensePlate... numbers);

    @Delete
    void delete(LicensePlate number);

}



