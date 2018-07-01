package com.example.angeles.uandesgo;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.angeles.uandesgo.db.AppDatabase;
import com.example.angeles.uandesgo.db.User.User;


public class ProfileFragment extends Fragment {

    private iComunicator mListener;
    static private AppDatabase appDatabase;
    static private SharedPreferences sharedPreferences;
    private User user;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof iComunicator) {
            mListener = (iComunicator) context;
        }
    }

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appDatabase = mListener.getDb();
        sharedPreferences = mListener.getSharedPreferences();
        //getCurrentUser();
        //Log.d ("NOmnreeeeee",user.getEmail());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void getCurrentUser() {
        final String current_user_email = sharedPreferences.getString("email_guardado", "null");
        new Thread(new Runnable() {
            @Override
            public void run() {
                 user = appDatabase.userDao().getOneUser(current_user_email);
            }
        });

    }
}
