package com.example.angeles.uandesgo;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.angeles.uandesgo.db.AppDatabase;
import com.example.angeles.uandesgo.db.User.Profile.Profile;
import com.example.angeles.uandesgo.db.User.User;



public class OtherUserProfileFragment extends Fragment {
    private iComunicator mListener;
    static private AppDatabase appDatabase;
    static private SharedPreferences sharedPreferences;
    private CredentialManage credentialManage;
    private int ide_user;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof iComunicator) {
            mListener = (iComunicator) context;
        }
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appDatabase = mListener.getDb();
        sharedPreferences = mListener.getSharedPreferences();
        credentialManage = new CredentialManage(getActivity());

    }



    public OtherUserProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_other_user_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle user_ide = this.getArguments();
        if (user_ide != null) {
            ide_user = user_ide.getInt("user_id");
        }

        final View actual_view = view;

        new Thread(new Runnable() {
            @Override
            public void run() {

                final User user = appDatabase.userDao().getUserById(ide_user);
                Profile profile = appDatabase.profileDao().getOneProfile(user.getUid());
                final String phone = profile.getPhone();
                final String name = profile.getName();
                Handler mainHandler = new Handler(getActivity().getMainLooper());
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {

                        TextView user_email = (TextView) actual_view.findViewById(R.id.user_email_o);
                        user_email.setText(user.getEmail());
                        TextView user_phone = (TextView) actual_view.findViewById(R.id.user_phone_o);
                        user_phone.setText(phone);
                        TextView user_name = (TextView) actual_view.findViewById(R.id.user_name_o);
                        user_name.setText(name);
                    }
                });

            }
        }).start();
        final LinearLayout phone_call = actual_view.findViewById(R.id.user_address_layout);
        final LinearLayout email = actual_view.findViewById(R.id.user_email_layout);
        phone_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView user_phone = actual_view.findViewById(R.id.user_phone_o);
                String number = user_phone.getText().toString();
                mListener.make_phone_call(number);
            }
        });

        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView user_email = actual_view.findViewById(R.id.user_email_o);
                String email = user_email.getText().toString();
                mListener.send_email(email);
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
