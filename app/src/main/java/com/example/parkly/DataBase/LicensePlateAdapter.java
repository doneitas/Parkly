package com.example.parkly.DataBase;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.parkly.R;

import java.util.List;

public class LicensePlateAdapter extends ArrayAdapter<LicensePlate>
{
    public LicensePlateAdapter(Context context, List<LicensePlate> licensePlates)
    {
        super(context, 0,licensePlates);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LicensePlate licensePlate = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.license_plate, parent, false);
        }
        TextView tvLicensePlate = convertView.findViewById(R.id.license_plate);
        tvLicensePlate.setText(licensePlate.getNumber());
        return convertView;
    }
}
