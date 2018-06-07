package com.example.angeles.uandesgo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.angeles.uandesgo.db.Place;
import com.example.angeles.uandesgo.db.Route;

import java.util.List;

/**
 * Created by Angeles on 4/3/2018.
 */

public class RoutesAdapter extends ArrayAdapter<Route> {
    public RoutesAdapter(Context context, List<Route> forms) {
        super(context, 0, forms);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Route place = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_route_adapter, parent, false);
        }
        // Lookup view for data population
        TextView frdes = (TextView) convertView.findViewById(R.id.textViewdestino);
        TextView frhour = (TextView) convertView.findViewById(R.id.textViewhora);
        TextView frquantity = (TextView) convertView.findViewById(R.id.textViewcapacidad);

        // Populate the data into the template view using the data object
        frdes.setText(String.valueOf(place.getPlaceId()));
        frhour.setText(place.getDep_time().toString());
        frquantity.setText(String.valueOf(place.getQuantity()));

        // Return the completed view to render on screen
        return convertView;
    }
}