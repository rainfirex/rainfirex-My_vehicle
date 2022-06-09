package com.sakhhome.vehicle.fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.sakhhome.vehicle.R;
import com.sakhhome.vehicle.database.TableOsago;
import com.sakhhome.vehicle.models.Osago;
import com.sakhhome.vehicle.models.Vehicle;
import com.sakhhome.vehicle.utils.DbBitmapUtility;
import com.sakhhome.vehicle.utils.Utils;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Calendar;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import static android.app.Activity.RESULT_OK;

public class OsagoFragment extends Fragment {

    private EditText txtDateEnd, txtDateReg;
    private ImageView imageOsago;

    private Bitmap selectImage;
    private Vehicle currVehicle;
    private Osago currentOsago;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_osago, null);

        imageOsago = v.findViewById(R.id.imageOsago);
        imageOsago.setOnClickListener(imageViewer_click);

        txtDateReg = v.findViewById(R.id.txtDateReg);
        txtDateEnd = v.findViewById(R.id.txtDateEnd);

        Button btnDateEnd = v.findViewById(R.id.btnDateEnd);
        btnDateEnd.setOnClickListener(btnDate_click);

        Button btnDateReg = v.findViewById(R.id.btnDateReg);
        btnDateReg.setOnClickListener(btnDate_click);

        Button btnAddOsago = v.findViewById(R.id.btnAddOsago);
        btnAddOsago.setOnClickListener(btnAddOsago_click);

        setHasOptionsMenu(true);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        loadOsago(getContext());
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_osago, menu);
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

    View.OnClickListener imageViewer_click = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            byte[] img = DbBitmapUtility.getBytes(selectImage);

            Bundle bundle = new Bundle();
            bundle.putByteArray("image", img);


            Fragment fragment =  new OsagoViewerFragment();
            fragment.setArguments(bundle);

            getParentFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentOsago,fragment)
                    .commit();
        }
    };

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
                            final InputStream inputStream = getActivity().getContentResolver().openInputStream(uri);
                            Bitmap t = BitmapFactory.decodeStream(inputStream);

                            selectImage = Utils.createBitmapBySize(getContext(), t,0,0);
                            imageOsago.setImageBitmap(selectImage);

                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
    );

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
            try {
                if (currentOsago == null){
                    currentOsago =  Osago.create(view.getContext(),dateReg, dateEnd, selectImage, currVehicle.getId());

                    Log.d("OS", "create id "+ currentOsago.getId());

                    txtResult = view.getResources().getText(R.string.osago_create);
                }
                else{
                    Log.d("OS", "update id "+ currentOsago.getId());
                    currentOsago.setDateReg(dateReg);
                    currentOsago.setDateEnd(dateEnd);
                    currentOsago.setOsago(selectImage);
                    currentOsago.save(view.getContext());

                    txtResult = view.getResources().getText(R.string.osago_update);

                }
            }
            catch (Exception e){
                txtResult = e.getMessage();
            }

            Toast.makeText(view.getContext(), txtResult, Toast.LENGTH_SHORT).show();
        }
    };

    /**
     * Загрузка
     */
    private void loadOsago(Context context) {
        Bundle bundle = getArguments();
        assert bundle != null;

         int vehicleId = bundle.getInt("vehicleId");
        currVehicle = Vehicle.get(getContext(), vehicleId);
        currVehicle.with(context, new String[]{TableOsago.TABLE});

        currentOsago = currVehicle.loadOsago(context);

        if (currentOsago == null) return;

        txtDateReg.setText(currentOsago.getDateReg());
        txtDateEnd.setText(currentOsago.getDateEnd());
        selectImage = currentOsago.getOsago();
        imageOsago.setImageBitmap(currentOsago.getOsago());
    }
}
