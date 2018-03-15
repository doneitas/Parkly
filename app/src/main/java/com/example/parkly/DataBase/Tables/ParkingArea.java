package com.example.parkly.DataBase.Tables;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by Giedrius on 2018-03-12.
 */

@Entity(tableName = "ParkingAreas")
public class ParkingArea {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "address")
    private String address;

    @ColumnInfo(name = "zone")
    private char zone;

    public int getId() {
        return id;
    }

    public void setId(int id) {this.id = id;}

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {this.address = address;}

    public void setZone(char zone)
    {
        this.zone = zone;
    }

    public char getZone()
    {
        return zone;
    }
}
