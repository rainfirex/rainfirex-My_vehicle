package com.sakhhome.vehicle.models;

import com.sakhhome.vehicle.database.TableReFuel;

import java.io.Serializable;

public class ReFuel extends TableReFuel implements Serializable {
    private int id;
    private String date;
    private int odometr;
    private String type;
    private int fuelLiter;
    private Double fuelPrice;
    private Double sum;
    private String address;
    private String station;
    private String note;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getFuelLiter() {
        return fuelLiter;
    }

    public void setFuelLiter(int fuelLiter) {
        this.fuelLiter = fuelLiter;
    }

    public Double getFuelPrice() {
        return fuelPrice;
    }

    public void setFuelPrice(Double fuelPrice) {
        this.fuelPrice = fuelPrice;
    }

    public Double getSum() {
        return sum;
    }

    public void setSum(Double sum) {
        this.sum = sum;
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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public ReFuel(int id, String date, int odometr, String type, int fuelLiter, Double fuelPrice, Double sum, String address, String station, String note) {
        this.id        = id;
        this.date      = date;
        this.odometr   = odometr;
        this.type      = type;
        this.fuelLiter = fuelLiter;
        this.fuelPrice = fuelPrice;
        this.sum       = sum;
        this.address   = address;
        this.station   = station;
        this.note      = note;
    }
}
