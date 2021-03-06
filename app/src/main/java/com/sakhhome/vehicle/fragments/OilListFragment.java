package com.sakhhome.vehicle.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

import com.sakhhome.vehicle.R;
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

    @Override
    public void onListItemClick(@NonNull ListView l, @NonNull View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        OilChange oilChange = (OilChange) l.getItemAtPosition(position);

        Bundle bundle = new Bundle();
        bundle.putSerializable("obj", oilChange);

        Fragment fragment = new OilInfoFragment();
        fragment.setArguments(bundle);

        getParentFragmentManager().
                beginTransaction().
                replace(R.id.fragmentOil, fragment).
                commit();
    }
}
