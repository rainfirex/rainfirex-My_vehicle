package com.sakhhome.vehicle.adaptors;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sakhhome.vehicle.R;
import com.sakhhome.vehicle.models.ReFuel;

import java.text.DecimalFormat;
import java.util.List;

public class ReFuelAdaptor extends BaseAdapter {

    private List<ReFuel> list;
    private LayoutInflater inflater;

    public ReFuelAdaptor(List<ReFuel> list, LayoutInflater inflater){
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
            view = inflater.inflate(R.layout.view_refuel_list_item, viewGroup, false);
        }

        ReFuel fuel = (ReFuel)getItem(i);

        TextView date = view.findViewById(R.id.date);
        date.setText(fuel.getDate());
        date.setTextColor(Color.RED);

        TextView odometr = view.findViewById(R.id.odometr);
        odometr.setText( String.format(" %s %s", fuel.getOdometr(), view.getResources().getText(R.string.km)));
        odometr.setTextColor(Color.BLUE);

        TextView sum = view.findViewById(R.id.sum);
        sum.setText(String.format(" %s %s", new DecimalFormat("0.00").format(fuel.getSum()), view.getResources().getText(R.string.rub)));
        sum.setTextColor(Color.rgb(100,205,50));

        return view;
    }
}
