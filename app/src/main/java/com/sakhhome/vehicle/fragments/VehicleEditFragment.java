package com.sakhhome.vehicle.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.sakhhome.vehicle.R;
import com.sakhhome.vehicle.models.Vehicle;
import com.sakhhome.vehicle.modules.VehicleParams;
import com.sakhhome.vehicle.utils.Utils;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.regex.Pattern;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import static android.app.Activity.RESULT_OK;

public class VehicleEditFragment extends Fragment {

    private EditText txtInputTitle, txtInputColor, txtInputOdometr, txtInputEngine, txtInputEnginePower, txtInputBody, txtInputTankLiters, txtInputMass;

    private ImageView imgSelectAvatar;

    private Spinner spSelectYear, spSelectMark, spSelectModel;

    private Bitmap selectImage;

    private VehicleParams vp;

    private ArrayAdapter<String> adapterYears, adapterMark;

    private Vehicle editVehicle;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_vehicle_add, null);

        Context context = v.getContext();

        vp = new VehicleParams(context);
        String[] years = vp.getYears();
        String[] marks = vp.getMark();

        spSelectYear  = v.findViewById(R.id.spSelectYear);
        spSelectMark  = v.findViewById(R.id.spSelectMark);
        spSelectModel = v.findViewById(R.id.spSelectModel);

        txtInputTitle   = v.findViewById(R.id.txtInputTitle);
        txtInputColor   = v.findViewById(R.id.txtInputColor);
        txtInputOdometr = v.findViewById(R.id.txtInputOdometr);
        txtInputEngine      = v.findViewById(R.id.txtInputEngine);
        txtInputEnginePower = v.findViewById(R.id.txtInputEnginePower);
        txtInputBody        = v.findViewById(R.id.txtInputBody);
        txtInputTankLiters  = v.findViewById(R.id.txtInputTankLiters);
        txtInputMass        = v.findViewById(R.id.txtInputMass);

        imgSelectAvatar = v.findViewById(R.id.imgSelectAvatar);
        imgSelectAvatar.setImageResource(R.drawable.car_avatar);
        imgSelectAvatar.setOnClickListener(imgSelectAvatar_click);

        adapterYears = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, years);
        spSelectYear.setAdapter(adapterYears);

        adapterMark = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, marks);
        spSelectMark.setAdapter(adapterMark);

        loadEditVehicle();

        spSelectMark.setOnItemSelectedListener(spSelectModel_click);

        Button btnEditVehicle = v.findViewById(R.id.btnEditVehicle);

        if (editVehicle == null)
        {
            getActivity().setTitle(R.string.select_vehicle_create_title);
            btnEditVehicle.setOnClickListener(btnAddVehicle_click);
        } else
        {
            getActivity().setTitle(R.string.select_vehicle_edit_title);
            btnEditVehicle.setText(getResources().getText(R.string.edit_vehicle_btn_change));
            btnEditVehicle.setOnClickListener(btnEditVehicle_click);
        }
        return v;
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
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");

            activityForResult.launch(intent);
        }
    };

    /**
     * Загрузка изображения
     * @param requestCode
     * @param resultCode
     * @param data
     */
    ActivityResultLauncher<Intent> activityForResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK){
                        Intent intent = result.getData();
                        final Uri uri = intent.getData();
                        try {
                            final InputStream inputStream = getActivity().getContentResolver().openInputStream(uri);
                            Bitmap t = BitmapFactory.decodeStream(inputStream);
                            selectImage = Utils.createBitmapBySize(getContext(), t, 0, 0);
                            imgSelectAvatar.setImageBitmap(selectImage);

                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
    );

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

            if (title.equalsIgnoreCase("")){
                Toast.makeText(view.getContext(), view.getResources().getText(R.string.edit_vehicle_err_title), Toast.LENGTH_SHORT).show();
                txtInputTitle.requestFocus();
                return;
            }
            if (odometr.equalsIgnoreCase("") || !odometr.matches("([0-9]+)")){
                Toast.makeText(view.getContext(), view.getResources().getText(R.string.edit_vehicle_err_odometr), Toast.LENGTH_SHORT).show();
                txtInputOdometr.requestFocus();
                return;
            }
            if (mark.equalsIgnoreCase("")){
                Toast.makeText(view.getContext(), view.getResources().getText(R.string.edit_vehicle_err_mark), Toast.LENGTH_SHORT).show();
                spSelectMark.requestFocus();
                return;
            }
            if (model.equalsIgnoreCase("")){
                Toast.makeText(view.getContext(), view.getResources().getText(R.string.edit_vehicle_err_model), Toast.LENGTH_SHORT).show();
                spSelectModel.requestFocus();
                return;
            }
            if (year.equalsIgnoreCase("")){
                Toast.makeText(view.getContext(), view.getResources().getText(R.string.edit_vehicle_err_year), Toast.LENGTH_SHORT).show();
                spSelectYear.requestFocus();
                return;
            }
            if (engine.equalsIgnoreCase("")){
                Toast.makeText(view.getContext(), view.getResources().getText(R.string.edit_vehicle_err_engine_name), Toast.LENGTH_SHORT).show();
                txtInputEngine.requestFocus();
                return;
            }
            if (enginePower.equalsIgnoreCase("") || !enginePower.matches("([0-9]+)(.)([0-9]+)")){
                Toast.makeText(view.getContext(), view.getResources().getText(R.string.edit_vehicle_err_engine_v), Toast.LENGTH_SHORT).show();
                txtInputEnginePower.requestFocus();
                return;
            }
            if (!tank.equalsIgnoreCase("") && !tank.matches("([0-9]{2,3})")){
                Toast.makeText(view.getContext(), view.getResources().getText(R.string.edit_vehicle_err_tank), Toast.LENGTH_SHORT).show();
                txtInputTankLiters.requestFocus();
                return;
            }
            else {
                tank = "0.0";
            }
            if (!mass.equalsIgnoreCase("") && !mass.matches("([0-9]{4})")){
                Toast.makeText(view.getContext(), view.getResources().getText(R.string.edit_vehicle_err_mass), Toast.LENGTH_SHORT).show();
                txtInputMass.requestFocus();
                return;
            }
            else {
                mass = "0";
            }


            try {
                Vehicle vh = Vehicle.create(view.getContext(),
                        title, mark, model, Integer.parseInt(year), color, Integer.parseInt(odometr), selectImage,
                        engine, Double.parseDouble(enginePower), body, Double.parseDouble(tank), Integer.parseInt(mass));

                Toast.makeText(view.getContext(), String.format("Транспорт добавлен: %s", vh.getTitle()), Toast.LENGTH_SHORT).show();

                returnToList();
            }
            catch (Exception ex){
                Toast.makeText(view.getContext(), String.format("Ошибка: %s", ex.getMessage()), Toast.LENGTH_LONG).show();
                ex.printStackTrace();
            }

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

            if (title.equalsIgnoreCase("")){
                Toast.makeText(view.getContext(), view.getResources().getText(R.string.edit_vehicle_err_title), Toast.LENGTH_SHORT).show();
                txtInputTitle.requestFocus();
                return;
            }
            if (odometr.equalsIgnoreCase("") || !odometr.matches("([0-9]+)")){
                Toast.makeText(view.getContext(), view.getResources().getText(R.string.edit_vehicle_err_odometr), Toast.LENGTH_SHORT).show();
                txtInputOdometr.requestFocus();
                return;
            }
            if (mark.equalsIgnoreCase("")){
                Toast.makeText(view.getContext(), view.getResources().getText(R.string.edit_vehicle_err_mark), Toast.LENGTH_SHORT).show();
                spSelectMark.requestFocus();
                return;
            }
            if (model.equalsIgnoreCase("")){
                Toast.makeText(view.getContext(), view.getResources().getText(R.string.edit_vehicle_err_model), Toast.LENGTH_SHORT).show();
                spSelectModel.requestFocus();
                return;
            }
            if (year.equalsIgnoreCase("")){
                Toast.makeText(view.getContext(), view.getResources().getText(R.string.edit_vehicle_err_year), Toast.LENGTH_SHORT).show();
                spSelectYear.requestFocus();
                return;
            }
            if (engine.equalsIgnoreCase("")){
                Toast.makeText(view.getContext(), view.getResources().getText(R.string.edit_vehicle_err_engine_name), Toast.LENGTH_SHORT).show();
                txtInputEngine.requestFocus();
                return;
            }
            if (enginePower.equalsIgnoreCase("") || !enginePower.matches("([0-9]+)(.)([0-9]+)")){
                Toast.makeText(view.getContext(), view.getResources().getText(R.string.edit_vehicle_err_engine_v), Toast.LENGTH_SHORT).show();
                txtInputEnginePower.requestFocus();
                return;
            }
            if (!tank.equalsIgnoreCase("") && !tank.matches("([0-9]{2,3})")){
                Toast.makeText(view.getContext(), view.getResources().getText(R.string.edit_vehicle_err_tank), Toast.LENGTH_SHORT).show();
                txtInputTankLiters.requestFocus();
                return;
            }
            else {
                tank = "0.0";
            }
            if (!mass.equalsIgnoreCase("") && !mass.matches("([0-9]{4})")){
                Toast.makeText(view.getContext(), view.getResources().getText(R.string.edit_vehicle_err_mass), Toast.LENGTH_SHORT).show();
                txtInputMass.requestFocus();
                return;
            }
            else {
                mass = "0";
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

                returnToList();

                Toast.makeText(view.getContext(), "Транспорт изменен: " + editVehicle.getTitle(), Toast.LENGTH_SHORT).show();
            }
            catch (Exception ex){
                Toast.makeText(view.getContext(), String.format("Ошибка: %s", ex.getMessage()), Toast.LENGTH_LONG).show();
            }
        }
    };

    /**
     * Загружаем в поля данные
     */
    private void loadEditVehicle(){
        Bundle bundle = getArguments();
        if (bundle != null){
            int id = bundle.getInt("id");
            editVehicle = Vehicle.get(getContext(), id);
        }

        if (editVehicle != null){
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

            if (editVehicle.getAvatar() != null){
                imgSelectAvatar.setImageBitmap(editVehicle.getAvatar());
                selectImage = editVehicle.getAvatar();
            }
            else
                imgSelectAvatar.setImageResource(R.drawable.car_avatar);

            Toast.makeText(getContext(), "Редактируем: " + editVehicle.getTitle(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Вернуться к списку
     */
    private void returnToList(){
        getParentFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentVehicle, new VehicleListFragment())
                .commit();
    }
}
