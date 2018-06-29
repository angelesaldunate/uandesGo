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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.angeles.uandesgo.db.AppDatabase;
import com.example.angeles.uandesgo.db.Place;
import com.example.angeles.uandesgo.db.Route;
import com.example.angeles.uandesgo.db.User;

import org.w3c.dom.Text;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public class CreateRouteFragment extends Fragment {
    private static final String DATABASE_NAME = "uandesGo_db";
    private List<com.example.angeles.uandesgo.db.Place> all;
    private iComunicator mListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof iComunicator) {
            mListener = (iComunicator) context;
        }
    }

    public CreateRouteFragment() {
        // Required empty public constructor
    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_create_route, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        final AppDatabase appDatabase = mListener.getDB();
        final ListView lv = (ListView) view.findViewById(R.id.list_destinations);

        new Thread(new Runnable() {
            @Override
            public void run() {
                all = appDatabase.placeDao().getAllPlaces();
                SharedPreferences sharedPref = mListener.getSharedPreferences();
                String value1 = sharedPref.getString("email_guardado",null);
                final com.example.angeles.uandesgo.db.User u = appDatabase.userDao().getOneUser(value1);
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
                                final AppDatabase appDatabase = Room.databaseBuilder(getContext(),AppDatabase.class, DATABASE_NAME).fallbackToDestructiveMigration().build();
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        boolean checked2 = ((RadioButton) getView().findViewById(R.id.radioButton_going)).isChecked();
                                        EditText quantity = (EditText) getView().findViewById(R.id.editText_capacity);
                                        int qty =0;

                                        if (quantity.getText().toString()!= ""){
                                             qty = Integer.valueOf(quantity.getText().toString());

                                        }
                                        final com.example.angeles.uandesgo.db.Place place = adapter.getItem(a);
                                        com.example.angeles.uandesgo.db.Route rt = new Route();
                                        rt.setDep_time(new Date().toString());
                                        rt.setPlaceId(place.getPid());
                                        rt.setQuantity(qty);
                                        rt.setUserId(u.getUid());
                                        rt.setStarting_point(checked2);
                                        appDatabase.routeDao().insertAll(rt);
                                    }
                                }) .start();
                                Toast.makeText(getContext(),"Ruta Creada",Toast.LENGTH_SHORT).show();
                                HomeFragment homeFragment = new HomeFragment();
                                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.framenew,homeFragment).addToBackStack("null").commitAllowingStateLoss();


                            }
                        });
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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radioButton_going:
                if (checked)
                    // going
                    break;
            case R.id.radioButton_leaving:
                if (checked)
                    // Leaving
                    break;
        }
    }


}
