package com.sakhhome.vehicle.fragments;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.sakhhome.vehicle.R;
import com.sakhhome.vehicle.models.OilChange;
import com.sakhhome.vehicle.models.Vehicle;
import com.sakhhome.vehicle.utils.Utils;

import java.util.Calendar;

public class OilAddFragment extends Fragment {

    TextView textViewOilLiter;
    SeekBar seekBarOilLiter;
    EditText txtDateChange, txtOdometr, txtOilName, txtOilPrice, txtAddress, txtStation,
            txtOilAirFilterName, txtOilAirFilterPrice, txtOilFilerName, txtOilFilterPrice, txtOilWorkPrice;
    CheckBox chkStationInfo, chkChangeAirFilter, chkChangeOilFilter;
    Button btnSetDate, btnChangeOil;

    LinearLayout layoutStationAddress, layoutStationName, layoutAirName, layoutAirPrice, layoutOilFilerName, layoutOilFilerPrice;

    Vehicle currVehicle;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_oil_add, null);


        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        int vehicleId = sharedPreferences.getInt("currentVehicleId", -1);

        currVehicle = Vehicle.get(getContext(), vehicleId);


        if (vehicleId == -1 || currVehicle == null) return v;

        layoutStationAddress = v.findViewById(R.id.layoutStationAddress);
        layoutStationName = v.findViewById(R.id.layoutStationName);
        layoutAirName  = v.findViewById(R.id.layoutAirName);
        layoutAirPrice = v.findViewById(R.id.layoutAirPrice);
        layoutOilFilerName = v.findViewById(R.id.layoutOilFilerName);
        layoutOilFilerPrice = v.findViewById(R.id.layoutOilFilerPrice);

        textViewOilLiter = v.findViewById(R.id.textViewOilLiter);
        textViewOilLiter.setText(String.format("%s %s", getResources().getString(R.string.oil_liter), 1));

        txtOdometr = v.findViewById(R.id.txtOdometr);
        txtOdometr.setText(String.valueOf(currVehicle.getOdometr()));

        txtOilName = v.findViewById(R.id.txtOilName);
        txtOilPrice = v.findViewById(R.id.txtOilPrice);
        txtOilFilerName = v.findViewById(R.id.txtOilFilerName);
        txtOilFilterPrice = v.findViewById(R.id.txtOilFilterPrice);
        txtOilWorkPrice   = v.findViewById(R.id.txtOilWorkPrice);

        txtAddress = v.findViewById(R.id.txtAddress);
        txtStation = v.findViewById(R.id.txtStation);
        txtOilAirFilterName = v.findViewById(R.id.txtOilAirFilterName);
        txtOilAirFilterPrice = v.findViewById(R.id.txtOilAirFilterPrice);

        chkStationInfo = v.findViewById(R.id.chkStationInfo);
        chkStationInfo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    layoutStationAddress.setVisibility(View.VISIBLE);
                    layoutStationName.setVisibility(View.VISIBLE);
                }
                else {
                    layoutStationAddress.setVisibility(View.GONE);
                    layoutStationName.setVisibility(View.GONE);
                }
            }
        });

        chkChangeAirFilter = v.findViewById(R.id.chkChangeAirFilter);
        chkChangeAirFilter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    layoutAirName.setVisibility(View.VISIBLE);
                    layoutAirPrice.setVisibility(View.VISIBLE);
                }
                else {
                    layoutAirName.setVisibility(View.GONE);
                    layoutAirPrice.setVisibility(View.GONE);
                }
            }
        });

        chkChangeOilFilter = v.findViewById(R.id.chkChangeOilFilter);
        chkChangeOilFilter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    layoutOilFilerName.setVisibility(View.VISIBLE);
                    layoutOilFilerPrice.setVisibility(View.VISIBLE);
                }
                else {
                    layoutOilFilerName.setVisibility(View.GONE);
                    layoutOilFilerPrice.setVisibility(View.GONE);
                }
            }
        });

        seekBarOilLiter = v.findViewById(R.id.seekBarOilLiter);
        seekBarOilLiter.setOnSeekBarChangeListener(seekBarOilLiter_change);

        txtDateChange = v.findViewById(R.id.txtDateChange);
        txtDateChange.setText(Utils.getCurrDate());

        btnSetDate = v.findViewById(R.id.btnSetDate);
        btnSetDate.setOnClickListener(btnSetDate_click);

        btnChangeOil = v.findViewById(R.id.btnChangeOil);
        btnChangeOil.setOnClickListener(btnChangeOil_click);

        return v;
    }

    /**
     * Установить литры
     */
    SeekBar.OnSeekBarChangeListener seekBarOilLiter_change = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            textViewOilLiter.setText(String.format("%s %s", getResources().getString(R.string.oil_liter), i));
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };

    /**
     * Выбрать дату
     */
    View.OnClickListener btnSetDate_click = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    txtDateChange.setText(String.format("%s.%s.%s", Utils.addZero(day), Utils.addZero(month), year));
                }
            };

            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH) + 1;
            int day = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(view.getContext(), dateSetListener, year,month,day);
            datePickerDialog.show();
        }
    };

    /**
     * Добавить запись
     */
    View.OnClickListener btnChangeOil_click = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            if (txtDateChange.getText().toString().trim().equalsIgnoreCase("")){
                txtDateChange.requestFocus();
                return;
            }
            if (txtOdometr.getText().toString().trim().equalsIgnoreCase("")){
                txtOdometr.requestFocus();
                return;
            }

            String date = txtDateChange.getText().toString().trim();
            int odometr = Integer.parseInt(txtOdometr.getText().toString().trim());
            String oilName = txtOilName.getText().toString().trim();
            int oilLiter = seekBarOilLiter.getProgress();
            Double oilPrice = (txtOilPrice.getText().toString().trim().length() > 0)? Double.parseDouble(txtOilPrice.getText().toString().trim()) : 0;
            String address = txtAddress.getText().toString().trim();
            String station = txtStation.getText().toString().trim();
            String airFilterName = txtOilAirFilterName.getText().toString().trim();
            Double airFilterPrice = (txtOilAirFilterPrice.getText().toString().trim().length() > 0) ? Double.parseDouble(txtOilAirFilterPrice.getText().toString().trim()) : 0;
            String oilFilterName = txtOilFilerName.getText().toString().trim();
            Double oilFilterPrice = (txtOilFilterPrice.getText().toString().trim().length() > 0) ? Double.parseDouble(txtOilFilterPrice.getText().toString().trim()) : 0;
            Double oilWorkPrice = (txtOilWorkPrice.getText().toString().trim().length() > 0) ? Double.parseDouble(txtOilWorkPrice.getText().toString().trim()) : 0;

            if(odometr <= currVehicle.getOdometr()){
                Toast.makeText(getActivity(), view.getResources().getText(R.string.error_odometr), Toast.LENGTH_LONG).show();
                return;
            }

            Double sumOil = 0.0;
            if (oilPrice > 0 && oilLiter > 0){
                sumOil = oilPrice * oilLiter;
            }
            Double sum = sumOil + airFilterPrice + oilFilterPrice + oilWorkPrice;

            currVehicle.setOdometr(odometr);
            currVehicle.save(getContext());

            OilChange.create(getContext(), date, odometr, oilName, oilLiter, oilPrice, address,
                    station, airFilterName, airFilterPrice, currVehicle.getId(), oilFilterName, oilFilterPrice, oilWorkPrice);

            Toast.makeText(getActivity(), "Сумма за масло: " + sumOil + ", Сумма затраты: "+ sum, Toast.LENGTH_LONG).show();


            getParentFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentOil, new OilListFragment())
                    .commit();
        }
    };
}
