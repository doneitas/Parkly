package com.example.parkly.DataBase.Tables;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.sql.Time;
import java.util.Date;

/**
 * Created by Giedrius on 2018-03-12.
 */

@Entity(tableName = "Payments")
public class Payment {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "date")
    private Date date;

    @ColumnInfo(name = "address")
    private String address;

    @ColumnInfo(name = "zone")
    private char zone;

    @ColumnInfo(name = "duration")
    private Time duration;

    @ColumnInfo(name = "price")
    private double price;

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

    public void setDate(Date date)
    {
        this.date = date;
    }

    public Date getDate() { return date; }

    public void setDuration(Time duration)
    {
        this.duration = duration;
    }

    public Time getDuration()
    {
        return duration;
    }

    public void setPrice(double price)
    {
        this.price = price;
    }

    public double getPrice()
    {
        return price;
    }
}
