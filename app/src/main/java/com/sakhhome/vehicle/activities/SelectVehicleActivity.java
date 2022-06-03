package com.sakhhome.vehicle.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.sakhhome.vehicle.R;

import com.sakhhome.vehicle.fragments.VehicleEditFragment;
import com.sakhhome.vehicle.fragments.VehicleListFragment;

public class SelectVehicleActivity extends AppCompatActivity {

    public final static int REQUEST_CODE = 101;

    Fragment fragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_vehicle);

        setTitle(R.string.select_vehicle_title);

        fragment = new VehicleListFragment();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentVehicle, fragment)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_select_vehicle, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.add_vehicle:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentVehicle, new VehicleEditFragment())
                        .commit();

                return true;
        }

        return false;
    }
}
