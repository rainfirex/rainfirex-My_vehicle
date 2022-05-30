package com.sakhhome.vehicle.adaptors;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sakhhome.vehicle.R;
import com.sakhhome.vehicle.models.Vehicle;

import java.util.List;

public class VehicleSelectAdaptor extends BaseAdapter {

    private List<Vehicle> listVehicle;

    private LayoutInflater inflater;

    public VehicleSelectAdaptor(List<Vehicle> listVehicle, LayoutInflater inflater){
        this.listVehicle = listVehicle;
        this.inflater = inflater;
    }

    @Override
    public int getCount() {
        return listVehicle.size();
    }

    @Override
    public Object getItem(int i) {

        return listVehicle.get(i);
    }
    @Override
    public long getItemId(int i) {
        return i;
    }

    /**
     * Получить позицию по Id
     * @param id
     * @return
     */
    public int findId(int id){
        for (Vehicle vh : listVehicle){
            if (vh.getId() == id)
                return listVehicle.indexOf(vh);
        }
        return -1;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null){
            view = inflater.inflate(R.layout.view_vehicle_list_item, viewGroup, false);
        }

        TextView vehicleTitle = view.findViewById(R.id.vehicleTitle);
        TextView vehicleMarkModel = view.findViewById(R.id.vehicleMarkModel);
        TextView vehicleOdometr = view.findViewById(R.id.vehicleOdometr);
        ImageView vehicleAvatar = view.findViewById(R.id.vehicleAvatar);

        Vehicle vehicle = (Vehicle)getItem(i);

        vehicleTitle.setText(vehicle.getTitle());
        vehicleMarkModel.setText(String.format("Транспорт: %s %s %s г." , vehicle.getMark(), vehicle.getModel(), vehicle.getYear()));
        vehicleOdometr.setText(String.format("Пробег: %s КМ", vehicle.getOdometr()));
        if(vehicle.getAvatar() != null)
            vehicleAvatar.setImageBitmap(vehicle.getAvatar());
        else
            vehicleAvatar.setImageResource(R.drawable.car_avatar);
        return view;
    }
}
