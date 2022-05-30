package com.sakhhome.vehicle.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.sakhhome.vehicle.models.ReFuel;

import java.util.ArrayList;
import java.util.List;

public abstract class TableReFuel {
    public final static String TABLE = "refuel";

    public final static String KEY_ID = "id";
    public final static String KEY_VEHICLE_ID = "vehicle_id";
    public final static String KEY_DATE = "date";
    public final static String KEY_ODOMETR = "odometr";
    public final static String KEY_TYPE = "type";
    public final static String KEY_LITER = "liter";
    public final static String KEY_PRICE = "price";
    public final static String KEY_SUM = "sum";
    public final static String KEY_ADDRESS = "address";
    public final static String KEY_STATION = "station";
    public final static String KEY_NOTE = "note";

    public static String createTableString(){
        return "create table "+TABLE+" (id integer primary key autoincrement, "+KEY_DATE+" TEXT, "
                +KEY_ODOMETR+" INTEGER DEFAULT 0, "+KEY_TYPE+" TEXT, "+KEY_LITER+" INTEGER DEFAULT 0, "+KEY_PRICE+ " REAL DEFAULT 0, "
                +KEY_SUM+" REAL DEFAULT 0, "
                +KEY_ADDRESS+" TEXT, "+KEY_STATION+" TEXT, "+KEY_NOTE+" TEXT,"
                +KEY_VEHICLE_ID+ " INTEGER NOT NULL, FOREIGN KEY ("+KEY_VEHICLE_ID+") REFERENCES "+TableVehicle.TABLE+"("+TableVehicle.KEY_ID+"));";
    }

    public static ReFuel create(Context context, String date, int odometr, String type, int litres, Double fuelPrice, Double sum, String address, String station, String note, int vehicleId){
        VehicleDB vehicleDB = new VehicleDB(context);
        SQLiteDatabase db = vehicleDB.getWritableDatabase();

        ContentValues cv = new  ContentValues();
        cv.put(KEY_DATE, date);
        cv.put(KEY_ODOMETR, odometr);
        cv.put(KEY_TYPE, type);
        cv.put(KEY_LITER, litres);
        cv.put(KEY_PRICE, fuelPrice);
        cv.put(KEY_SUM, sum);
        cv.put(KEY_ADDRESS, address);
        cv.put(KEY_STATION, station);
        cv.put(KEY_NOTE, note);
        cv.put(KEY_VEHICLE_ID, vehicleId);

        long id = db.insert(TABLE, null, cv);

        db.close();

        if (id > 0){
            return new ReFuel((int)id, date, odometr, type, litres, fuelPrice, sum, address, station, note);
        }
        return null;
    }

    public static List<ReFuel> gets(Context context, int vehicleId) {
        List<ReFuel> list = new ArrayList<>();

        VehicleDB vehicleDB = new VehicleDB(context);
        SQLiteDatabase db = vehicleDB.getWritableDatabase();

        Cursor c;
        if (vehicleId > 0){
            c = db.query(TABLE, new String[]{"*"}, KEY_VEHICLE_ID + " = ?", new String[]{String.valueOf(vehicleId)}, null,null, null);
        }
        else{
            c = db.query(TABLE, new String[]{"*"}, null, null, null,null, null);
        }

        if (c.moveToNext()){
            int idIndex = c.getColumnIndex(KEY_ID);
            int dateIndex = c.getColumnIndex(KEY_DATE);
            int odometrIndex = c.getColumnIndex(KEY_ODOMETR);
            int typeIndex = c.getColumnIndex(KEY_TYPE);
            int literIndex = c.getColumnIndex(KEY_LITER);
            int priceIndex = c.getColumnIndex(KEY_PRICE);
            int sumIndex = c.getColumnIndex(KEY_SUM);
            int addressIndex = c.getColumnIndex(KEY_ADDRESS);
            int stationIndex = c.getColumnIndex(KEY_STATION);
            int noteIndex = c.getColumnIndex(KEY_NOTE);

            do {
                int id         = c.getInt(idIndex);
                String date    = c.getString(dateIndex);
                int odometr    = c.getInt(odometrIndex);
                String type    = c.getString(typeIndex);
                int liter      = c.getInt(literIndex);
                Double price   = c.getDouble(priceIndex);
                Double sum     = c.getDouble(sumIndex);
                String address = c.getString(addressIndex);
                String station = c.getString(stationIndex);
                String note    = c.getString(noteIndex);

                list.add(new ReFuel(id, date, odometr, type, liter, price, sum, address, station, note));
            }
            while (c.moveToNext());
        }

        db.close();

        return list;
    }
}
