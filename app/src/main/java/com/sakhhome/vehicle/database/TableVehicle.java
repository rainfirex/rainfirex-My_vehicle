package com.sakhhome.vehicle.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;

import com.sakhhome.vehicle.models.Vehicle;
import com.sakhhome.vehicle.utils.DbBitmapUtility;

import java.util.ArrayList;
import java.util.List;


//NULL: указывает фактически на отсутствие значения
//INTEGER: представляет целое число, которое может быть положительным и отрицательным и в зависимости от своего значения может занимать 1, 2, 3, 4, 6 или 8 байт
//REAL: представляет число с плавающей точкой, занимает 8 байт в памяти
//TEXT: строка текста в одинарных кавычках, которая сохраняется в кодировке базы данных (UTF-8, UTF-16BE или UTF-16LE)
//BLOB: бинарные данные

public abstract class TableVehicle {
    public final static String TABLE = "vehicle";

    public final static String KEY_ID = "id";
    public final static String KEY_TITLE = "title";
    public final static String KEY_MARK = "mark";
    public final static String KEY_MODEL = "model";
    public final static String KEY_YEAR = "year";
    public final static String KEY_COLOR = "color";
    public final static String KEY_ODOMETR = "odometr";
    public final static String KEY_AVATAR = "avatar";
    public final static String KEY_ENGINE = "engine";
    public final static String KEY_POWER_ENGINE = "power_engine";
    public final static String KEY_BODY = "body";
    public final static String KEY_TANK_LITERS = "tank_liters";
    public final static String KEY_MASS = "mass";

    /**
     * Получить весь транспорт из БД
     * @param context
     * @return
     */
    public static List<Vehicle> gets(Context context){
        List<Vehicle> list = new ArrayList<>();

        VehicleDB vehicleDB = new VehicleDB(context);
        SQLiteDatabase db = vehicleDB.getWritableDatabase();
        Cursor c = db.query(TableVehicle.TABLE, null, null, null, null,null, null);

        if (c.moveToFirst()){

            int idIndex      = c.getColumnIndex(TableVehicle.KEY_ID);
            int titleIndex   = c.getColumnIndex(TableVehicle.KEY_TITLE);
            int markIndex    = c.getColumnIndex(TableVehicle.KEY_MARK);
            int modelIndex   = c.getColumnIndex(TableVehicle.KEY_MODEL);
            int yearIndex    = c.getColumnIndex(TableVehicle.KEY_YEAR);
            int colorIndex   = c.getColumnIndex(TableVehicle.KEY_COLOR);
            int odometrIndex = c.getColumnIndex(TableVehicle.KEY_ODOMETR);
            int avatarIndex  = c.getColumnIndex(TableVehicle.KEY_AVATAR);
            int engineIndex  = c.getColumnIndex(TableVehicle.KEY_ENGINE);
            int enginePowerIndex  = c.getColumnIndex(TableVehicle.KEY_POWER_ENGINE);
            int bodyIndex  = c.getColumnIndex(TableVehicle.KEY_BODY);
            int tankIndex  = c.getColumnIndex(TableVehicle.KEY_TANK_LITERS);
            int massIndex  = c.getColumnIndex(TableVehicle.KEY_MASS);

            do{
                int id         = c.getInt(idIndex);
                String title   = c.getString(titleIndex);
                String mark    = c.getString(markIndex);
                String model   = c.getString(modelIndex);
                int year       = c.getInt(yearIndex);
                String color   = c.getString(colorIndex);
                int odometr    = c.getInt(odometrIndex);
                byte[] avatarByte  = c.getBlob(avatarIndex);
                Bitmap avatar = (avatarByte != null) ? DbBitmapUtility.getImage(avatarByte) : null;
                String engine   = c.getString(engineIndex);
                Double enginePower = c.getDouble(enginePowerIndex);
                String body     = c.getString(bodyIndex);
                Double tank = c.getDouble(tankIndex);
                int mass = c.getInt(massIndex);

                Vehicle vehicle = new Vehicle(id, title, mark, model, year, color, odometr, avatar, engine, enginePower, body, tank, mass);
                list.add(vehicle);
            }
            while (c.moveToNext());
        }
        db.close();

        return list;
    }

