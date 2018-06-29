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
import android.widget.ListView;
import android.widget.Toast;

import com.example.angeles.uandesgo.db.AppDatabase;
import com.example.angeles.uandesgo.db.Place;
import com.example.angeles.uandesgo.db.RequestedRoute;
import com.example.angeles.uandesgo.db.Route;
import com.example.angeles.uandesgo.db.User;

import java.util.Date;
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

    private List<Route> all;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public SearchRouteFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchRouteFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchRouteFragment newInstance(String param1, String param2) {
        SearchRouteFragment fragment = new SearchRouteFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){

        final AppDatabase appDatabase= Room.databaseBuilder(getContext(),AppDatabase.class, DATABASE_NAME).fallbackToDestructiveMigration().build();
        final ListView lv = (ListView) view.findViewById(R.id.list_allRoutes);
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        final String value1 = sharedPref.getString("email_dv",null);


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
                                final AppDatabase appDatabase = Room.databaseBuilder(getContext(),AppDatabase.class, DATABASE_NAME).fallbackToDestructiveMigration().build();



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

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
