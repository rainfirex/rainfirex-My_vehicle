package com.sakhhome.vehicle.activities;

import android.os.Bundle;
import android.view.Menu;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.sakhhome.vehicle.R;

public class RefuelingActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refueling);

        setTitle(R.string.activity_refueling);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_oil_change, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
