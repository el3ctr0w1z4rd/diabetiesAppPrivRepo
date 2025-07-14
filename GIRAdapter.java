package com.example.diabeticmealplannerapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class GIRAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final int xmlResource;
    private final List<String> list;

    public GIRAdapter(@NonNull Context context, int resource, @NonNull List<String> objects) {
        super(context, resource, objects);
        this.context = context;
        this.xmlResource = resource;
        this.list = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            row = inflater.inflate(xmlResource, parent, false);
        }

        TextView featureText = row.findViewById(R.id.appFeatures);
        featureText.setText(list.get(position));

        return row;
    }
}
