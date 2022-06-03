package com.sakhhome.vehicle.utils;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.CursorWindow;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.format.Formatter;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.Calendar;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;

public class Utils {

    /**
     * Расширение размера Cursor для выборки больших данных
     * @param isDebug boolean
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
     * @param number int
     * @return String
     */
    public static String addZero(int number){
        if(number >= 1 && number <10){
            return String.format("0%s", number);
        }
        return String.valueOf(number);
    }


    /**
     * Преобразование растрового изображения в указанный размер
     * @param bitmap
     * @param width
     * @param height
     * @return
     */
    public static Bitmap createBitmapBySize(Context context, Bitmap bitmap, int width, int height) {
        Log.d("SIZE","Image size original:" + Formatter.formatFileSize(context, bitmap.getByteCount()));

        int orig_height = bitmap.getHeight();
        int orig_width = bitmap.getWidth();

        int bitByte = bitmap.getByteCount();
        Log.d("SIZE", "byte:"+bitByte);

        if (width == 0 && height == 0) {
            if (bitmap.getByteCount() > 100844352) {
                orig_height = orig_height / 4;
                orig_width = orig_width / 4;
            } else {
                orig_height = orig_height / 2;
                orig_width = orig_width / 2;
            }
        }
        else {
            orig_height = height;
            orig_width = width;
        }
        Bitmap out = Bitmap.createScaledBitmap(bitmap, orig_width, orig_height, false);
        Log.d("SIZE","Image size resize:" + Formatter.formatFileSize(context, out.getByteCount()));

        return out;
    }

    public static boolean saveImage(Context c, Bitmap bitmap, @NonNull String name) throws IOException {
        boolean saved;
        OutputStream fos;

        String IMAGES_FOLDER_NAME = "";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ContentResolver resolver = c.getContentResolver();
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, name);
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/png");
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, "DCIM/" + IMAGES_FOLDER_NAME);
            Uri imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            fos = resolver.openOutputStream(imageUri);
        } else {
            String imagesDir = Environment
                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
                    .toString() + File.separator + IMAGES_FOLDER_NAME;

            File file = new File(imagesDir);

            if (!file.exists()) {
                file.mkdir();
            }

            File image = new File(imagesDir, name + ".png");
            fos = new FileOutputStream(image);
        }

        saved = bitmap.compress(Bitmap.CompressFormat.WEBP, 100, fos);
        fos.flush();
        fos.close();
        
        return saved;
    }

    public static Bitmap getCompressBitmap(Bitmap bitmap, Bitmap.CompressFormat format, int quality, BitmapFactory.Options options, int desWidth, int desHeight) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(format, quality, baos);

        byte[] val = baos.toByteArray();
        ByteArrayInputStream ins = new ByteArrayInputStream(val);

        int ouWidth = options.outWidth;
        int outHeight = options.outHeight;



        float heightScale = (outHeight / desHeight);
        float widthScale = (ouWidth / desWidth);
        options.inSampleSize = Math.round(heightScale > widthScale ? heightScale : widthScale);
        options.inScaled = true;

        Bitmap bm = BitmapFactory.decodeStream(ins, null, options);
        ins.close();
        baos.close();

//        Log.d("AAA","quality="+quality+",byte-size="+val.length);
        return bm;
    }
}
