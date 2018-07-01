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
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.angeles.uandesgo.db.AppDatabase;
import com.example.angeles.uandesgo.db.RequestedRoute.RequestedRoute;
import com.example.angeles.uandesgo.db.Route.Route;
import com.example.angeles.uandesgo.db.User.User;

import java.util.ArrayList;
import java.util.List;


public class RequestedRoutesFragment extends Fragment {
    private static final String DATABASE_NAME = "uandesGo_db";
    private List<Route> allroutes = new ArrayList<>();
    private List<Integer> idesroutes;
    private iComunicator mListener;
    static private AppDatabase appDatabase;
    static private SharedPreferences sharedPreferences;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof iComunicator) {
            mListener = (iComunicator) context;
        }
    }

    public RequestedRoutesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appDatabase = mListener.getDb();
        sharedPreferences = mListener.getSharedPreferences();
    }

    public void onViewCreated(View view, Bundle savedInstanceState){
        final ListView lv = (ListView) view.findViewById(R.id.list_requested);
        final String value1 = sharedPreferences.getString("email_guardado",null);
        new Thread(new Runnable() {
            @Override
            public void run() {
                final User u = appDatabase.userDao().getOneUser(value1);
                idesroutes = appDatabase.requestedDao().getAllIdRoute(u.getUid());
                //System.out.println("ACAAAAAAAAAAAAAAAAAAAAAAAAAA");
                //System.out.println(idesroutes.size());
                for(int k=0; k<idesroutes.size() ; k++){
                    Route route = appDatabase.routeDao().getRoutebyId(idesroutes.get(k));
                    allroutes.add(route);
                }
                Handler mainHandler = new Handler(getActivity().getMainLooper());
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        final RoutesAdapter adapter = new RoutesAdapter(getContext(), allroutes);
                        lv.setAdapter(adapter);
                        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                final Route route = adapter.getItem(i);
                                RequestedRoute rr= new RequestedRoute();
                                rr.setRouteId(route.getRid());
                                rr.setUserId(u.getUid());
                                appDatabase.requestedDao().insertAll(rr);
                                Toast.makeText(getContext(),"Ruta Pedida",Toast.LENGTH_SHORT).show();
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
        return inflater.inflate(R.layout.fragment_requested_routes, container, false);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
