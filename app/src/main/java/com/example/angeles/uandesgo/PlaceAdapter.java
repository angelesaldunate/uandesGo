package com.example.angeles.uandesgo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.angeles.uandesgo.db.Place.Place;

import java.util.List;

/**
 * Created by Angeles on 4/3/2018.
 */

public class PlaceAdapter extends ArrayAdapter<Place> {
    public PlaceAdapter(Context context, List<Place> forms) {
        super(context, 0, forms);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Place place = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_place_adapter, parent, false);
        }
        // Lookup view for data population
        TextView fride = (TextView) convertView.findViewById(R.id.textViewide);

        TextView frName = (TextView) convertView.findViewById(R.id.textViewnombre);
        TextView frZOne = (TextView) convertView.findViewById(R.id.textViewzona);
        // Populate the data into the template view using the data object
        fride.setText(String.valueOf(place.getPid()));
        frName.setText(place.getName());
        frZOne.setText(place.getZone());

        // Return the completed view to render on screen
        return convertView;
    }
}