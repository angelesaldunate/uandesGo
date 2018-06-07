package com.example.angeles.uandesgo;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.angeles.uandesgo.db.AppDatabase;
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
    private static final String DATABASE_NAME = "movies_db";


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final Route place = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_route_adapter, parent, false);
        }
        final AppDatabase appDatabase = Room.databaseBuilder(getContext(),AppDatabase.class, DATABASE_NAME).fallbackToDestructiveMigration().build();

        // Lookup view for data population
        final TextView frdes = (TextView) convertView.findViewById(R.id.textViewdestino);
        TextView frhour = (TextView) convertView.findViewById(R.id.textViewhora);
        TextView frquantity = (TextView) convertView.findViewById(R.id.textViewcapacidad);
        new Thread(new Runnable() {
            @Override
            public void run() {
                final Place fp = appDatabase.placeDao().getPlacebyId(place.getPlaceId());

                Handler mainHandler = new Handler(getContext().getMainLooper());
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        frdes.setText(fp.getName());

                    }
                });



            }}).start();

        // Populate the data into the template view using the data object
        frhour.setText(place.getDep_time().toString());
        frquantity.setText(String.valueOf(place.getQuantity()));

        // Return the completed view to render on screen
        return convertView;
    }
}