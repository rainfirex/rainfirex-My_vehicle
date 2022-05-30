package com.sakhhome.vehicle.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.sakhhome.vehicle.R;
import com.sakhhome.vehicle.fragments.OilAddFragment;
import com.sakhhome.vehicle.fragments.OilListFragment;

public class OilChangeActivity extends AppCompatActivity {

    private Fragment fragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oil_change);

        setTitle(R.string.activity_oil_change);

        fragment = new OilListFragment();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentOil, fragment)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_oil, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.add_oil_change:
                fragment = new OilAddFragment();
                break;
            case R.id.add_oil_list:
                fragment = new OilListFragment();
                break;
        }
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentOil, fragment)
                .commit();

        return super.onOptionsItemSelected(item);
    }
}
