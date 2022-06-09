package com.sakhhome.vehicle.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class ConnectDBTest {
    static SQLiteDatabase db;

    public static void conn(Context context){
        VehicleDB vehicleDB = new VehicleDB(context);
        if (db == null)
            db = vehicleDB.getWritableDatabase();
    }

    public static void close(){
        if(db.isOpen()){
            db.close();
        }
    }
}
