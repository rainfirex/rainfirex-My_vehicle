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
import com.sakhhome.vehicle.models.Vehicle;

import java.text.DecimalFormat;

public class OilInfoFragment extends Fragment {

    Vehicle currVehicle;
    OilChange oilChange;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        int vehicleId = sharedPreferences.getInt("currentVehicleId", -1);

        currVehicle = Vehicle.get(getContext(), vehicleId);

        Bundle bundle = getArguments();
        if (bundle != null){
            oilChange = (OilChange) bundle.getSerializable("obj");
        }

        View v = inflater.inflate(R.layout.fragment_oil_info, null);

        CharSequence rub = v.getResources().getText(R.string.rub);

        TextView infoDate = v.findViewById(R.id.infoDate);
        infoDate.setText(String.format("Дата замены: %s %s",oilChange.getDate(), v.getResources().getText(R.string.year)));

        TextView infoOdometr = v.findViewById(R.id.infoOdometr);
        infoOdometr.setText(String.format("Пробег: %s %s", oilChange.getOdometr(), v.getResources().getText(R.string.km)));

        TextView infoEngineOilName = v.findViewById(R.id.infoEngineOilName);
        infoEngineOilName.setText(String.format("Наименование моторного масла: %s", oilChange.getOilName()));

        TextView infoEngineOilLiters = v.findViewById(R.id.infoEngineOilLiters);
        infoEngineOilLiters.setText(String.format("Использовано %s литров", oilChange.getOilLiter()));

        TextView infoEngineOilPrice = v.findViewById(R.id.infoEngineOilPrice);
        infoEngineOilPrice.setText(String.format("Цена за литр: %s %s",
                new DecimalFormat("0.00").format(oilChange.getOilPrice()), rub));

        TextView infoAddressStation = v.findViewById(R.id.infoAddressStation);
        infoAddressStation.setText(String.format("Адрес станции замены: %s", oilChange.getAddress()));

        TextView infoStationName = v.findViewById(R.id.infoStationName);
        infoStationName.setText(String.format("Станции замены: %s", oilChange.getAddress()));

        TextView infoAirFilterName = v.findViewById(R.id.infoAirFilterName);
        infoAirFilterName.setText(String.format("Наименование воздушного фильтра: %s", oilChange.getAirFilterName()));

        TextView infoAirFilterPrice = v.findViewById(R.id.infoAirFilterPrice);
        infoAirFilterPrice.setText(String.format("Цена возд. фильтра: %s %s", new DecimalFormat("0.00").format(oilChange.getOilPrice()), rub));

        TextView infoOilFilterName = v.findViewById(R.id.infoOilFilterName);
        infoOilFilterName.setText(String.format("Наименование масленого фильтра: %s", oilChange.getOilFilterName()));

        TextView infoOilFilterPrice = v.findViewById(R.id.infoOilFilterPrice);
        infoOilFilterPrice.setText(String.format("Цена масленого фильтра: %s %s", new DecimalFormat("0.00").format(oilChange.getOilFilterPrice()), rub));

        TextView infoWorkPrice = v.findViewById(R.id.infoWorkPrice);
        infoWorkPrice.setText(String.format("Стоимость обслуживания: %s %s", new DecimalFormat("0.00").format(oilChange.getWorkPrice()), rub));

        Double sumOil = 0.0;
        if (oilChange.getOilPrice() > 0 && oilChange.getOilPrice() > 0){
            sumOil = oilChange.getOilPrice() * oilChange.getOilLiter();
        }
        Double sum = sumOil + oilChange.getAirFilterPrice() + oilChange.getOilFilterPrice() + oilChange.getWorkPrice();

        TextView infoTotalOilSum = v.findViewById(R.id.infoTotalOilSum);
        infoTotalOilSum.setText(String.format("Стоимость масла: %s %s",
                new DecimalFormat("0.00").format(sumOil), rub));

        TextView infoTotalSum = v.findViewById(R.id.infoTotalSum);
        infoTotalSum.setText(String.format("Всего обошлось в %s %s",
                new DecimalFormat("0.00").format(sum), rub));

        Button btnDelete = v.findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(btnDelete_click);

        return v;
    }

    /**
     * Удалить запись
     */
    View.OnClickListener btnDelete_click = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            OilChange.delete(view.getContext(),  oilChange.getId());
            oilChange = null;

            getParentFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentOil, new OilListFragment())
                    .commit();
        }
    };
}
