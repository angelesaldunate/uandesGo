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
import com.example.angeles.uandesgo.db.User;

import java.util.Date;
import java.util.List;

/**
 * Created by Angeles on 4/3/2018.
 */

public class RoutesAdapter extends ArrayAdapter<Route> {
    public RoutesAdapter(Context context, List<Route> forms) {
        super(context, 0, forms);
    }
    private static final String DATABASE_NAME = "uandesGo_db";


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
        final TextView fruser = (TextView) convertView.findViewById(R.id.textView_usuario_sentido);

        TextView frhour = (TextView) convertView.findViewById(R.id.textViewhora);
        TextView frquantity = (TextView) convertView.findViewById(R.id.textViewgl);
        new Thread(new Runnable() {
            @Override
            public void run() {
                final Place fp = appDatabase.placeDao().getPlacebyId(place.getPlaceId());
                final User up = appDatabase.userDao().getUserById(place.getUserId());

                Handler mainHandler = new Handler(getContext().getMainLooper());
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        frdes.setText(fp.getName());
                        fruser.setText(up.getName());

                    }
                });



            }}).start();

        // Populate the data into the template view using the data object
        frhour.setText(place.getDep_time());
        boolean goinlv = place.getStarting_point();
        String gl = "Voy a La Universidad";

        if (!goinlv){
             gl = "Vuelvo de La Universidad";
        }

        frquantity.setText(gl);

        // Return the completed view to render on screen
        return convertView;
    }
}