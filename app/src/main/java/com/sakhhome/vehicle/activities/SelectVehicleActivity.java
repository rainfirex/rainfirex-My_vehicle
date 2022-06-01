package com.sakhhome.vehicle.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.sakhhome.vehicle.R;
import com.sakhhome.vehicle.adaptors.VehicleSelectAdaptor;
import com.sakhhome.vehicle.database.TableVehicle;
import com.sakhhome.vehicle.fragments.VehicleListFragment;
import com.sakhhome.vehicle.models.Vehicle;

import java.util.List;

public class SelectVehicleActivity extends AppCompatActivity {

    public final static int REQUEST_CODE = 101;

    ListView listSelectVehicle;
    List<Vehicle> listVehicle;

    // Выбранный транспорт
    // для контекстного меню
    Vehicle selectVehicle;

    VehicleSelectAdaptor adapterVehicle;

    Fragment fragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_vehicle);

        setTitle(R.string.activity_select_vehicle);

        fragment = new VehicleListFragment();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentVehicle, fragment)
                .commit();

        listSelectVehicle = findViewById(R.id.listSelectVehicle);
        registerForContextMenu(listSelectVehicle);

        // Получить весь транспорт из БД
        listVehicle = Vehicle.gets(this);

        LayoutInflater layoutInflater = getLayoutInflater();
        adapterVehicle = new VehicleSelectAdaptor(listVehicle, layoutInflater);

        listSelectVehicle.setAdapter(adapterVehicle);
        listSelectVehicle.setOnItemClickListener(listSelectVehicle_click);
    }

    /**
     * Выбор транспорта
     */
    AdapterView.OnItemClickListener listSelectVehicle_click = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Vehicle vh = listVehicle.get(i);

            Intent intent = new Intent();
            intent.putExtra("vehicle_id", vh.getId());
            setResult(RESULT_OK, intent);

            finish();
        }
    };

    /**
     * Контекстное меню для списка
     * @param menu
     * @param v
     * @param menuInfo
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_context_select_vehicle, menu);

        if(v.getId() == R.id.listSelectVehicle){
            ListView listView = (ListView) v;
            AdapterView.AdapterContextMenuInfo adaptor = (AdapterView.AdapterContextMenuInfo) menuInfo;
            selectVehicle = (Vehicle) listView.getItemAtPosition(adaptor.position);
        }
    }

    /**
     * Выбор элемента контекстного меню
     * @param item
     * @return
     */
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
        return false;
    }

    /**
     * Открыть на редактирование
     */
    private void contextMenuShowEdit(){
        Intent intent = new Intent(getBaseContext(), EditVehicleActivity.class);
        intent.putExtra(TableVehicle.KEY_ID, selectVehicle.getId());

        activityForResult.launch(intent);
    }

    /**
     * Удалить транспорт
     */
    private void contextMenuDelete(){
        int count = Vehicle.delete(getApplicationContext(), selectVehicle.getId());
        if (count > 0){
            listVehicle.remove(selectVehicle);
            selectVehicle = null;
            adapterVehicle.notifyDataSetChanged();
        }
    }

    /**
     * Обновить результат редактирование
     */
    ActivityResultLauncher<Intent> activityForResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        int id = result.getData().getIntExtra("vehicle_id", -1);

                        Vehicle editVehicle = Vehicle.get(getApplicationContext(), id);

                        int position = adapterVehicle.findId(editVehicle.getId());
                        Vehicle vehicle = listVehicle.get(position);
                        vehicle.setTitle(editVehicle.getTitle());
                        vehicle.setColor(editVehicle.getColor());
                        vehicle.setOdometr(editVehicle.getOdometr());
                        vehicle.setYear(editVehicle.getYear());
                        vehicle.setMark(editVehicle.getMark());
                        vehicle.setModel(editVehicle.getModel());
                        vehicle.setAvatar(editVehicle.getAvatar());

                        adapterVehicle.notifyDataSetChanged();
                    }
                }
            });

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_select_vehicle, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.add_vehicle:
                startActivity(new Intent(this, EditVehicleActivity.class));
                return true;
        }

        return false;
    }
}
