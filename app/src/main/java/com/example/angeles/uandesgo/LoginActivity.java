package com.example.angeles.uandesgo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.preference.PreferenceFragment;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.angeles.uandesgo.db.AppDatabase;
import com.example.angeles.uandesgo.db.User.Profile.Profile;
import com.example.angeles.uandesgo.db.User.User;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;


public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor>, iComunicator  {

    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    private UserLoginTask mAuthTask = null;
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private EditText mNameView;
    private EditText mPhoneView;
    private View mProgressView;
    private View mLoginFormView;
    private static final String DATABASE_NAME = "uandesGo_db";
    static private AppDatabase appDatabase;
    static private SharedPreferences sharedPreferences;
    static private CredentialManage credentialManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        appDatabase = Room.databaseBuilder(this,AppDatabase.class, DATABASE_NAME).fallbackToDestructiveMigration().build();

        mPasswordView = (EditText) findViewById(R.id.password);
        mNameView = (EditText) findViewById(R.id.name);
        mPhoneView = (EditText) findViewById(R.id.phone);

        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorGreen)));

    }




    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {

                // Reset errors.
                Handler mainHandler = new Handler(getMainLooper());
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mEmailView.setError(null);
                        mPasswordView.setError(null);
                    }
                });

                // Store values at the time of the login attempt.
                final String email = mEmailView.getText().toString();
                final String password = mPasswordView.getText().toString();
                final String name = mNameView.getText().toString();
                final String phone = mPhoneView.getText().toString();

                boolean cancel = false;
                View focusView = null;

                // Check for a valid password, if the user entered one.
                if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mPasswordView.setError(getString(R.string.error_invalid_password));
                        }
                    });
                    focusView = mPasswordView;
                    cancel = true;
                }

                // Check for a valid email address.
                if (TextUtils.isEmpty(email)) {
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mEmailView.setError(getString(R.string.error_field_required));
                        }
                    });
                    focusView = mEmailView;
                    cancel = true;
                } else if (!isEmailValid(email)) {
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mEmailView.setError(getString(R.string.error_invalid_email));
                        }
                    });
                    focusView = mEmailView;
                    cancel = true;
                }

                //Revisar la base de datos para ver si tiene perfil o no, pero puede que no este creado en la bd
                boolean profile_exist = true;
                User current= appDatabase.userDao().getOneUser(email);
                if (current == null){
                    //no hay usuario ni perfil
                    profile_exist= false;
                }else{// esta el usuario pero no el perfil
                    if (appDatabase.profileDao().getOneProfile(current.getUid())==null){
                        profile_exist = false;
                    }
                }

                if (!profile_exist){

                    if(TextUtils.isEmpty(name)){
                        mainHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                mNameView.setError(getString(R.string.error_field_required));
                            }
                        });
                        focusView = mNameView;
                        cancel = true;
                    }
                    if (TextUtils.isEmpty(phone)){
                        mainHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                mPhoneView.setError(getString(R.string.error_field_required));
                            }
                        });
                        focusView = mPhoneView;
                        cancel = true;
                    }

                }
                final View focus = focusView;
                if (cancel) {
                    // There was an error; don't attempt login and focus the first
                    // form field with an error.
                    mainHandler.post(new Runnable() {
                                         @Override
                                         public void run() {
                                             focus.requestFocus();
                                         }
                                     });
                } else {
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            // Show a progress spinner, and kick off a background task to
                            // perform the user login attempt.
                            showProgress(true);
                            mAuthTask = new UserLoginTask(email, password);
                            mAuthTask.execute((Void) null);
                            Context context = getApplicationContext();
                            CharSequence text = "Credenciales Validas";
                            int duration = Toast.LENGTH_SHORT;
                            Toast toast = Toast.makeText(context, text, duration);
                            toast.show();

                            Intent resultIntent = new Intent();
                            resultIntent.putExtra("email_devuelto",email);
                            resultIntent.putExtra("password_devuelto",password);
                            resultIntent.putExtra("nombre_devuelto", name);
                            resultIntent.putExtra("telefono_devuelto",phone);
                            setResult(MainActivity.RESULT_OK, resultIntent);
                            finish();
                        }
                    });
                }
            }
        }).start();


    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            for (String credential : DUMMY_CREDENTIALS) {
                String[] pieces = credential.split(":");
                if (pieces[0].equals(mEmail)) {
                    // Account exists, return true if the password matches.
                    return pieces[1].equals(mPassword);
                }
            }

            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                finish();
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
    @Override
    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    @Override
    public CredentialManage getCredentialManage() {
        return credentialManager;
    }

    @Override
    public AppDatabase getDb() {
        return appDatabase;
    }

    @Override
    public String getDate(Long time) {
        return null;
    }

    @Override
    public void make_phone_call(String number) {
        return;
    }

    @Override
    public void send_email(String destination) {
        return;
    }

    public void checkProfileExistence(final String email){
        new Thread(new Runnable() {
            @Override
            public void run() {

            }
        }) .start();
    }
}

