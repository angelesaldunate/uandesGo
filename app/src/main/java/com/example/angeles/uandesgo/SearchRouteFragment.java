package com.example.angeles.uandesgo;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
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

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SearchRouteFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SearchRouteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchRouteFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String DATABASE_NAME = "movies_db";
    private static AppDatabase appDatabase;

    private List<Route> all;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

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


        new Thread(new Runnable() {
            @Override
            public void run() {
                final User u = appDatabase.userDao().getOneUser(value1);
                all = appDatabase.routeDao().getAllNotMineRoutes(u.getUid());
                Handler mainHandler = new Handler(getActivity().getMainLooper());
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
                                        rr.setUserId(u.getUid());
                                        appDatabase.requestedDao().insertAll(rr);
                                    }
                                }) .start();
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
