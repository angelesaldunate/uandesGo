package com.example.angeles.uandesgo;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.angeles.uandesgo.db.AppDatabase;
import com.example.angeles.uandesgo.db.Place;
import com.example.angeles.uandesgo.db.RequestedRoute;
import com.example.angeles.uandesgo.db.Route;
import com.example.angeles.uandesgo.db.User;

import java.util.Date;
import java.util.List;


public class SearchRouteFragment extends Fragment {
    private iComunicator mListener;
    private static final String DATABASE_NAME = "uandesGo_db";
    private List<com.example.angeles.uandesgo.db.Route> all;
    private com.example.angeles.uandesgo.db.User user;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof iComunicator) {
            mListener = (iComunicator) context;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        //final ListView lv = (ListView) view.findViewById(R.id.list_allRoutes);
        final String value1 = mListener.getSharedPreferences().getString("email_guardado",null);

        FragmentManager fm = getActivity().getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);

        if (fragment == null) {
            fragment = new CardFragment();
            fm.beginTransaction().add(R.id.fragmentContainer, fragment).commit();
        }

        /*new Thread(new Runnable() {
            @Override
            public void run() {
                final AppDatabase appDatabase = mListener.getDB();
                user = appDatabase.userDao().getOneUser(value1);
                all = appDatabase.routeDao().getAllNotMineRoutes(user.getUid());
                Handler mainHandler = new Handler(mListener.getActivity().getMainLooper());
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        final RoutesAdapter adapter = new RoutesAdapter(getContext(), all);
                        lv.setAdapter(adapter);
                        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                final int a = i;
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        final Route route = adapter.getItem(a);
                                        RequestedRoute rr= new RequestedRoute();
                                        rr.setRouteId(route.getRid());
                                        rr.setUserId(user.getUid());
                                        appDatabase.requestedDao().insertAll(rr);
                                    }
                                }).start();
                                Toast.makeText(getContext(),"Ruta Pedida",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        }) .start();*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search_route, container, false);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
