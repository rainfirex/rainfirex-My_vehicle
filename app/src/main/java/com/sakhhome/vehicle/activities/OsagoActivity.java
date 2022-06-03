package com.sakhhome.vehicle.activities;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.text.format.Formatter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.sakhhome.vehicle.R;
import com.sakhhome.vehicle.models.Osago;
import com.sakhhome.vehicle.models.Vehicle;
import com.sakhhome.vehicle.utils.Utils;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.security.Permission;
import java.util.Calendar;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import static android.os.Build.VERSION.SDK_INT;

public class OsagoActivity extends AppCompatActivity {

    private EditText txtDateEnd, txtDateReg;
    private ImageView imageOsago;

    private Bitmap selectImage;
    private Vehicle currVehicle;
    private Osago currentOsago;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_osago);

        setTitle(getResources().getText(R.string.osago_title));

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        int vehicleId = sharedPreferences.getInt("currentVehicleId", -1);

        currVehicle = Vehicle.get(this, vehicleId);

        if (vehicleId == -1 || currVehicle == null) return;

        imageOsago = findViewById(R.id.imageOsago);

        txtDateReg = findViewById(R.id.txtDateReg);
        txtDateEnd = findViewById(R.id.txtDateEnd);

        Button btnDateEnd = findViewById(R.id.btnDateEnd);
        btnDateEnd.setOnClickListener(btnDate_click);

        Button btnDateReg = findViewById(R.id.btnDateReg);
        btnDateReg.setOnClickListener(btnDate_click);

        Button btnAddOsago = findViewById(R.id.btnAddOsago);
        btnAddOsago.setOnClickListener(btnAddOsago_click);
    }

    @Override
    protected void onStart() {
        super.onStart();

        loadOsago(getApplicationContext());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_osago, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.addOsago){
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            resultLoadImage.launch(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Загрузка изображения
     */
    ActivityResultLauncher<Intent> resultLoadImage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK){
                        Intent intent = result.getData();
                        final Uri uri = intent.getData();
                        try {
                            final InputStream inputStream = getContentResolver().openInputStream(uri);
                            Bitmap t = BitmapFactory.decodeStream(inputStream);

                            selectImage = Utils.createBitmapBySize(getBaseContext(), t,0,0);
                            imageOsago.setImageBitmap(selectImage);

                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
    );

    /**
     * Сохранить
     */
    View.OnClickListener btnAddOsago_click = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            String dateReg = txtDateReg.getText().toString().trim();
            String dateEnd = txtDateEnd.getText().toString().trim();

            if (selectImage == null){
                Toast.makeText(view.getContext(), view.getResources().getText(R.string.osago_err_img), Toast.LENGTH_SHORT).show();
                return;
            }

            if (dateReg.equalsIgnoreCase("")){
                Toast.makeText(view.getContext(), view.getResources().getText(R.string.osago_err_date_reg), Toast.LENGTH_SHORT).show();
                txtDateReg.requestFocus();
                return;
            }

            if (dateEnd.equalsIgnoreCase("")){
                Toast.makeText(view.getContext(), view.getResources().getText(R.string.osago_err_date_end), Toast.LENGTH_SHORT).show();
                txtDateEnd.requestFocus();
                return;
            }

            CharSequence txtResult;
            if (currentOsago == null){
                Osago.create(view.getContext(),dateReg, dateEnd, selectImage, currVehicle.getId());

                txtResult = view.getResources().getText(R.string.osago_create);
            }
            else{
                currentOsago.setDate_reg(dateReg);
                currentOsago.setDate_end(dateEnd);
                currentOsago.setOsago(selectImage);
                currentOsago.save(view.getContext());

                txtResult = view.getResources().getText(R.string.osago_update);
            }
            Toast.makeText(view.getContext(), txtResult, Toast.LENGTH_SHORT).show();
        }
    };

    /**
     * Выбор даты
     */
    View.OnClickListener btnDate_click = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int el = view.getId();

            DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    String dateString = String.format("%s.%s.%s", Utils.addZero(day), Utils.addZero(month), year);
                    if (el == R.id.btnDateReg){
                        txtDateReg.setText(dateString);
                    }
                    if (el == R.id.btnDateEnd){
                        txtDateEnd.setText(dateString);
                    }
                }
            };

            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH) + 1;
            int day = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(view.getContext(), dateSetListener, year,month,day);
            datePickerDialog.show();
        }
    };

    /**
     * Загрузка
     * @param context
     */
    private void loadOsago(Context context){
        currentOsago = currVehicle.getOsago(context);

        txtDateReg.setText(currentOsago.getDate_reg());
        txtDateEnd.setText(currentOsago.getDate_end());
        selectImage = currentOsago.getOsago();
        imageOsago.setImageBitmap(currentOsago.getOsago());
    }
}
