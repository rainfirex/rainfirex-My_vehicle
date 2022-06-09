package com.sakhhome.vehicle.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

import com.sakhhome.vehicle.R;
import com.sakhhome.vehicle.adaptors.VehicleSelectAdaptor;
import com.sakhhome.vehicle.models.Vehicle;

import java.util.List;

public class VehicleListFragment extends ListFragment {

    private List<Vehicle> listVehicle;
    private Vehicle selectVehicle;
    private VehicleSelectAdaptor adapterVehicle;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Получить весь транспорт из БД
        listVehicle = Vehicle.gets(getContext());

        LayoutInflater layoutInflater = getLayoutInflater();
        adapterVehicle = new VehicleSelectAdaptor(listVehicle, layoutInflater);

        setListAdapter(adapterVehicle);

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();

        registerForContextMenu(getListView());
    }

    @Override
    public void onListItemClick(@NonNull ListView l, @NonNull View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        selectVehicle = (Vehicle)l.getItemAtPosition(position);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(v.getContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt("currentVehicleId", selectVehicle.getId());
        editor.apply();

        getActivity().onBackPressed();
    }

    /**
     * Контекстное меню для списка
     * @param menu
     * @param v
     * @param menuInfo
     */
    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.menu_context_select_vehicle, menu);

        ListView listView = (ListView) v;
        AdapterView.AdapterContextMenuInfo adaptor = (AdapterView.AdapterContextMenuInfo) menuInfo;
        selectVehicle = (Vehicle) listView.getItemAtPosition(adaptor.position);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.itemEditVehicle:
                contextMenuShowEdit();
                return true;
            case R.id.itemDeleteVehicle:
                contextMenuDelete();
                return true;
        }
        return super.onContextItemSelected(item);
    }

    /**
     * Открыть на редактирование
     */
    private void contextMenuShowEdit(){
        Bundle bundle = new Bundle();
        bundle.putInt("id", selectVehicle.getId());

        Fragment fragment = new VehicleEditFragment();
        fragment.setArguments(bundle);

        getParentFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentVehicle, fragment)
                .commit();
    }

    /**
     * Удалить транспорт
     */
    private void contextMenuDelete(){
        try {
            int count = Vehicle.delete(getContext(), selectVehicle.getId());
            if (count > 0) {
                listVehicle.remove(selectVehicle);
                selectVehicle = null;
                adapterVehicle.notifyDataSetChanged();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }
}