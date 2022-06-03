package com.sakhhome.vehicle.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.sakhhome.vehicle.R;
import com.sakhhome.vehicle.fragments.FuelAddFragment;
import com.sakhhome.vehicle.fragments.FuelListFragment;

public class RefuelingActivity extends AppCompatActivity {

    private Fragment fragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refueling);

        setTitle(R.string.title_fuel_activity);

        fragment = new FuelListFragment();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentFuel, fragment)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_fuel, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.add:
                fragment = new FuelAddFragment();
                break;
            case R.id.list:
                fragment = new FuelListFragment();
                break;
        }

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentFuel, fragment)
                .commit();

        return super.onOptionsItemSelected(item);
    }
}
