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

import java.util.Date;
import java.util.List;

/**
 * Created by Angeles on 4/3/2018.
 */

public class RoutesAdapter extends ArrayAdapter<Route> {
    private iComunicator mListener;
    private AppDatabase appDatabase;
    public RoutesAdapter(Context context, List<Route> forms) {
        super(context, 0, forms);
        mListener = (iComunicator) context;
        appDatabase = mListener.getDb();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final Route route = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_route_adapter, parent, false);
        }

        // Lookup view for data population
        final TextView driverTextView = (TextView) convertView.findViewById(R.id.driverTextView);
        final TextView originTextView = convertView.findViewById(R.id.originTextView);
        final TextView destinationTextView = convertView.findViewById(R.id.destinationTextView);
        final ImageView directionIcon = convertView.findViewById(R.id.directionIcon);
        final TextView dateTextView = convertView.findViewById(R.id.dateTextView);
        new Thread(new Runnable() {
            @Override
            public void run() {
                final Place place = appDatabase.placeDao().getPlacebyId(route.getPlaceId());
                final User up = appDatabase.userDao().getUserById(route.getUserId());
                final Profile profile = appDatabase.profileDao().getOneProfile(route.getUserId());

                Handler mainHandler = new Handler(getContext().getMainLooper());
                mainHandler.post(new Runnable() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void run() {
                        boolean direction = route.getStarting_point();
                        driverTextView.setText(profile.getName());
                        if (direction){
                            originTextView.setText(place.getName());
                            destinationTextView.setText("UAndes");
                        } else {
                            destinationTextView.setText(place.getName());
                            originTextView.setText("UAndes");
                        }
                        Long date_string = Long.valueOf(route.getDep_time());
                        dateTextView.setText(mListener.getDate(date_string));

                    }
                });
            }}).start();

        // Return the completed view to render on screen
        return convertView;
    }
}