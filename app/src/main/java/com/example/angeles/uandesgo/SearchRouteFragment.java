package com.example.angeles.uandesgo;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.angeles.uandesgo.db.AppDatabase;
import com.example.angeles.uandesgo.db.RequestedRoute.RequestedRoute;
import com.example.angeles.uandesgo.db.Route.Route;
import com.example.angeles.uandesgo.db.User.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class SearchRouteFragment extends Fragment {
    private static final String DATABASE_NAME = "uandesGo_db";
    private static AppDatabase appDatabase;
    private List<Route> all;
    private iComunicator mListener;
    private CredentialManage credentialManager;

    public SearchRouteFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        credentialManager = mListener.getCredentialManage();
        appDatabase = mListener.getDb();
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){

        final ListView lv = (ListView) view.findViewById(R.id.list_allRoutes);
        final String value1 = credentialManager.getEmail();
        Log.d("MAAAAAILL",value1, null);

        new Thread(new Runnable() {
            @Override
            public void run() {
                final User u = appDatabase.userDao().getOneUser(value1);
                all = appDatabase.routeDao().getAllNotMineRoutes(u.getUid());
                final List<Route> filtered_routes = new ArrayList<Route>() ;
                Long currentTime = System.currentTimeMillis();
                for (int i = 0; i<all.size();i++){
                    Route posible_route = all.get(i);
                    long timeDiff = Long.valueOf(String.valueOf(posible_route.getDep_time()))+15*60*1000 - currentTime;
                    if (timeDiff > 0) {
                        filtered_routes.add(all.get(i));
                    }
                }

                Handler mainHandler = new Handler(getActivity().getMainLooper());
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        final RoutesAdapter adapter = new RoutesAdapter(getContext(), filtered_routes);
                        lv.setAdapter(adapter);
                        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                Fragment info_route = new RouteInfoFragment();
                                Bundle ide = new Bundle();
                                ide.putInt("route_id",adapter.getItem(i).getRid());
                                info_route.setArguments(ide);
                                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.framenew,info_route).addToBackStack("null").commit();


                            }
                        });

                    }
                });

            }
        }) .start();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_route, container, false);
    }

    @Override
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
}
