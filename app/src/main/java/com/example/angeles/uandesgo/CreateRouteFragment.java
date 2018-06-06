package com.example.angeles.uandesgo;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.angeles.uandesgo.db.AppDatabase;
import com.example.angeles.uandesgo.db.Place;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CreateRouteFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CreateRouteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateRouteFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String DATABASE_NAME = "movies_db";
    private List<Place> all;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public CreateRouteFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreateRouteFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateRouteFragment newInstance(String param1, String param2) {
        CreateRouteFragment fragment = new CreateRouteFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_route, container, false);
    }



    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        String[] places = new String[] {
                "Escuela Militar","Los Dominicos"

        };
//        PlaceViewModel mplaceViewModel = ViewModelProviders.of(this).get(PlaceViewModel.class);
//        LiveData<List<Place>> lp = mplaceViewModel.getmAllPlaces();
//        PlaceAdapter adapter = new PlaceAdapter(getContext(),lp.getValue());
        final AppDatabase formDatabase=Room.databaseBuilder(getContext(),AppDatabase.class, DATABASE_NAME).fallbackToDestructiveMigration().build();
        final ListView lv = (ListView) view.findViewById(R.id.list_destinations);

//        lv.setAdapter(adapter);
        new Thread(new Runnable() {
            @Override
            public void run() {
                all = formDatabase.placeDao().getAllPlaces();
                Handler mainHandler = new Handler(getActivity().getMainLooper());
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        final PlaceAdapter adapter = new PlaceAdapter(getContext(), all);


                        lv.setAdapter(adapter);
                        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                Toast.makeText(getContext(),adapter.getItem(i).getName(),Toast.LENGTH_SHORT).show();

                            }
                        });
                    }
                });

            }
        }) .start();

//
//        final List<String> place_list = new ArrayList<String>(Arrays.asList(places));
//
//        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity() , android.R.layout.simple_list_item_1,place_list);
//
//        lv.setAdapter(arrayAdapter);
//        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                boolean checked1 = ((RadioButton) getView().findViewById(R.id.radioButton_going) ).isChecked();
//                boolean checked2 = ((RadioButton) getView().findViewById(R.id.radioButton_leaving) ).isChecked();
//
//                String destin = "";
//                if (checked1)
//                    destin = "Going";
//                if (checked2)
//                    destin = "Leaving";
//
//
//              //  Toast.makeText(getContext(),arrayAdapter.getItem(i),Toast.LENGTH_SHORT).show();
//                Toast.makeText(getContext(),destin,Toast.LENGTH_SHORT).show();
//
//
//            }
//        });


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
