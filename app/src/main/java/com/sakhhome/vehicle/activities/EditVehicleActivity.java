package com.sakhhome.vehicle.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.sakhhome.vehicle.R;
import com.sakhhome.vehicle.database.TableVehicle;
import com.sakhhome.vehicle.models.Vehicle;
import com.sakhhome.vehicle.modules.VehicleParams;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class EditVehicleActivity extends AppCompatActivity {

    private EditText txtInputTitle, txtInputColor, txtInputOdometr, txtInputEngine, txtInputEnginePower, txtInputBody, txtInputTankLiters, txtInputMass;

    private ImageView imgSelectAvatar;

    private Spinner spSelectYear, spSelectMark, spSelectModel;

    private final int imgAvatarCode = 1;

    private Bitmap selectImage;

    private VehicleParams vp;

    private ArrayAdapter<String> adapterYears, adapterMark;

    private Vehicle editVehicle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_vehicle);

        vp = new VehicleParams(getApplicationContext());
        String[] years = vp.getYears();
        String[] marks = vp.getMark();

        spSelectYear  = findViewById(R.id.spSelectYear);
        spSelectMark  = findViewById(R.id.spSelectMark);
        spSelectModel = findViewById(R.id.spSelectModel);

        txtInputTitle   = findViewById(R.id.txtInputTitle);
        txtInputColor   = findViewById(R.id.txtInputColor);
        txtInputOdometr = findViewById(R.id.txtInputOdometr);
        txtInputEngine      = findViewById(R.id.txtInputEngine);
        txtInputEnginePower = findViewById(R.id.txtInputEnginePower);
        txtInputBody        = findViewById(R.id.txtInputBody);
        txtInputTankLiters  = findViewById(R.id.txtInputTankLiters);
        txtInputMass        = findViewById(R.id.txtInputMass);

        imgSelectAvatar = findViewById(R.id.imgSelectAvatar);
        imgSelectAvatar.setImageResource(R.drawable.car_avatar);
        imgSelectAvatar.setOnClickListener(imgSelectAvatar_click);

        adapterYears = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, years);
        spSelectYear.setAdapter(adapterYears);

        adapterMark = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, marks);
        spSelectMark.setAdapter(adapterMark);loadEditVehicle();

        spSelectMark.setOnItemSelectedListener(spSelectModel_click);

        Button btnEditVehicle = findViewById(R.id.btnEditVehicle);

        if (editVehicle == null)
        {
            setTitle(R.string.activity_create_vehicle);
            btnEditVehicle.setOnClickListener(btnAddVehicle_click);
        } else
        {
            setTitle(R.string.activity_edit_vehicle);
            btnEditVehicle.setText(getResources().getText(R.string.app_edit_vehicle));
            btnEditVehicle.setOnClickListener(btnEditVehicle_click);
        }
    }

    /**
     * Выбор модели транспорта
     */
    AdapterView.OnItemSelectedListener spSelectModel_click = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            String selectMark = adapterView.getItemAtPosition(i).toString();
            String[] models = vp.getModel(selectMark);

            ArrayAdapter<String> adapterModel = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_list_item_1, models);
            spSelectModel.setAdapter(adapterModel);

            // Если транспорт был выбран на редактирование
            if(editVehicle != null){
                int p = adapterModel.getPosition(editVehicle.getModel());
                spSelectModel.setSelection(p);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };

    /**
     * Выбор изображения
     */
    View.OnClickListener imgSelectAvatar_click = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(Intent.ACTION_PICK );
            intent.setType("image/*");
            startActivityForResult(intent, imgAvatarCode);
        }
    };

    /**
     * Добавить
     */
    View.OnClickListener btnAddVehicle_click = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String title = txtInputTitle.getText().toString().trim();
            String color = txtInputColor.getText().toString().trim();
            String odometr = txtInputOdometr.getText().toString().trim();
            String year = spSelectYear.getSelectedItem().toString().trim();
            String mark = spSelectMark.getSelectedItem().toString().trim();
            String model = spSelectModel.getSelectedItem().toString().trim();
            String engine = txtInputEngine.getText().toString().trim();
            String enginePower =  txtInputEnginePower.getText().toString().trim();
            String body = txtInputBody.getText().toString().trim();
            String tank = txtInputTankLiters.getText().toString().trim();
            String mass = txtInputMass.getText().toString().trim();
            if(
                    title.equalsIgnoreCase("")
                    || color.equalsIgnoreCase("")
                    || odometr.equalsIgnoreCase("")
                    || mark.equalsIgnoreCase("")
                    || model.equalsIgnoreCase("")
                    || year.equalsIgnoreCase("")
            )
            {
                Toast.makeText(view.getContext(), "Не запонены поля", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                Vehicle vh = Vehicle.create(getApplicationContext(),
                        title, mark, model, Integer.parseInt(year), color, Integer.parseInt(odometr), selectImage,
                        engine, Double.parseDouble(enginePower), body, Double.parseDouble(tank), Integer.parseInt(mass));

                Toast.makeText(view.getContext(), String.format("Транспорт добавлен: %s", vh.getTitle()), Toast.LENGTH_SHORT).show();
            }
            catch (Exception ex){
                Toast.makeText(view.getContext(), String.format("Ошибка: %s", ex.getMessage()), Toast.LENGTH_SHORT).show();
            }

            finish();
        }
    };

    /**
     * Обновить
     */
    View.OnClickListener btnEditVehicle_click = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String title = txtInputTitle.getText().toString().trim();
            String color = txtInputColor.getText().toString().trim();
            String odometr = txtInputOdometr.getText().toString().trim();
            String year = spSelectYear.getSelectedItem().toString().trim();
            String mark = spSelectMark.getSelectedItem().toString().trim();
            String model = spSelectModel.getSelectedItem().toString().trim();
            String engine = txtInputEngine.getText().toString().trim();
            String enginePower =  txtInputEnginePower.getText().toString().trim();
            String body = txtInputBody.getText().toString().trim();
            String tank = txtInputTankLiters.getText().toString().trim();
            String mass = txtInputMass.getText().toString().trim();

            if(
                    title.equalsIgnoreCase("")
                            || color.equalsIgnoreCase("")
                            || odometr.equalsIgnoreCase("")
                            || mark.equalsIgnoreCase("")
                            || model.equalsIgnoreCase("")
                            || year.equalsIgnoreCase("")
            )
            {
                Toast.makeText(view.getContext(), "Не запонены поля", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                editVehicle.setTitle(title);
                editVehicle.setMark(mark);
                editVehicle.setModel(model);
                editVehicle.setYear(Integer.parseInt(year));
                editVehicle.setColor(color);
                editVehicle.setOdometr(Integer.parseInt(odometr));
                editVehicle.setAvatar(selectImage);
                editVehicle.setEngine(engine);
                editVehicle.setPowerEngine(Double.parseDouble(enginePower));
                editVehicle.setBody(body);
                editVehicle.setTankLiters(Double.parseDouble(tank));
                editVehicle.setMass(Integer.parseInt(mass));
                editVehicle.save(view.getContext());

                Toast.makeText(view.getContext(), "Транспорт изменен: " + editVehicle.getTitle(), Toast.LENGTH_SHORT).show();

                Intent intent = new Intent();
                intent.putExtra("vehicle_id", editVehicle.getId());
                setResult(RESULT_OK, intent);
            }
            catch (Exception ex){
                Toast.makeText(view.getContext(), String.format("Ошибка: %s", ex.getMessage()), Toast.LENGTH_SHORT).show();
            }

            finish();
        }
    };

    /**
     * Загрузка изображения
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case imgAvatarCode:
                if(resultCode == RESULT_OK){
                    final Uri uri = data.getData();

                    try {
                        final InputStream inputStream = getContentResolver().openInputStream(uri);
                        selectImage = BitmapFactory.decodeStream(inputStream);
                        imgSelectAvatar.setImageBitmap(selectImage);

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    /**
     * Загружаем в поля данные
     */
    private void loadEditVehicle(){
        Intent intent = getIntent();
        int id = intent.getIntExtra(TableVehicle.KEY_ID, -1);
        if (id != -1){
            editVehicle = Vehicle.get(getApplicationContext(), id);

            txtInputTitle.setText(editVehicle.getTitle().trim());
            txtInputColor.setText(editVehicle.getColor().trim());
            txtInputOdometr.setText(String.valueOf(editVehicle.getOdometr()).trim());


            txtInputEngine.setText(editVehicle.getEngine().trim());
            txtInputEnginePower.setText(String.valueOf(editVehicle.getPowerEngine()));
            txtInputBody.setText(editVehicle.getBody().trim());
            txtInputTankLiters.setText(String.valueOf(editVehicle.getTankLiters()));
            txtInputMass.setText(String.valueOf(editVehicle.getMass()));

            int positionYear = adapterYears.getPosition(String.valueOf(editVehicle.getYear()));
            int positionMark = adapterMark.getPosition(editVehicle.getMark());

            spSelectYear.setSelection(positionYear);
            spSelectMark.setSelection(positionMark);

            if (editVehicle.getAvatar() != null)
                imgSelectAvatar.setImageBitmap(editVehicle.getAvatar());
            else
                imgSelectAvatar.setImageResource(R.drawable.car_avatar);

            Toast.makeText(getApplicationContext(), "Редактируем: " + editVehicle.getTitle(), Toast.LENGTH_SHORT).show();
        }
    }
}
