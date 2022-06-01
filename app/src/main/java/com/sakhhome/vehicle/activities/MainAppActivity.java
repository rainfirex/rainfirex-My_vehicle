package com.sakhhome.vehicle.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import com.sakhhome.vehicle.models.Vehicle;
import com.sakhhome.vehicle.utils.Utils;

public class MainAppActivity extends AppCompatActivity implements View.OnClickListener {

    TextView txtCurrentVehicle, txtCurrentOdometr, txtCurrentColor, txtCurrentMarkModelYear, txtCurrentEngine,
            txtCurrentBody, txtCurrentTank, txtCurrentMass, txtChangeVehicle;

    ImageView imgCurrentVehicle;

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

        setTitle(R.string.activity_main);

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

        ImageButton imgBtnOpenFuel = findViewById(R.id.imgBtnOpenFuel);
        imgBtnOpenFuel.setOnClickListener(this);

        ImageButton imgBtnOpenOil = findViewById(R.id.imgBtnOpenOil);
        imgBtnOpenOil.setOnClickListener(this);
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
            case R.id.imgBtnOpenFuel:
                startActivity(new Intent(this, RefuelingActivity.class));
                break;
            case R.id.imgBtnOpenOil:
                startActivity(new Intent(this, OilChangeActivity.class));
                break;
            case R.id.txtChangeVehicle:
                activityForResult.launch(new Intent(this, SelectVehicleActivity.class));
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
     * @param int id
     */
    private void setCurrentVehicle(int id) {
        if (id == -1) return;

        currentVehicle = Vehicle.get(getApplicationContext(), id);

        if (currentVehicle == null) return;

        txtCurrentVehicle.setText(currentVehicle.getTitle());
        txtCurrentMarkModelYear.setText(String.format("%s %s %s г.", currentVehicle.getMark(), currentVehicle.getModel(), currentVehicle.getYear()));
        txtCurrentColor.setText(String.format("Цвет: %s", currentVehicle.getColor()));
        txtCurrentOdometr.setText(String.format("Пробег: %s КМ", currentVehicle.getOdometr()));
        txtCurrentEngine.setText(String.format("Двигатель: %s, объем %s л.", currentVehicle.getEngine(), currentVehicle.getPowerEngine()));
        txtCurrentBody.setText(String.format("Кузов: %s", currentVehicle.getBody()));
        txtCurrentTank.setText(String.format("Объем бака: %s л.", currentVehicle.getTankLiters()));
        txtCurrentMass.setText(String.format("Масса: %s тонн", currentVehicle.getMass()));

        if (currentVehicle.getAvatar() != null)
            imgCurrentVehicle.setImageBitmap(currentVehicle.getAvatar());
        else
            imgCurrentVehicle.setImageResource(R.drawable.car_avatar);
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
