package com.sakhhome.vehicle.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;

import com.sakhhome.vehicle.database.TableOsago;
import com.sakhhome.vehicle.database.TableVehicle;
import com.sakhhome.vehicle.database.VehicleDB;
import com.sakhhome.vehicle.utils.DbBitmapUtility;

import java.io.Serializable;
import java.lang.reflect.Array;

public class Vehicle extends TableVehicle implements Serializable {

    private int id;
    private String title;
    private String mark;
    private String model;
    private int year;
    private String color;
    private int odometr;
    private Bitmap avatar;
    private String engine;
    private double powerEngine;
    private String body;
    private int tankLiters;
    private int mass;
    private Osago osago;
    private int oilEngineLiters;

    public String getEngine() {
        return engine;
    }

    public void setEngine(String engine) {
        this.engine = engine;
    }

    public double getPowerEngine() {
        return powerEngine;
    }

    public void setPowerEngine(double powerEngine) {
        this.powerEngine = powerEngine;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getTankLiters() {
        return tankLiters;
    }

    public void setTankLiters(int tankLiters) {
        this.tankLiters = tankLiters;
    }

    public int getMass() {
        return mass;
    }

    public void setMass(int mass) {
        this.mass = mass;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getOdometr() {
        return odometr;
    }

    public void setOdometr(int odometr) {
        this.odometr = odometr;
    }

    public Bitmap getAvatar() {
        return avatar;
    }

    public void setAvatar(Bitmap avatar) {
        this.avatar = avatar;
    }

    public Osago getOsago() {
        return osago;
    }

    public void setOsago(Osago osago) {
        this.osago = osago;
    }

    public Vehicle(int id, String title, String mark, String model, int year, String color, int odometr, Bitmap avatar,
                   String engine, double powerEngine, String body, int tankLiters, int mass) {
        this.id      = id;
        this.title   = title;
        this.mark    = mark;
        this.model   = model;
        this.year    = year;
        this.color   = color;
        this.odometr = odometr;
        this.avatar  = avatar;
        this.engine  = engine;
        this.powerEngine = powerEngine;
        this.body        = body;
        this.tankLiters  = tankLiters;
        this.mass        = mass;
    }

    /**
     * Сохранить
     * @param context
     * @return
     */
    public int save(Context context){
        VehicleDB vehicleDB = new VehicleDB(context);
        SQLiteDatabase db = vehicleDB.getWritableDatabase();

        byte[] avatarByte = (avatar != null) ? DbBitmapUtility.getBytes(avatar) : null;

        ContentValues cv = new ContentValues();
        cv.put(KEY_TITLE, this.title);
        cv.put(KEY_MARK, this.mark);
        cv.put(KEY_MODEL, this.model);
        cv.put(KEY_YEAR, this.year);
        cv.put(KEY_COLOR, this.color);
        cv.put(KEY_ODOMETR, this.odometr);
        cv.put(KEY_AVATAR, avatarByte);
        cv.put(KEY_ENGINE, this.engine);
        cv.put(KEY_POWER_ENGINE, this.powerEngine);
        cv.put(KEY_BODY, this.body);
        cv.put(KEY_TANK_LITERS, this.tankLiters);
        cv.put(KEY_MASS, this.mass);

        return db.update(TABLE, cv, "id = ?", new String[]{ String.valueOf(id) });
    }

    public Osago loadOsago(Context context){
        VehicleDB vehicleDB = new VehicleDB(context);
        SQLiteDatabase db = vehicleDB.getWritableDatabase();

        Osago osago = null;

        Cursor c = db.query(TableOsago.TABLE, new String[]{"*"}, TableOsago.KEY_VEHICLE_ID + " = ?", new String[]{String.valueOf(id)}, null,null, null);

        if(c.moveToFirst()){
            int idIndex      = c.getColumnIndex(TableOsago.KEY_ID);
            int dateRegIndex = c.getColumnIndex(TableOsago.KEY_DATE_REG);
            int dateEndIndex = c.getColumnIndex(TableOsago.KEY_DATE_END);
            int osagoIndex   = c.getColumnIndex(TableOsago.KEY_OSAGO_IMG);

            int osagoId    = c.getInt(idIndex);
            String dateReg = c.getString(dateRegIndex);
            String dateEnd = c.getString(dateEndIndex);
            byte[] osagoByte   = c.getBlob(osagoIndex);

            Bitmap osagoImg   = (osagoByte != null) ? DbBitmapUtility.getImage(osagoByte) : null;

            db.close();

            osago = new Osago(osagoId, dateReg, dateEnd, osagoImg);
        }

        return osago;
    }

    public void with(Context context, String[] models){
        for (String item: models) {
            switch (item){
                case TableOsago.TABLE:
                    osago = loadOsago(context);
                    break;
            }
        }
    }
}
