package com.sakhhome.vehicle.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class DataCSVHelper {

    private InputStream inputStream;

    /**
     * Чтение и запись csv файла
     * @param inputStream
     */
    public DataCSVHelper(InputStream inputStream){
        this.inputStream = inputStream;
    }

    /**
     * Прочитать файл
     * @param delimiter
     * @return
     */
    public List read(String delimiter){
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        List result = new ArrayList();

        try {
            String line = "";
            while ((line = reader.readLine()) != null) {
                String[] row = line.split(delimiter);
                result.add(row);
            }
            inputStream.close();
        }
        catch (Exception exception){
            throw new RuntimeException("Error in reading CSV file: "+ exception);
        }
        return result;
    }
}
