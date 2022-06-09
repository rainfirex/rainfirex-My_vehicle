package com.sakhhome.vehicle.activities;

import android.app.ActionBar;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.sakhhome.vehicle.R;
import com.sakhhome.vehicle.fragments.OsagoFragment;
import com.sakhhome.vehicle.models.Vehicle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class OsagoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_osago1);

        setTitle(getResources().getText(R.string.osago_title));

        int vehicleId = loadVehicleId();

        if (vehicleId != -1){
            Bundle bundle = new Bundle();
            bundle.putInt("vehicleId", vehicleId);

            Fragment fragment = new OsagoFragment();
            fragment.setArguments(bundle);

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentOsago, fragment)
                    .commit();
        }
        else{
            Toast.makeText(getBaseContext(), "Транспорт не выбран", Toast.LENGTH_LONG).show();
        }
    }

    private int loadVehicleId(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        return sharedPreferences.getInt("currentVehicleId", -1);
    }
}
