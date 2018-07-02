package com.example.angeles.uandesgo;

import android.annotation.SuppressLint;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
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

import org.w3c.dom.Text;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 * Created by Angeles on 4/3/2018.
 */

public class RoutesAdapter extends ArrayAdapter<Route> {
    private iComunicator mListener;
    private AppDatabase appDatabase;
    private LayoutInflater lf;
    private List<ViewHolder> lstHolders;
    private Handler mHandler = new Handler();
    private Runnable updateRemainingTimeRunnable = new Runnable() {
        @Override
        public void run() {
            synchronized (lstHolders) {
                Calendar cal = Calendar.getInstance();
                long offset = cal.get(Calendar.ZONE_OFFSET) + cal.get(Calendar.DST_OFFSET);
                long currentTime = System.currentTimeMillis() + offset;
                for (ViewHolder holder : lstHolders) {
                    holder.updateTimer(currentTime);
                }
            }
        }
    };

    public RoutesAdapter(Context context, List<Route> forms) {
        super(context, 0, forms);
        lf = LayoutInflater.from(context);
        lstHolders = new ArrayList<>();
        mListener = (iComunicator) context;
        appDatabase = mListener.getDb();
        startUpdateTimer();
    }

    private void startUpdateTimer() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                mHandler.post(updateRemainingTimeRunnable);
            }
        }, 1000, 1000);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        ViewHolder holder = null;
        final Route route = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = lf.inflate(R.layout.list_route_adapter, parent, false);
            holder.tvTimer = (TextView) convertView.findViewById(R.id.timerTextView);
            convertView.setTag(holder);
            synchronized (lstHolders) {
                lstHolders.add(holder);
            }
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.setData(route);

        // Lookup view for data population
        final TextView driverTextView = (TextView) convertView.findViewById(R.id.driverTextView);
        final TextView originTextView = convertView.findViewById(R.id.originTextView);
        final TextView destinationTextView = convertView.findViewById(R.id.destinationTextView);
        final ImageView directionIcon = convertView.findViewById(R.id.directionIcon);
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

                    }
                });
            }}).start();

        // Return the completed view to render on screen
        return convertView;
    }

    private class ViewHolder {
        TextView tvTimer;
        Route route;

        public void setData(Route route) {
            this.route = route;
            updateTimer(System.currentTimeMillis());

        }

        public void updateTimer(long currentTime) {
            long timeDiff = Long.valueOf(String.valueOf(route.getDep_time()))+15*60*1000 - currentTime;
            if (timeDiff > 0) {
                int seconds = (int) (timeDiff / 1000) % 60;
                int minutes = (int) ((timeDiff / (1000 * 60)) % 60);
                int hours = (int) ((timeDiff / (1000 * 60 * 60)) % 24);
                tvTimer.setText(hours + ":" + minutes + ":" + seconds);
            } else {
                tvTimer.setText("Expirado!!");
            }
        }
    }
}