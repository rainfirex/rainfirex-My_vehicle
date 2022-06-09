package com.sakhhome.vehicle.fragments;

import android.app.ActionBar;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.sakhhome.vehicle.R;
import com.sakhhome.vehicle.utils.DbBitmapUtility;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class OsagoViewerFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_osago_viewer, null);



        ImageView imageViewer = v.findViewById(R.id.imageViewer);
        imageViewer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                getActivity().onBackPressed();
            }
        });

        Bundle bundle = getArguments();
        if (bundle != null){
            byte[] image = bundle.getByteArray("image");
            Bitmap bitmap = DbBitmapUtility.getImage(image);
            imageViewer.setImageBitmap(bitmap);
        }

        return v;
    }
}
