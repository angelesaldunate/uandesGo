package com.example.angeles.uandesgo;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.angeles.uandesgo.db.AppDatabase;
import com.example.angeles.uandesgo.db.User.Profile.Profile;
import com.example.angeles.uandesgo.db.User.User;


public class ProfileFragment extends Fragment {

    private iComunicator mListener;
    static private AppDatabase appDatabase;
    static private SharedPreferences sharedPreferences;
    private CredentialManage credentialManage;
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
        credentialManage = new CredentialManage(getActivity());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        return view;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        TextView user_name = (TextView) view.findViewById(R.id.user_name);
        TextView user_email = (TextView) view.findViewById(R.id.user_email);
        user_email.setText(credentialManage.getEmail());
        user_name.setText(credentialManage.getName());

        new Thread(new Runnable() {
            @Override
            public void run() {
                final User user = appDatabase.userDao().getOneUser(credentialManage.getEmail());
                Profile profile = appDatabase.profileDao().getOneProfile(user.getUid());
                final String phone = profile.getPhone();
                Handler mainHandler = new Handler(getActivity().getMainLooper());
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        TextView user_phone = (TextView) getActivity().findViewById(R.id.user_phone);
                        user_phone.setText(phone);
                    }
                });

            }
        }).start();


    }

        @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


}
