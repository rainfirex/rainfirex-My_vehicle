package com.sakhhome.vehicle.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;

import com.sakhhome.vehicle.adaptors.VehicleSelectAdaptor;
import com.sakhhome.vehicle.models.OilChange;
import com.sakhhome.vehicle.models.Vehicle;

import java.util.List;

public class VehicleListFragment extends ListFragment {

    List<Vehicle> listVehicle;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        // Получить весь транспорт из БД
        listVehicle = Vehicle.gets(getContext());

        LayoutInflater layoutInflater = getLayoutInflater();
        VehicleSelectAdaptor adapterVehicle = new VehicleSelectAdaptor(listVehicle, layoutInflater);
        setListAdapter(adapterVehicle);

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onListItemClick(@NonNull ListView l, @NonNull View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        Vehicle vh = (Vehicle)l.getItemAtPosition(position);

//        Bundle bundle = new Bundle();
//        bundle.putSerializable("obj", vh);

        Intent intent = new Intent();
        intent.putExtra("vehicle_id", vh.getId());

//        getActivity().getSupportFragmentManager().popBackStack();
//        getActivity().getFragmentManager().popBackStack();
        getActivity().setIntent(intent);
        getActivity().onBackPressed();

//        setResult(RESULT_OK, intent);
//
//        finish();
    }
}
