package com.sakhhome.vehicle.adaptors;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sakhhome.vehicle.R;
import com.sakhhome.vehicle.models.OilChange;

import java.text.DecimalFormat;
import java.util.List;

public class OilChangeAdaptor extends BaseAdapter {

    private List<OilChange> list;
    private LayoutInflater inflater;

    public OilChangeAdaptor(List<OilChange> list, LayoutInflater inflater){
        this.list = list;
        this.inflater = inflater;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null){
            view = inflater.inflate(R.layout.view_oil_change_list_item, viewGroup, false);
        }

        TextView txtDate = view.findViewById(R.id.txtDate);
        TextView txtOdometr = view.findViewById(R.id.txtOdometr);
        TextView txtOilName = view.findViewById(R.id.txtOilName);
        TextView txtSum = view.findViewById(R.id.txtSum);

        OilChange oilChange = (OilChange) getItem(i);

        Double sum = oilChange.getOilPrice() * oilChange.getOilLiter();

        txtDate.setText(String.format(" %s ", oilChange.getDate()));
        txtDate.setTextColor(Color.RED);

        txtOdometr.setText( String.format(" %s %s", oilChange.getOdometr(), view.getResources().getText(R.string.km)));
        txtOdometr.setTextColor(Color.BLUE);

        txtOilName.setText(String.format("%s %s, %s %s",
                view.getResources().getText(R.string.odometr_oil_motor), oilChange.getOilName(), oilChange.getOilLiter(), view.getResources().getText(R.string.litre)));

        txtSum.setText(String.format(" %s %s", new DecimalFormat("0.00").format(sum), view.getResources().getText(R.string.rub)));
        txtSum.setTextColor(Color.rgb(100,205,50));

        return view;
    }
}
