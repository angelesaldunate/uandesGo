package com.example.angeles.uandesgo;

import android.Manifest;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.LightingColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


import com.example.angeles.uandesgo.db.AppDatabase;
import com.example.angeles.uandesgo.db.Place.Place;
import com.example.angeles.uandesgo.db.User.Profile.Profile;
import com.example.angeles.uandesgo.db.User.User;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, iComunicator {
    static final int SEND_MESSAGE = 1;
    private static final String DATABASE_NAME = "uandesGo_db";
    static private AppDatabase appDatabase;
    static private SharedPreferences sharedPreferences;
    static private CredentialManage credentialManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        appDatabase = Room.databaseBuilder(this, AppDatabase.class, DATABASE_NAME).fallbackToDestructiveMigration().build();
        sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        credentialManager = new CredentialManage(this);
        /////////////////////////////////////////////////////////////////////////
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorGreen)));
        //////////////////////////////////////////////////////////////////////////
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (appDatabase.placeDao().getAllPlaces().size() == 0) {
                    appDatabase.placeDao().insertAll(new Place("Escuela Militar", "Oriente"));
                    appDatabase.placeDao().insertAll(new Place("Los Dominicos", "Oriente"));
                    appDatabase.placeDao().insertAll(new Place("Republica", "Centro"));
                    appDatabase.placeDao().insertAll(new Place("La Moneda", "Centro"));
                    appDatabase.placeDao().insertAll(new Place("Plaza de Maipu", "Sur Poniente"));
                    appDatabase.placeDao().insertAll(new Place("San Pablo", "Sur Poniente"));
                }
            }
        }).start();


        if (!credentialManager.verificarCredenciales()) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.putExtra(EXTRA_MESSAGE, "Sent!");
            startActivityForResult(intent, SEND_MESSAGE);
        } else {
            setCredentialsOnHeader(credentialManager.getEmail());
            new Thread(new Runnable() {
                @Override
                public void run() {
                    int ide_user = appDatabase.userDao().getOneUser(credentialManager.getEmail()).getUid();
                    String name = appDatabase.profileDao().getOneProfile(ide_user).getName();
                    setNameOnHeader(name);
                }
            }).start();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Fragment fragment;
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_create) {
            fragment = new CreateRouteFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.framenew, fragment).addToBackStack("null").commit();

        } else if (id == R.id.nav_search) {
            fragment = new SearchRouteFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.framenew, fragment).addToBackStack("null").commit();


        } else if (id == R.id.nav_myroutes) {
            fragment = new MyRoutesFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.framenew, fragment).addToBackStack("null").commit();


        } else if (id == R.id.nav_request) {
            fragment = new RequestedRoutesFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.framenew, fragment).addToBackStack("null").commit();


        } else if (id == R.id.nav_profile) {
            fragment = new ProfileFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.framenew, fragment).addToBackStack("null").commit();


        } else if (id == R.id.nav_logout) {
            logOut();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void logOut() {
        credentialManager.borrarCredenciales();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra(EXTRA_MESSAGE, "Sent!");
        //iniciaractividad solo si no existe anteriormente
        startActivityForResult(intent, SEND_MESSAGE);

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SEND_MESSAGE) {
            if (resultCode == RESULT_OK) {

                final String email = data.getStringExtra("email_devuelto");
                final String password = data.getStringExtra("password_devuelto");
                final String name = data.getStringExtra("nombre_devuelto");
                final String phone = data.getStringExtra("telefono_devuelto");
                credentialManager.guardarCredenciales(email, password);


                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (appDatabase.userDao().getOneUser(email) == null) {
                            appDatabase.userDao().insertAll(new User(email, password));
                            int ide = appDatabase.userDao().getOneUser(email).getUid();
                            Profile pro = new Profile();
                            pro.setUserId(ide);
                            pro.setName(name);
                            pro.setPhone(phone);
                            appDatabase.profileDao().insertAll(pro);
                            setNameOnHeader(name);

                        } else {
                            User current = appDatabase.userDao().getOneUser(email);
                            Profile actual_profile = appDatabase.profileDao().getOneProfile(current.getUid());
                            setNameOnHeader(actual_profile.getName());
                        }


                    }
                }).start();
                setCredentialsOnHeader(email);


            }
        }
    }

    public void setCredentialsOnHeader(String email) {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = (navigationView.getHeaderView(0));
        TextView textViewmail = headerView.findViewById(R.id.emailView);
        TextView textviewnombre = headerView.findViewById(R.id.Textviewnavnombre);
        textViewmail.setText(email);
        Fragment fragment = new SearchRouteFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.framenew, fragment).addToBackStack("null").commit();
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
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time * 1000);
        cal.add(Calendar.HOUR, -4);
        String date = DateFormat.format("dd-MM-yyyy HH:mm:ss", cal).toString();
        return date;
    }

    @Override
    public void make_phone_call(String number) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + number));
        startActivity(intent);
    }

    @Override
    public void send_email(String destination) {
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto",destination, null));
        startActivity(Intent.createChooser(intent, "Choose an Email client :"));
    }

    public void setNameOnHeader( String name){
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = (navigationView.getHeaderView(0));
        TextView textviewnombre = headerView.findViewById(R.id.Textviewnavnombre);
        textviewnombre.setText(name);
    }
}
