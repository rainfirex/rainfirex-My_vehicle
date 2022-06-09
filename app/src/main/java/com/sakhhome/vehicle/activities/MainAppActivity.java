package com.sakhhome.vehicle.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.sakhhome.vehicle.R;
import com.sakhhome.vehicle.database.ConnectDBTest;
import com.sakhhome.vehicle.models.Vehicle;
import com.sakhhome.vehicle.utils.Utils;

public class MainAppActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView txtCurrentVehicle, txtCurrentOdometr, txtCurrentColor, txtCurrentMarkModelYear, txtCurrentEngine,
            txtCurrentBody, txtCurrentTank, txtCurrentMass, txtChangeVehicle;

    private ImageView imgCurrentVehicle;

    private final boolean DEBUG_MODE = false;

    // Настройки
    private SharedPreferences sharedPreferences;

    // Выбранный транспорт
    private Vehicle currentVehicle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        // Расширение курсора
        Utils.sCursorWindowSize(DEBUG_MODE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_app);

        setTitle(R.string.main_vehicle_title);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainAppActivity.this);

        txtCurrentVehicle = findViewById(R.id.txtCurrentVehicle);
        txtCurrentOdometr = findViewById(R.id.txtCurrentOdometr);
        txtCurrentColor   = findViewById(R.id.txtCurrentColor);
        txtCurrentMarkModelYear = findViewById(R.id.txtCurrentMarkModelYear);
        txtCurrentEngine = findViewById(R.id.txtCurrentEngine);
        txtCurrentBody   = findViewById(R.id.txtCurrentBody);
        txtCurrentTank   = findViewById(R.id.txtCurrentTank);
        txtCurrentMass   = findViewById(R.id.txtCurrentMass);

        txtChangeVehicle = findViewById(R.id.txtChangeVehicle);
        txtChangeVehicle.setOnClickListener(this);

        imgCurrentVehicle = findViewById(R.id.imgCurrentVehicle);
        imgCurrentVehicle.setImageResource(R.drawable.car_avatar);

        Button btnRefuel = findViewById(R.id.btnRefuel);
        btnRefuel.setOnClickListener(this);

        Button btnChangeOil1 = findViewById(R.id.btnChangeOil1);
        btnChangeOil1.setOnClickListener(this);

        Button btnOsago = findViewById(R.id.btnOsago);
        btnOsago.setOnClickListener(this);

        Button btnConn = findViewById(R.id.btnConn);
        btnConn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConnectDBTest.conn(getBaseContext());
            }
        });

        Button btnDisConn = findViewById(R.id.btnDisConn);
        btnDisConn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConnectDBTest.close();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        int id = sharedPreferences.getInt("currentVehicleId", -1);
        setCurrentVehicle(id);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnRefuel:
                startActivity(new Intent(this, RefuelingActivity.class));
                break;
            case R.id.btnChangeOil1:
                startActivity(new Intent(this, OilChangeActivity.class));
                break;
            case R.id.txtChangeVehicle:
                activityForResult.launch(new Intent(this, SelectVehicleActivity.class));
                break;
            case R.id.btnOsago:
                startActivity(new Intent(this, OsagoActivity.class));
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK){
            if (requestCode == SelectVehicleActivity.REQUEST_CODE){
                int id = data.getIntExtra("vehicle_id", -1);

                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putInt("currentVehicleId", id);
                editor.apply();

                setCurrentVehicle(id);
            }
        }
    }

    /**
     * Установить выбранный транспорт
     * @param id
     */
    private void setCurrentVehicle(int id) {
        if (id == -1) return;

        currentVehicle = Vehicle.get(getApplicationContext(), id);

        String title;
        String fullname;
        String color;
        String odometr;
        String engine;
        String body;
        String tank;
        String mass;

        if (currentVehicle != null) {
            title = currentVehicle.getTitle();
            fullname = String.format("%s %s %s %s", currentVehicle.getMark(), currentVehicle.getModel(), currentVehicle.getYear(), getResources().getString(R.string.year));
            color = String.format("%s %s",getResources().getString(R.string.main_txt_color), currentVehicle.getColor());
            odometr = String.format("%s %s %s",getResources().getString(R.string.main_txt_odometr), currentVehicle.getOdometr(), getResources().getString(R.string.km));
            engine = String.format("%s %s, %s %s л.",getResources().getString(R.string.main_txt_engine), currentVehicle.getEngine(), getResources().getString(R.string.main_txt_engine_v), currentVehicle.getPowerEngine());
            body = String.format("%s %s",getResources().getString(R.string.main_txt_body),  currentVehicle.getBody());
            tank = String.format("%s %s л.",getResources().getString(R.string.main_txt_tank), currentVehicle.getTankLiters());
            mass = String.format("%s %s тонн",getResources().getString(R.string.main_txt_mass), currentVehicle.getMass());

            if (currentVehicle.getAvatar() != null)
                imgCurrentVehicle.setImageBitmap(currentVehicle.getAvatar());
            else
                imgCurrentVehicle.setImageResource(R.drawable.car_avatar);
        }
        else {
            title = "";
            fullname = getResources().getString(R.string.main_txt_vehicle);
            color = getResources().getString(R.string.main_txt_color);
            odometr = getResources().getString(R.string.main_txt_odometr);
            engine  = getResources().getString(R.string.main_txt_engine);
            body = getResources().getString(R.string.main_txt_body);
            tank = getResources().getString(R.string.main_txt_tank);
            mass = getResources().getString(R.string.main_txt_mass);

            imgCurrentVehicle.setImageResource(R.drawable.car_avatar);
        }

        txtCurrentVehicle.setText(title);
        txtCurrentMarkModelYear.setText(fullname);
        txtCurrentColor.setText(color);
        txtCurrentOdometr.setText(odometr);
        txtCurrentEngine.setText(engine);
        txtCurrentBody.setText(body);
        txtCurrentTank.setText(tank);
        txtCurrentMass.setText(mass);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_vehicle, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.mainAbout:
                startActivity(new Intent(this, AboutActivity.class));
                return true;
            case R.id.mainSetting:
                startActivity(new Intent(this, SettingActivity.class));
                break;
        }
        return false;
    }


    /**
     * Результат выбора транспорта
     */
    ActivityResultLauncher<Intent> activityForResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        int id = result.getData().getIntExtra("vehicle_id", -1);

                        SharedPreferences.Editor editor = sharedPreferences.edit();

                        editor.putInt("currentVehicleId", id);
                        editor.apply();

                        setCurrentVehicle(id);
                    }
                }
            });
}
