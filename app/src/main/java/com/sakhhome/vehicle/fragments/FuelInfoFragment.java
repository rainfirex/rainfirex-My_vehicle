package com.sakhhome.vehicle.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.sakhhome.vehicle.R;
import com.sakhhome.vehicle.models.Vehicle;

public class FuelInfoFragment extends Fragment {

    Vehicle currVehicle;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        int vehicleId = sharedPreferences.getInt("currentVehicleId", -1);

        currVehicle = Vehicle.get(getContext(), vehicleId);

        View v = inflater.inflate(R.layout.fragment_fuel_info, null);


        return v;
    }
}
