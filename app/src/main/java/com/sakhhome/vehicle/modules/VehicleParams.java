package com.sakhhome.vehicle.modules;

import android.content.Context;

import com.sakhhome.vehicle.R;
import com.sakhhome.vehicle.utils.DataCSVHelper;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class VehicleParams {

    private final Context context;
    private final List listBaseVehicle;

    public VehicleParams(Context context){
        this.context = context;

        InputStream inputStream = context.getResources().openRawResource(R.raw.basecar);
        DataCSVHelper dataCSVHelper = new DataCSVHelper(inputStream);
        listBaseVehicle = dataCSVHelper.read(";");
    }

    /**
     * года с 1985 по текущий
     * @return String[]
     */
    public String[] getYears(){
        int startYear = 1985;

        Calendar cal = Calendar.getInstance();
        int curYear = cal.get(Calendar.YEAR);

        String years[] = new String[(curYear - startYear) + 1];
        int j = 0;
        for (int i = startYear; i <= curYear; i++){
            years[j] = "" +i;
            j++;
        }
        return years;
    }

    /**
     * Марки
     * @return List<String>
     */
    public String[] getMark() {
        List<String> listMark = new ArrayList<>();

        for (int i = 0; i < listBaseVehicle.size(); i++){
            String[] vehicleArray = (String[])listBaseVehicle.get(i);

            if(!listMark.contains(vehicleArray[0])) {
                listMark.add(vehicleArray[0]);
            }
        }
        return listMark.toArray(new String[0]);
    }

    /**
     * Модели
     * @param String selectMark
     * @return
     */
    public String[] getModel(String selectMark){
        List<String> listModel = new ArrayList<>();

        for (int i = 0; i < listBaseVehicle.size(); i++){
            String[] vehicleArray = (String[])listBaseVehicle.get(i);

            if(selectMark.equals(vehicleArray[0]) && !listModel.contains(vehicleArray[1])) {
                listModel.add(vehicleArray[1]);
            }
        }
        return listModel.toArray(new String[0]);
    }
}
