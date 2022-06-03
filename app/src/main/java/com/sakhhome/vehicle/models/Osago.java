package com.sakhhome.vehicle.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;

import com.sakhhome.vehicle.database.TableOsago;
import com.sakhhome.vehicle.database.VehicleDB;
import com.sakhhome.vehicle.utils.DbBitmapUtility;

import java.io.Serializable;

public class Osago extends TableOsago implements Serializable {
    private int id;
    private String date_reg;
    private String date_end;
    private Bitmap osago;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate_reg() {
        return date_reg;
    }

    public void setDate_reg(String date_reg) {
        this.date_reg = date_reg;
    }

    public String getDate_end() {
        return date_end;
    }

    public void setDate_end(String date_end) {
        this.date_end = date_end;
    }

    public Bitmap getOsago() {
        return osago;
    }

    public void setOsago(Bitmap osago) {
        this.osago = osago;
    }

    public Osago(int id, String date_reg, String date_end, Bitmap osago) {
        this.id = id;
        this.date_reg = date_reg;
        this.date_end = date_end;
        this.osago = osago;
    }

    /**
     * Сохранить
     * @param context
     * @return int
     */
    public int save(Context context){
        VehicleDB vehicleDB = new VehicleDB(context);
        SQLiteDatabase db = vehicleDB.getWritableDatabase();

        byte[] osagoByte = (osago != null) ? DbBitmapUtility.getBytes(osago) : null;

        ContentValues cv = new ContentValues();
        cv.put(KEY_DATE_REG,    this.date_reg);
        cv.put(KEY_DATE_END,   this.date_end);
        cv.put(KEY_OSAGO_IMG,  osagoByte);

        return db.update(TABLE, cv, "id = ?", new String[]{ String.valueOf(id) });
    }
}
