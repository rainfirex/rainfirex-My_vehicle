package com.sakhhome.vehicle.models;

import android.content.Context;

import com.sakhhome.vehicle.database.TableOilChange;

import java.util.List;

public class OilChange extends TableOilChange {

    private int id;
    private String date;
    private int odometr;
    private String oilName;
    private int oilLiter;
    private Double oilPrice;
    private String address;
    private String station;
    private String airFilterName;
    private Double airFilterPrice;



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getOdometr() {
        return odometr;
    }

    public void setOdometr(int odometr) {
        this.odometr = odometr;
    }

    public String getOilName() {
        return oilName;
    }

    public void setOilName(String oilName) {
        this.oilName = oilName;
    }

    public int getOilLiter() {
        return oilLiter;
    }

    public void setOilLiter(int oilLiter) {
        this.oilLiter = oilLiter;
    }

    public Double getOilPrice() {
        return oilPrice;
    }

    public void setOilPrice(Double oilPrice) {
        this.oilPrice = oilPrice;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public String getAirFilterName() {
        return airFilterName;
    }

    public void setAirFilterName(String airFilterName) {
        this.airFilterName = airFilterName;
    }

    public Double getAirFilterPrice() {
        return airFilterPrice;
    }

    public void setAirFilterPrice(Double airFilterPrice) {
        this.airFilterPrice = airFilterPrice;
    }


    public OilChange(int id, String date, int odometr, String oilName, int oilLiter, Double oilPrice,
                     String address, String station, String airFilterName, Double airFilterPrice) {
        this.id = id;
        this.date = date;
        this.odometr = odometr;
        this.oilName = oilName;
        this.oilLiter = oilLiter;
        this.oilPrice = oilPrice;
        this.address = address;
        this.station = station;
        this.airFilterName = airFilterName;
        this.airFilterPrice = airFilterPrice;
    }
}
