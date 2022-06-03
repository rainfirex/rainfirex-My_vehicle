package com.sakhhome.vehicle.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;

import com.sakhhome.vehicle.models.Osago;
import com.sakhhome.vehicle.models.Vehicle;
import com.sakhhome.vehicle.utils.DbBitmapUtility;

public abstract class TableOsago {
    public final static String TABLE = "osago";

    public final static String KEY_ID = "id";
    public final static String KEY_VEHICLE_ID = "vehicle_id";
    public final static String KEY_DATE_REG = "date_reg";
    public final static String KEY_DATE_END = "date_end";
    public final static String KEY_OSAGO_IMG = "osago";

    public static String createTableString(){
        return "create table "+TABLE+" (id integer primary key autoincrement, "+KEY_DATE_REG+" TEXT, "+ KEY_DATE_END+" TEXT, "
                +KEY_OSAGO_IMG+" BLOB NULL, "
                +KEY_VEHICLE_ID+ " INTEGER NOT NULL, FOREIGN KEY ("+KEY_VEHICLE_ID+") REFERENCES "+TableVehicle.TABLE+"("+TableVehicle.KEY_ID+"));";
    }

    public static Osago create(Context context, String dateReg, String dateEnd, Bitmap osago, int vehicleId ){
        VehicleDB vehicleDB = new VehicleDB(context);
        SQLiteDatabase db = vehicleDB.getWritableDatabase();

        byte[] osagoByte = (osago != null) ? DbBitmapUtility.getBytes(osago) : null;

        ContentValues cv = new  ContentValues();
        cv.put(KEY_DATE_REG, dateReg);
        cv.put(KEY_DATE_END, dateEnd);
        cv.put(KEY_OSAGO_IMG, osagoByte);
        cv.put(KEY_VEHICLE_ID, vehicleId);

        long id = db.insert(TABLE, null, cv);

        db.close();

        if(id > 0){
            return new Osago((int)id, dateReg, dateEnd, osago);
        }
        else
            return null;
    }

    public static Osago get(Context context, int id){
        Osago osago = null;

        VehicleDB vehicleDB = new VehicleDB(context);
        SQLiteDatabase db = vehicleDB.getWritableDatabase();

        Cursor c = db.query(TABLE, new String[]{"*"}, KEY_ID + " = ?", new String[]{String.valueOf(id)}, null,null, null);
        if(c.moveToFirst()){
            int dateRegIndex = c.getColumnIndex(KEY_DATE_REG);
            int dateEndIndex = c.getColumnIndex(KEY_DATE_END);
            int osagoIndex   = c.getColumnIndex(KEY_OSAGO_IMG);


            String dateReg = c.getString(dateRegIndex);
            String dateEnd = c.getString(dateEndIndex);
            byte[] osagoByte   = c.getBlob(osagoIndex);

            Bitmap osagoImg   = (osagoByte != null) ? DbBitmapUtility.getImage(osagoByte) : null;

            db.close();

            osago = new Osago(id, dateReg, dateEnd, osagoImg);
        }

        return osago;
    }
}
