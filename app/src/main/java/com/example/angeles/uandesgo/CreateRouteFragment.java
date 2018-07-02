package com.example.angeles.uandesgo;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.angeles.uandesgo.db.AppDatabase;
import com.example.angeles.uandesgo.db.Place.Place;
import com.example.angeles.uandesgo.db.Route.Route;
import com.example.angeles.uandesgo.db.User.User;

import java.util.Date;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CreateRouteFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * create an instance of this fragment.
 */
public class CreateRouteFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String DATABASE_NAME = "uandesGo_db";
    private List<Place> all;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public CreateRouteFragment() {
        // Required empty public constructor
    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_route, container, false);
    }



    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){

        final AppDatabase appDatabase=Room.databaseBuilder(getContext(),AppDatabase.class, DATABASE_NAME).fallbackToDestructiveMigration().build();
        final ListView lv = (ListView) view.findViewById(R.id.list_destinations);


        new Thread(new Runnable() {
            @Override
            public void run() {
                all = appDatabase.placeDao().getAllPlaces();
                SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                String value1 = sharedPref.getString("email_guardado",null);
                final User u = appDatabase.userDao().getOneUser(value1);
                Handler mainHandler = new Handler(getActivity().getMainLooper());
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        final PlaceAdapter adapter = new PlaceAdapter(getContext(), all);
                        lv.setAdapter(adapter);
                        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                final int a = i;
                                final int state = 0;
                                final AppDatabase appDatabase = Room.databaseBuilder(getContext(),AppDatabase.class, DATABASE_NAME).fallbackToDestructiveMigration().build();
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        boolean checked2 = ((RadioButton) getView().findViewById(R.id.radioButton_going)).isChecked();
                                        final EditText quantity = (EditText) getView().findViewById(R.id.editText_capacity);
                                        String qty = quantity.getText().toString();
                                        if (TextUtils.isEmpty(qty)) {
                                            Handler mainHandler = new Handler(getActivity().getMainLooper());
                                            mainHandler.post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    quantity.setError(getString(R.string.error_field_required));
                                                    quantity.requestFocus();
                                                }
                                            });
                                        }
                                        else{
                                            final Place place = adapter.getItem(a);
                                            Route rt = new Route();
                                            rt.setDep_time(new Date().toString());
                                            rt.setPlaceId(place.getPid());

                                            rt.setQuantity(Integer.valueOf(qty));
                                            rt.setUserId(u.getUid());
                                            rt.setStarting_point(checked2);
                                            appDatabase.routeDao().insertAll(rt);
                                            getActivity().runOnUiThread(new Runnable() {
                                                public void run() {
                                                    Toast.makeText(getContext(),"Ruta Creada",Toast.LENGTH_SHORT).show();
                                                }
                                            });


                                            MyRoutesFragment homeFragment = new MyRoutesFragment();
                                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.framenew,homeFragment).addToBackStack("null").commit();


                                        }

                                    }
                                }) .start();





                            }
                        });
                    }
                });

            }
        }) .start();

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
