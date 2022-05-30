package com.sakhhome.vehicle.fragments;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.sakhhome.vehicle.R;
import com.sakhhome.vehicle.models.ReFuel;
import com.sakhhome.vehicle.models.Vehicle;
import com.sakhhome.vehicle.utils.Utils;

import java.text.DecimalFormat;
import java.util.Calendar;

public class FuelAddFragment extends Fragment {

    private TextView textViewFuelLiter, txtResultInfo;
    private EditText txtDateChange, txtOdometr, txtFuelPrice, txtFuelSum, txtFuelAddress, txtFuelStation, txtNote;
    private SeekBar seekBarFuelLiter;
    private RadioButton radioLiterPrice, radioSum;
    private LinearLayout layoutFuelPrice, layoutFuelSum, layoutFuelAddress, layoutFuelStation;

    private Vehicle currVehicle;

    private double fuelPrice, sum;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_fuel_add, null);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        int vehicleId = sharedPreferences.getInt("currentVehicleId", -1);

        currVehicle = Vehicle.get(getContext(), vehicleId);

        if (vehicleId == -1 || currVehicle == null) return v;

        layoutFuelPrice = v.findViewById(R.id.layoutFuelPrice);
        layoutFuelSum = v.findViewById(R.id.layoutFuelSum);
        layoutFuelAddress = v.findViewById(R.id.layoutFuelAddress);
        layoutFuelStation = v.findViewById(R.id.layoutFuelStation);

        textViewFuelLiter = v.findViewById(R.id.textViewFuelLiter);
        textViewFuelLiter.setText(String.format("%s %s", getResources().getString(R.string.fuel_litres), 1));

        seekBarFuelLiter = v.findViewById(R.id.seekBarFuelLiter);
        seekBarFuelLiter.setOnSeekBarChangeListener(seekBarFuelLiter_change);

        txtDateChange = v.findViewById(R.id.txtDateChange);
        txtDateChange.setText(Utils.getCurrDate());

        txtOdometr = v.findViewById(R.id.txtOdometr);
        txtOdometr.setText(String.valueOf(currVehicle.getOdometr()));

        txtFuelPrice = v.findViewById(R.id.txtFuelPrice);
        txtFuelPrice.addTextChangedListener(txtFuelPrice_input);

        txtFuelSum = v.findViewById(R.id.txtFuelSum);
        txtFuelSum.addTextChangedListener(txtFuelSum_input);

        txtResultInfo = v.findViewById(R.id.txtResultInfo);

        txtFuelAddress = v.findViewById(R.id.txtFuelAddress);
        txtFuelStation = v.findViewById(R.id.txtFuelStation);

        txtNote = v.findViewById(R.id.txtNote);

        Button btnSetDate = v.findViewById(R.id.btnSetDate);
        btnSetDate.setOnClickListener(btnSetDate_click);

        CheckBox chkStationName = v.findViewById(R.id.chkStationName);
        chkStationName.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    layoutFuelAddress.setVisibility(View.VISIBLE);
                    layoutFuelStation.setVisibility(View.VISIBLE);
                }
                else {
                    layoutFuelAddress.setVisibility(View.GONE);
                    layoutFuelStation.setVisibility(View.GONE);
                }
            }
        });

        radioLiterPrice = v.findViewById(R.id.radioLiterPrice);
        radioLiterPrice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    layoutFuelPrice.setVisibility(View.VISIBLE);
                }
                else
                {
                    layoutFuelPrice.setVisibility(View.GONE);
                }
                calc();
            }
        });
        radioSum = v.findViewById(R.id.radioSum);
        radioSum.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    layoutFuelSum.setVisibility(View.VISIBLE);
                }
                else
                {
                    layoutFuelSum.setVisibility(View.GONE);
                }
                calc();
            }
        });

        Button btnAdd = v.findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(btnAdd_click);

        return v;
    }

    private Double calcSum(int litres, Double price){
        double sum = 0.00;
        if (litres > 0 && price > 0){
            sum = litres * price;
        }
        return sum;
    }

    private Double calcLiterPrice(int litres, Double sum){
        double price = 0.00;
        if (litres > 0 && sum > 0){
            price = sum / litres;
        }
        return price;
    }

    private void calc(){
        int litres = seekBarFuelLiter.getProgress();
        txtResultInfo.setVisibility(View.VISIBLE);

        String textInfo;
        if (radioLiterPrice.isChecked()){
            String txtPrice = txtFuelPrice.getText().toString().trim();
            if (txtPrice.equalsIgnoreCase("")) return;

            double price = Double.parseDouble(txtPrice);
            sum = calcSum(litres, price);
            fuelPrice = price;
            textInfo = String.format("Сумма за %s %s составляет: %s %s, по цене за литр: %s %s",
                    litres, getResources().getText(R.string.litre),
                    new DecimalFormat("0.00").format(sum), getResources().getText(R.string.rub),
                    new DecimalFormat("0.00").format(price), getResources().getText(R.string.rub));
        }
        else if (radioSum.isChecked()){
            String txtSum = txtFuelSum.getText().toString().trim();
            if (txtSum.equalsIgnoreCase("")) return;

            double sum = Double.parseDouble(txtSum);
            fuelPrice = calcLiterPrice(litres, sum);
            this.sum = sum;
            textInfo = String.format("Сумма за %s %s составляет: %s %s, по цене за литр: %s %s",
                    litres, getResources().getText(R.string.litre),
                    new DecimalFormat("0.00").format(sum), getResources().getText(R.string.rub),
                    new DecimalFormat("0.00").format(fuelPrice), getResources().getText(R.string.rub));
        }
        else return;

        txtResultInfo.setText(textInfo);
    }

   SeekBar.OnSeekBarChangeListener seekBarFuelLiter_change = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            textViewFuelLiter.setText(String.format("%s %s", getResources().getString(R.string.fuel_litres), i));
            calc();
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {}

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {}
    };

   TextWatcher txtFuelPrice_input = new TextWatcher() {
       @Override
       public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

       @Override
       public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
           calc();
       }

       @Override
       public void afterTextChanged(Editable editable) {}
   };

   TextWatcher txtFuelSum_input = new TextWatcher() {
       @Override
       public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

       @Override
       public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
           calc();
       }

       @Override
       public void afterTextChanged(Editable editable) {}
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
    View.OnClickListener btnAdd_click = new View.OnClickListener() {
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
            int oilLiter = seekBarFuelLiter.getProgress();
            String address = txtFuelAddress.getText().toString().trim();
            String station = txtFuelStation.getText().toString().trim();
            String note = txtNote.getText().toString().trim();

            if(odometr <= currVehicle.getOdometr()){
                Toast.makeText(getActivity(), "Пробег меньше текущего", Toast.LENGTH_LONG).show();
                return;
            }

            String type = "95";
            ReFuel.create(getContext(), date, odometr, type, oilLiter, fuelPrice, sum, address, station, note, currVehicle.getId());

            Toast.makeText(getActivity(), "Сумма: " + sum, Toast.LENGTH_LONG).show();

            getParentFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentFuel, new FuelListFragment())
                    .commit();
        }
    };
}
