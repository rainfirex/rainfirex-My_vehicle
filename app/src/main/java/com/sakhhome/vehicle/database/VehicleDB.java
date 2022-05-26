package com.sakhhome.vehicle.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class VehicleDB extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;

    private final static String DATABASE = "vehicle_helper";

    public VehicleDB(@Nullable Context context) {
        super(context, DATABASE, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createQuery = String.format("create table %s (id integer primary key autoincrement, %s TEXT, %s TEXT, %s TEXT, %s INTEGER, %s TEXT NULL, %s INTEGER DEFAULT 0, %s BLOB NULL," +
                        " %s TEXT NULL, %s REAL NULL, %s TEXT NULL, %s REAL DEFAULT 0, %s INTEGER DEFAULT 0);",
                TableVehicle.TABLE, TableVehicle.KEY_TITLE, TableVehicle.KEY_MARK, TableVehicle.KEY_MODEL, TableVehicle.KEY_YEAR,
                TableVehicle.KEY_COLOR, TableVehicle.KEY_ODOMETR, TableVehicle.KEY_AVATAR,
                TableVehicle.KEY_ENGINE, TableVehicle.KEY_POWER_ENGINE, TableVehicle.KEY_BODY, TableVehicle.KEY_TANK_LITERS, TableVehicle.KEY_MASS);

        sqLiteDatabase.execSQL(createQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        String dropQuery = String.format("DROP TABLE IF EXISTS %s", TableVehicle.TABLE);
        sqLiteDatabase.execSQL(dropQuery);

//        if (newVersion > oldVersion){
//            String queryUpgrade = String.format("ALTER TABLE %s" +
//                            " ADD COLUMN %s TEXT NULL, ADD COLUMN %s INTEGER NULL, ADD COLUMN %s TEXT NULL, ADD COLUMN %s INTEGER NULL, ADD COLUMN %s INTEGER NULL;",
//                    TableVehicle.TABLE, TableVehicle.KEY_ENGINE, TableVehicle.KEY_POWER_ENGINE, TableVehicle.KEY_BODY, TableVehicle.KEY_TANK_LITERS, TableVehicle.KEY_MASS);

//            String queryUpgrade = String.format("ALTER TABLE %s ADD COLUMN %s TEXT NULL DEFAULT '';", TableVehicle.TABLE, TableVehicle.KEY_ENGINE);
//            sqLiteDatabase.execSQL(queryUpgrade);
//            queryUpgrade = String.format("ALTER TABLE %s ADD COLUMN %s INTEGER DEFAULT 0;", TableVehicle.TABLE, TableVehicle.KEY_POWER_ENGINE);
//            sqLiteDatabase.execSQL(queryUpgrade);
//            queryUpgrade = String.format("ALTER TABLE %s ADD COLUMN %s TEXT NULL DEFAULT '';", TableVehicle.TABLE, TableVehicle.KEY_BODY);
//            sqLiteDatabase.execSQL(queryUpgrade);
//            queryUpgrade = String.format("ALTER TABLE %s ADD COLUMN %s INTEGER DEFAULT 0;", TableVehicle.TABLE, TableVehicle.KEY_TANK_LITERS);
//            sqLiteDatabase.execSQL(queryUpgrade);
//            queryUpgrade = String.format("ALTER TABLE %s ADD COLUMN %s INTEGER DEFAULT 0;", TableVehicle.TABLE, TableVehicle.KEY_MASS);
//            sqLiteDatabase.execSQL(queryUpgrade);
//        }

        onCreate(sqLiteDatabase);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
    }
}
