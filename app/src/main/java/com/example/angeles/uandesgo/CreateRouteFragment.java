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

public class CreateRouteFragment extends Fragment {
    private List<Place> all;
    private AppDatabase appDatabase;
    private ListView listView;
    private SharedPreferences sharedPreferences;


    private iComunicator mListener;

    public CreateRouteFragment() {
        // Required empty public constructor
    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_route, container, false);
        appDatabase = mListener.getDb();
        sharedPreferences = mListener.getSharedPreferences();
        listView = view.findViewById(R.id.list_destinations);
        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                all = appDatabase.placeDao().getAllPlaces();
                String value1 = sharedPreferences.getString("email_guardado", null);
                final User u = appDatabase.userDao().getOneUser(value1);
                Handler mainHandler = new Handler(getActivity().getMainLooper());
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        final PlaceAdapter adapter = new PlaceAdapter(getContext(), all);
                        listView.setAdapter(adapter);
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                final int a = i;
                                final int state = 0;
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
                                        } else {
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
                                                    Toast.makeText(getContext(), "Ruta Creada", Toast.LENGTH_SHORT).show();
                                                }
                                            });


                                            MyRoutesFragment homeFragment = new MyRoutesFragment();
                                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.framenew, homeFragment).addToBackStack("null").commit();


                                        }

                                    }
                                }).start();


                            }
                        });
                    }
                });

            }
        }).start();

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

