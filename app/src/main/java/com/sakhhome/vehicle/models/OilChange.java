package com.sakhhome.vehicle.models;

import com.sakhhome.vehicle.database.TableOilChange;

import java.io.Serializable;

public class OilChange extends TableOilChange implements Serializable {

    private int id;
    private String date;
    private int odometr;
    private String oilName;
    private int oilLiter;
    private Double oilPrice;
    private String oilFilterName;
    private Double oilFilterPrice;
    private String address;
    private String station;
    private String airFilterName;
    private Double airFilterPrice;
    private Double workPrice;



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

    public String getOilFilterName() {
        return oilFilterName;
    }

    public void setOilFilterName(String oilFilterName) {
        this.oilFilterName = oilFilterName;
    }

    public Double getOilFilterPrice() {
        return oilFilterPrice;
    }

    public void setOilFilterPrice(Double oilFilterPrice) {
        this.oilFilterPrice = oilFilterPrice;
    }

    public Double getWorkPrice() {
        return workPrice;
    }

    public void setWorkPrice(Double workPrice) {
        this.workPrice = workPrice;
    }

    public OilChange(int id, String date, int odometr, String oilName, int oilLiter, Double oilPrice,
                     String address, String station, String airFilterName, Double airFilterPrice, String oilFilterName, Double oilFilterPrice, Double WorkPrice) {
        this.id       = id;
        this.date     = date;
        this.odometr  = odometr;
        this.oilName  = oilName;
        this.oilLiter = oilLiter;
        this.oilPrice = oilPrice;
        this.address  = address;
        this.station  = station;
        this.airFilterName  = airFilterName;
        this.airFilterPrice = airFilterPrice;
        this.oilFilterName  = oilFilterName;
        this.oilFilterPrice = oilFilterPrice;
        this.workPrice      = WorkPrice;
    }
}
