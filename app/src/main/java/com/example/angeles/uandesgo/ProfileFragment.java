package com.example.angeles.uandesgo;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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

    public void onViewCreated(final View view, Bundle savedInstanceState) {

        final View actual_view = view;
        final TextView user_email = (TextView) view.findViewById(R.id.user_email);
        user_email.setText(credentialManage.getEmail());

        new Thread(new Runnable() {
            @Override
            public void run() {
                final User user = appDatabase.userDao().getOneUser(credentialManage.getEmail());
                Profile profile = appDatabase.profileDao().getOneProfile(user.getUid());
                final String phone = profile.getPhone();
                final String name = profile.getName();
                Handler mainHandler = new Handler(getActivity().getMainLooper());
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        TextView user_phone = (TextView) actual_view.findViewById(R.id.user_phone);
                        EditText user_edit_phone = (EditText) actual_view.findViewById(R.id.user_edit_phone);
                        user_edit_phone.setText(phone);
                        user_phone.setText(phone);
                        EditText user_edit_name = (EditText) actual_view.findViewById(R.id.user_edit_name);
                        TextView user_name = (TextView) actual_view.findViewById(R.id.user_name);
                        user_name.setText(name);
                        user_edit_name.setText(name);
                    }
                });

            }
        }).start();

        //Si Clickea Boton Para editar
        final Button edit_button = (Button) view.findViewById(R.id.buttonEdit);
        final Button ok_button = (Button) actual_view.findViewById(R.id.button_ok);
        final Button cancel_button = (Button) actual_view.findViewById(R.id.button_cancel);
        final LinearLayout phone_call = actual_view.findViewById(R.id.user_address_layout);
        final LinearLayout email = actual_view.findViewById(R.id.user_email_layout);
        edit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView user_name = (TextView) actual_view.findViewById(R.id.user_name);
                TextView user_phone = (TextView) actual_view.findViewById(R.id.user_phone);
                EditText user_edit_name = (EditText) actual_view.findViewById(R.id.user_edit_name);
                EditText user_edit_phone = (EditText) actual_view.findViewById(R.id.user_edit_phone);
                edit_button.setVisibility(TextView.INVISIBLE);
                ok_button.setVisibility(TextView.VISIBLE);
                cancel_button.setVisibility(TextView.VISIBLE);
                user_name.setVisibility(TextView.INVISIBLE);
                user_phone.setVisibility(TextView.INVISIBLE);
                user_edit_name.setVisibility(TextView.VISIBLE);
                user_edit_phone.setVisibility(TextView.VISIBLE);
            }
        });

        ok_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText user_edit_name = (EditText) actual_view.findViewById(R.id.user_edit_name);
                EditText user_edit_phone = (EditText) actual_view.findViewById(R.id.user_edit_phone);
                final String new_name = user_edit_name.getText().toString();
                final String new_phone = user_edit_phone.getText().toString();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        User user = appDatabase.userDao().getOneUser(credentialManage.getEmail());
                        Profile profile = appDatabase.profileDao().getOneProfile(user.getUid());
                        Profile updated_profile = new Profile();
                        updated_profile.setPrid(profile.getPrid());
                        updated_profile.setName(new_name);
                        updated_profile.setUserId(user.getUid());
                        updated_profile.setPhone(new_phone);
                        appDatabase.profileDao().update(updated_profile);
                    }
                }).start();
                setNameOnHeader(new_name);
                Fragment fr = new ProfileFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.framenew, fr).addToBackStack("null").commit();

            }
        });
        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fr = new ProfileFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.framenew, fr).addToBackStack("null").commit();
            }
        });

        phone_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView user_phone = actual_view.findViewById(R.id.user_phone);
                String number = user_phone.getText().toString();
                mListener.make_phone_call(number);
            }
        });

        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView user_email = actual_view.findViewById(R.id.user_email);
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
    public void setNameOnHeader( String name){
        NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
        View headerView = (navigationView.getHeaderView(0));
        TextView textviewnombre = headerView.findViewById(R.id.Textviewnavnombre);
        textviewnombre.setText(name);
    }


}
