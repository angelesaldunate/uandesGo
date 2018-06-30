package com.example.angeles.uandesgo;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.angeles.uandesgo.db.AppDatabase;
import com.example.angeles.uandesgo.db.Route.Route;
import com.example.angeles.uandesgo.db.User.User;

import java.util.List;

public class MyRoutesFragment extends Fragment {
    private static final String DATABASE_NAME = "uandesGo_db";
    private List<Route> all;
    private iComunicator mListener;
    private AppDatabase appDatabase;
    private SharedPreferences sharedPreferences;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof iComunicator) {
            mListener = (iComunicator) context;
        }
    }

    public MyRoutesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appDatabase = mListener.getDb();
        sharedPreferences = mListener.getSharedPreferences();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_routes, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){

        final ListView lv = (ListView) view.findViewById(R.id.list_myRoutes);
        final String value1 = sharedPreferences.getString("email_guardado",null);
        new Thread(new Runnable() {
            @Override
            public void run() {
                final User u = appDatabase.userDao().getOneUser(value1);
                all = appDatabase.routeDao().getAllMYRoutes(u.getUid());
                Handler mainHandler = new Handler(getActivity().getMainLooper());
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        final RoutesAdapter adapter = new RoutesAdapter(getContext(), all);
                        lv.setAdapter(adapter);
                    }
                });
            }
        }) .start();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
