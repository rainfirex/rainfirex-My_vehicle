package com.sakhhome.vehicle.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;

import com.sakhhome.vehicle.activities.MainAppActivity;
import com.sakhhome.vehicle.adaptors.OilChangeAdaptor;
import com.sakhhome.vehicle.models.OilChange;

import java.util.List;

public class OilListFragment extends ListFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        int vehicleId = sharedPreferences.getInt("currentVehicleId", -1);

        List<OilChange> list = OilChange.gets(getContext(), vehicleId);

        OilChangeAdaptor adapter = new OilChangeAdaptor(list, getLayoutInflater());
        setListAdapter(adapter);

        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
