package com.sakhhome.vehicle.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.sakhhome.vehicle.models.OilChange;

import java.util.ArrayList;
import java.util.List;

public abstract class TableOilChange {
    public final static String TABLE = "oil_change";

    public final static String KEY_ID = "id";
    public final static String KEY_VEHICLE_ID = "vehicle_id";
    public final static String KEY_DATE = "date";
    public final static String KEY_ODOMETR = "odometr";
    public final static String KEY_OIL_NAME = "oil_name";
    public final static String KEY_OIL_LITER = "oil_liter";
    public final static String KEY_OIL_PRICE = "oil_price";
    public final static String KEY_ADDRESS = "address";
    public final static String KEY_STATION = "station";
    public final static String KEY_AIR_FILTER_NAME = "air_filter_name";
    public final static String KEY_AIR_FILTER_PRICE = "air_filter_price";

    public static String createTableString(){
        return "create table "+TABLE+" (id integer primary key autoincrement, "+KEY_DATE+" TEXT, "
                +KEY_ODOMETR+" INTEGER DEFAULT 0, "+KEY_OIL_NAME+" TEXT, "+KEY_OIL_LITER+" INTEGER DEFAULT 0, "+KEY_OIL_PRICE+ " REAL DEFAULT 0, "
                +KEY_ADDRESS+" TEXT, "+KEY_STATION+" TEXT, "+KEY_AIR_FILTER_NAME+ " TEXT, "+KEY_AIR_FILTER_PRICE+" REAL DEFAULT 0, "
                +KEY_VEHICLE_ID+ " INTEGER NOT NULL, FOREIGN KEY ("+KEY_VEHICLE_ID+") REFERENCES "+TableVehicle.TABLE+"("+TableVehicle.KEY_ID+"));";
    }

    public static OilChange create(Context context, String date, int odometr, String oilName, int oilLiter, Double oilPrice,
                                   String address, String station, String airFilterName, Double airFilterPrice, int vehicleId){
        VehicleDB vehicleDB = new VehicleDB(context);
        SQLiteDatabase db = vehicleDB.getWritableDatabase();

        ContentValues cv = new  ContentValues();
        cv.put(KEY_DATE, date);
        cv.put(KEY_ODOMETR, odometr);
        cv.put(KEY_OIL_NAME, oilName);
        cv.put(KEY_OIL_LITER, oilLiter);
        cv.put(KEY_OIL_PRICE, oilPrice);
        cv.put(KEY_ADDRESS, address);
        cv.put(KEY_STATION, station);
        cv.put(KEY_AIR_FILTER_NAME, airFilterName);
        cv.put(KEY_AIR_FILTER_PRICE, airFilterPrice);
        cv.put(KEY_VEHICLE_ID, vehicleId);

        long id = db.insert(TABLE, null, cv);

        db.close();

        if (id > 0){
            return new OilChange((int)id, date, odometr, oilName, oilLiter, oilPrice, address, station, airFilterName, airFilterPrice);
        }
        return null;
    }

    public static List<OilChange> gets(Context context, int vehicleId) {
        List<OilChange> list = new ArrayList<>();

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
            int oilNameIndex = c.getColumnIndex(KEY_OIL_NAME);
            int oilLiterIndex = c.getColumnIndex(KEY_OIL_LITER);
            int oilPriceIndex = c.getColumnIndex(KEY_OIL_PRICE);
            int addressIndex = c.getColumnIndex(KEY_ADDRESS);
            int stationIndex = c.getColumnIndex(KEY_STATION);
            int airFilterNameIndex = c.getColumnIndex(KEY_AIR_FILTER_NAME);
            int airFilterPriceIndex = c.getColumnIndex(KEY_AIR_FILTER_PRICE);
            do {
                int id = c.getInt(idIndex);
                String date = c.getString(dateIndex);
                int odometr = c.getInt(odometrIndex);
                String oilName = c.getString(oilNameIndex);
                int oilLiter   = c.getInt(oilLiterIndex);
                Double oilPrice = c.getDouble(oilPriceIndex);
                String address  = c.getString(addressIndex);
                String station  = c.getString(stationIndex);
                String airFilterName = c.getString(airFilterNameIndex);
                Double airFilterPrice = c.getDouble(airFilterPriceIndex);

                list.add(new OilChange(id, date, odometr, oilName, oilLiter, oilPrice, address, station, airFilterName, airFilterPrice));
            }
            while (c.moveToNext());
        }

        db.close();

        return list;
    }
}
