package com.example.angeles.uandesgo;

import android.annotation.SuppressLint;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.angeles.uandesgo.db.AppDatabase;
import com.example.angeles.uandesgo.db.Place.Place;
import com.example.angeles.uandesgo.db.Route.Route;
import com.example.angeles.uandesgo.db.User.Profile.Profile;
import com.example.angeles.uandesgo.db.User.User;

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
        final TextView driverTextView = (TextView) convertView.findViewById(R.id.driverTextView);
        final TextView originTextView = convertView.findViewById(R.id.originTextView);
        final TextView destinationTextView = convertView.findViewById(R.id.destinationTextView);
        final ImageView directionIcon = convertView.findViewById(R.id.directionIcon);
        new Thread(new Runnable() {
            @Override
            public void run() {
                final Place fp = appDatabase.placeDao().getPlacebyId(place.getPlaceId());
                final User up = appDatabase.userDao().getUserById(place.getUserId());
                final Profile pr = appDatabase.profileDao().getOneProfile(place.getUserId());

                Handler mainHandler = new Handler(getContext().getMainLooper());
                mainHandler.post(new Runnable() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void run() {
                        boolean direction = place.getStarting_point();
                        driverTextView.setText(pr.getName());
                        if (direction){
                            originTextView.setText(fp.getName());
                            destinationTextView.setText("UAndes");
                        } else {
                            destinationTextView.setText(fp.getName());
                            originTextView.setText("UAndes");
                        }
                    }
                });
            }}).start();

        // Return the completed view to render on screen
        return convertView;
    }
}