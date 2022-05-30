package com.sakhhome.vehicle.utils;

import android.database.CursorWindow;

import java.lang.reflect.Field;
import java.util.Calendar;

public class Utils {

    /**
     * Расширение размера Cursor для выборки больших данных
     * @param boolean isDebug
     */
    public static void sCursorWindowSize(boolean isDebug){
        try {
            Field field = CursorWindow.class.getDeclaredField("sCursorWindowSize");
            field.setAccessible(true);
            field.set(null, 100 * 1024 * 1024); //the 100MB is the new size
        } catch (Exception e) {
            if (isDebug) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Получить текущую дату
     * @return String
     */
    public static String getCurrDate(){
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        int day = c.get(Calendar.DAY_OF_MONTH);
        return String.format("%s.%s.%s", addZero(day), addZero(month), year);
    }

    /**
     * Добавить 0
     * @param int number
     * @return String
     */
    public static String addZero(int number){
        if(number >= 1 && number <10){
            return String.format("0%s", number);
        }
        return String.valueOf(number);
    }
}
