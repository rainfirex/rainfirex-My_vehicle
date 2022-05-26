package com.sakhhome.vehicle.utils;

import android.database.CursorWindow;

import java.lang.reflect.Field;

public class Utils {
    /**
     * Расширение размера Cursor для выборки больших данных
     * @param isDebug
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
}
