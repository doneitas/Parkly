package com.example.parkly.DataBase;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "LicensePlates")
 public class LicensePlate {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "number")
    private String number;
/*
    public LicensePlate(int id, String number) {
        this.id = id;
        this.number = number;
    }
*/
    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {this.number = number;}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