    /**
     * Получить транспорт по ID
     * @param context
     * @param id
     * @return
     */
    public static Vehicle get(Context context, int id){
        Vehicle vehicle = null;

        VehicleDB vehicleDB = new VehicleDB(context);
        SQLiteDatabase db = vehicleDB.getWritableDatabase();

        Cursor c = db.query(TableVehicle.TABLE, new String[]{"*"}, TableVehicle.KEY_ID + " = ?", new String[]{String.valueOf(id)}, null,null, null);
        if(c.moveToFirst()){
            int titleIndex   = c.getColumnIndex(TableVehicle.KEY_TITLE);
            int markIndex    = c.getColumnIndex(TableVehicle.KEY_MARK);
            int modelIndex   = c.getColumnIndex(TableVehicle.KEY_MODEL);
            int yearIndex    = c.getColumnIndex(TableVehicle.KEY_YEAR);
            int colorIndex   = c.getColumnIndex(TableVehicle.KEY_COLOR);
            int odometrIndex = c.getColumnIndex(TableVehicle.KEY_ODOMETR);
            int avatarIndex  = c.getColumnIndex(TableVehicle.KEY_AVATAR);
            int engineIndex  = c.getColumnIndex(TableVehicle.KEY_ENGINE);
            int enginePowerIndex  = c.getColumnIndex(TableVehicle.KEY_POWER_ENGINE);
            int bodyIndex  = c.getColumnIndex(TableVehicle.KEY_BODY);
            int tankIndex  = c.getColumnIndex(TableVehicle.KEY_TANK_LITERS);
            int massIndex  = c.getColumnIndex(TableVehicle.KEY_MASS);

            String title   = c.getString(titleIndex);
            String mark    = c.getString(markIndex);
            String model   = c.getString(modelIndex);
            int year       = c.getInt(yearIndex);
            String color   = c.getString(colorIndex);
            int odometr    = c.getInt(odometrIndex);
            byte[] avatarByte  = c.getBlob(avatarIndex);
            Bitmap avatar   = (avatarByte != null) ? DbBitmapUtility.getImage(avatarByte) : null;
            String engine   = c.getString(engineIndex);
            Double enginePower = c.getDouble(enginePowerIndex);
            String body     = c.getString(bodyIndex);
            Double tank = c.getDouble(tankIndex);
            int mass = c.getInt(massIndex);

            db.close();

            vehicle = new Vehicle(id, title, mark, model, year, color, odometr, avatar, engine, enginePower, body, tank, mass);
        }

        return vehicle;
    }

    /**
     * Добавить транспорт в БД
     * @param context
     * @param title
     * @param mark
     * @param model
     * @param year
     * @param color
     * @param odometr
     * @param avatar
     * @param engine
     * @param enginePower
     * @param body
     * @param tank
     * @param mass
     * @return
     */
    public static Vehicle create(Context context, String title, String mark, String model, int year, String color, int odometr, Bitmap avatar,
                                 String engine, Double enginePower, String body, Double tank, int mass){
        VehicleDB vehicleDB = new VehicleDB(context);
        SQLiteDatabase db = vehicleDB.getWritableDatabase();

        byte[] avatarByte = (avatar != null) ? DbBitmapUtility.getBytes(avatar) : null;

        ContentValues cv = new  ContentValues();
        cv.put(TableVehicle.KEY_TITLE, title);
        cv.put(TableVehicle.KEY_MARK, mark);
        cv.put(TableVehicle.KEY_MODEL, model);
        cv.put(TableVehicle.KEY_YEAR, year);
        cv.put(TableVehicle.KEY_COLOR, color);
        cv.put(TableVehicle.KEY_ODOMETR, odometr);
        cv.put(TableVehicle.KEY_AVATAR, avatarByte);
        cv.put(TableVehicle.KEY_ENGINE, engine);
        cv.put(TableVehicle.KEY_POWER_ENGINE, enginePower);
        cv.put(TableVehicle.KEY_BODY, body);
        cv.put(TableVehicle.KEY_TANK_LITERS, tank);
        cv.put(TableVehicle.KEY_MASS, mass);

        long id = db.insert(TableVehicle.TABLE, null, cv);

        db.close();

        if(id > 0){
            return new Vehicle((int)id, title, mark, model, year, color, odometr, avatar, engine, enginePower, body, tank, mass);
        }
        else
            return null;
    };

    /**
     * Удалить транспорт из БД
     * @param context
     * @param id
     * @return
     */
    public static int delete(Context context, int id){
        VehicleDB vehicleDB = new VehicleDB(context);
        SQLiteDatabase db = vehicleDB.getWritableDatabase();

        int count = db.delete(TableVehicle.TABLE, TableVehicle.KEY_ID + " = ?", new String[]{String.valueOf(id)});

        db.close();

        return count;
    }

    /**
     * Сохранить модель
     * @param context
     * @return
     */
    public abstract int save(Context context);

    public static int update(){
        return -1;
    }
}
