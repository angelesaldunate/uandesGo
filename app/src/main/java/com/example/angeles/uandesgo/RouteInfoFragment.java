package com.example.angeles.uandesgo;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.angeles.uandesgo.db.AppDatabase;
import com.example.angeles.uandesgo.db.RequestedRoute.RequestedRoute;
import com.example.angeles.uandesgo.db.Route.Route;
import com.example.angeles.uandesgo.db.User.User;

import java.util.Calendar;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class RouteInfoFragment extends Fragment {
    private static AppDatabase appDatabase;
    private iComunicator mListener;
    private CredentialManage credentialManager;
    private int ide_route;



    public RouteInfoFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        credentialManager = mListener.getCredentialManage();
        appDatabase = mListener.getDb();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_route_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle route_ide = this.getArguments();
        if (route_ide != null) {
            ide_route = route_ide.getInt("route_id");
        }
        new Thread(new Runnable() {
            @Override
            public void run() {

                final Route route = appDatabase.routeDao().getRoutebyId(ide_route);
                final String namePlace = appDatabase.placeDao().getPlacebyId(route.getPlaceId()).getName();
                final String nameUser = appDatabase.profileDao().getOneProfile(route.getUserId()).getName();
                final String departureTime = route.getDep_time();
                final String capacity = String.valueOf(route.getQuantity());
                final String requested = String.valueOf(appDatabase.requestedDao().getAllRequestsForRoute(route.getRid()));
                final Boolean direction = route.getStarting_point();

                Handler mainHandler = new Handler(getActivity().getMainLooper());
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        TextView name_Place = (TextView) view.findViewById(R.id.info_name_place);
                        TextView name_user = (TextView) view.findViewById(R.id.info_name_user);
                        TextView dep_time = (TextView) view.findViewById(R.id.info_departure_time);
                        TextView actual_capacity = (TextView) view.findViewById(R.id.info_capacity);
                        if (!direction) {
                            name_Place.setText("UAndes a " + namePlace);
                        } else {
                            name_Place.setText(namePlace + " a UAndes");
                        }
                        name_user.setText(nameUser);
                        dep_time.setText(getDate(Long.valueOf(departureTime)+15*60*1000));
                        actual_capacity.setText(requested+"/"+capacity);

                    }
                });
            }
        }).start();
        Button buton_request = (Button) view.findViewById(R.id.button_request);
        buton_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        User current = appDatabase.userDao().getOneUser(credentialManager.getEmail());
                        RequestedRoute rr= new RequestedRoute();
                        rr.setRouteId(ide_route);
                        rr.setUserId(current.getUid());
                        appDatabase.requestedDao().insertAll(rr);
                    }
                }).start();

                Toast.makeText(getContext(),"Ruta Pedida",Toast.LENGTH_SHORT).show();
                Fragment requested_fragment = new RequestedRoutesFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.framenew,requested_fragment).addToBackStack("null").commit();

            }

        });


    }
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof iComunicator) {
            mListener = (iComunicator) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private String getDate(long time) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time-4*60*60*1000);
        String date = DateFormat.format("dd-MM-yyyy HH:mm:ss", cal).toString();
        return date;
    }
}


