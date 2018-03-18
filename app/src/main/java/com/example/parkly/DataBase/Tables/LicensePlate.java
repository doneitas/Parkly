package com.example.parkly.DataBase.Tables;

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

    @ColumnInfo(name = "current")
    private boolean current = false;

    public int getId() {return id;}

    public void setId(int id) {this.id = id;}

    public String getNumber() {return number;}

    public void setNumber(String number) {this.number = number;}

    public void setCurrent(boolean state) {this.current = state;}

    public boolean getCurrent() {return current;}
}

