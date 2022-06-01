package com.sakhhome.vehicle.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.sakhhome.vehicle.R;
import com.sakhhome.vehicle.models.OilChange;
import com.sakhhome.vehicle.models.ReFuel;
import com.sakhhome.vehicle.models.Vehicle;

public class FuelInfoFragment extends Fragment {

    Vehicle currVehicle;
    ReFuel reFuel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        int vehicleId = sharedPreferences.getInt("currentVehicleId", -1);
        Bundle bundle = getArguments();
        if (bundle != null){
            reFuel = (ReFuel) bundle.getSerializable("obj");
        }

        currVehicle = Vehicle.get(getContext(), vehicleId);

        View v = inflater.inflate(R.layout.fragment_fuel_info, null);

        CharSequence rub = v.getResources().getText(R.string.rub);

        TextView infoDate = v.findViewById(R.id.infoDate);
        infoDate.setText(String.format("Дата замены: %s %s",reFuel.getDate(), v.getResources().getText(R.string.year)));

        TextView infoOdometr = v.findViewById(R.id.infoOdometr);
        infoOdometr.setText(String.format("Пробег: %s %s", reFuel.getOdometr(), v.getResources().getText(R.string.km)));

        TextView infoFuel = v.findViewById(R.id.infoFuel);
        infoFuel.setText(String.format("Топливо %s, залито %s %s", reFuel.getType(), reFuel.getFuelLiter(), v.getResources().getText(R.string.litre)));

        TextView infoAddressStation = v.findViewById(R.id.infoAddressStation);
        infoAddressStation.setText(String.format("Адрес станции: %s", reFuel.getAddress()));

        TextView infoStationName = v.findViewById(R.id.infoStationName);
        infoStationName.setText(String.format("Наименование: %s", reFuel.getStation()));

        TextView infoFuelPrice = v.findViewById(R.id.infoFuelPrice);
        infoFuelPrice.setText(String.format("Цена за литр: %s %s", reFuel.getFuelPrice(), rub));

        TextView infoTotalSum = v.findViewById(R.id.infoTotalSum);
        infoTotalSum.setText(String.format("Общая сумма: %s %s", reFuel.getSum(), rub));

        TextView infoNote = v.findViewById(R.id.infoNote);
        infoNote.setText(String.format("Примечание:\n%s", reFuel.getNote()));

        Button btnDelete = v.findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(btnDelete_click);

        return v;
    }

    View.OnClickListener btnDelete_click = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            ReFuel.delete(view.getContext(), reFuel.getId());
            reFuel = null;

            getParentFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentFuel, new FuelListFragment())
                    .commit();
        }
    };
}
