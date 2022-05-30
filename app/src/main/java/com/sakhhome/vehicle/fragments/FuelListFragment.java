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
import com.sakhhome.vehicle.adaptors.ReFuelAdaptor;
import com.sakhhome.vehicle.models.ReFuel;

import java.util.List;

public class FuelListFragment extends ListFragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        int vehicleId = sharedPreferences.getInt("currentVehicleId", -1);

        List<ReFuel> list = ReFuel.gets(getContext(), vehicleId);

        ReFuelAdaptor adapter = new ReFuelAdaptor(list, getLayoutInflater());
        setListAdapter(adapter);

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onListItemClick(@NonNull ListView l, @NonNull View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        ReFuel reFuel = (ReFuel) l.getItemAtPosition(position);

        Bundle bundle = new Bundle();
        bundle.putSerializable("obj", reFuel);

        Fragment fragment = new FuelInfoFragment();
        fragment.setArguments(bundle);

        getParentFragmentManager().
                beginTransaction().
                replace(R.id.fragmentFuel, fragment).
                commit();
    }
}
