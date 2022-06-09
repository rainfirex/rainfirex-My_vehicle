package com.sakhhome.vehicle.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.util.Log;

import com.sakhhome.vehicle.database.TableOsago;
import com.sakhhome.vehicle.database.VehicleDB;
import com.sakhhome.vehicle.utils.DbBitmapUtility;

import java.io.Serializable;

public class Osago extends TableOsago implements Serializable {
    private int id;
    private String dateReg;
    private String dateEnd;
    private Bitmap osago;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDateReg() {
        return dateReg;
    }

    public void setDateReg(String dateReg) {
        this.dateReg = dateReg;
    }

    public String getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(String dateEnd) {
        this.dateEnd = dateEnd;
    }

    public Bitmap getOsago() {
        return osago;
    }

    public void setOsago(Bitmap osago) {
        this.osago = osago;
    }

    public Osago(int id, String dateReg, String dateEnd, Bitmap osago) {
        this.id = id;
        this.dateReg = dateReg;
        this.dateEnd = dateEnd;
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
        cv.put(KEY_DATE_REG,  this.dateReg);
        cv.put(KEY_DATE_END,  this.dateEnd);
        cv.put(KEY_OSAGO_IMG, osagoByte);

        int count = db.update(TABLE, cv, "id = ?", new String[]{ String.valueOf(id) });

        db.close();

        return count;
    }
}
