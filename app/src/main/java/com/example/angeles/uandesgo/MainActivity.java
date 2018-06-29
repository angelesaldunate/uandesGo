package com.example.angeles.uandesgo;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.example.angeles.uandesgo.db.Place;
import com.example.angeles.uandesgo.db.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, iComunicator {
    static final int SEND_MESSAGE = 1;
    private static final String DATABASE_NAME = "uandesGo_db";
    static private AppDatabase appDatabase;
    static private SharedPreferences sharedPref;
    private CredentialManage credentialManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ////////////////// Drawer Stuff
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Objects.requireNonNull(getSupportActionBar()).setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorGreen)));
        ////////////////////////////////////

        appDatabase = Room.databaseBuilder(this,AppDatabase.class, DATABASE_NAME).fallbackToDestructiveMigration().build();
        sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (appDatabase.placeDao().getAllPlaces().size()==0){
                    appDatabase.placeDao().insertAll(new Place("Escuela Militar", "Oriente"));
                    appDatabase.placeDao().insertAll(new Place("Los Dominicos", "Oriente"));
                    appDatabase.userDao().insertAll(new User("Pablo","email","passsssss","+56999999999"));
                }
            }
        }).start();

        credentialManager = new CredentialManage(this);
        if (!credentialManager.verificarCredenciales()) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.putExtra(EXTRA_MESSAGE,"Sent!");
            startActivityForResult(intent,SEND_MESSAGE);
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
        Fragment fragment= null;
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_create) {
            fragment=new CreateRouteFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.framenew,fragment).addToBackStack("null").commitAllowingStateLoss();

        } else if (id == R.id.nav_home) {
            fragment=new HomeFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.framenew,fragment).addToBackStack("null").commitAllowingStateLoss();

        } else if (id == R.id.nav_search) {
            fragment=new SearchRouteFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.framenew,fragment).addToBackStack("null").commitAllowingStateLoss();


        } else if (id == R.id.nav_myroutes) {
            fragment=new MyRoutesFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.framenew,fragment).addToBackStack("null").commitAllowingStateLoss();


        } else if (id == R.id.nav_request) {
            fragment=new RequestedRoutesFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.framenew,fragment).addToBackStack("null").commitAllowingStateLoss();


        } else if (id == R.id.nav_profile) {
            fragment=new ProfileFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.framenew,fragment).addToBackStack("null").commitAllowingStateLoss();


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
        intent.putExtra(EXTRA_MESSAGE,"Sent!");
        //iniciaractividad solo si no existe anteriormente
        startActivityForResult(intent,SEND_MESSAGE);

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SEND_MESSAGE) {
            if (resultCode == RESULT_OK) {
                final String email = data.getStringExtra("email_devuelto");
                final String password = data.getStringExtra("password_devuelto");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        appDatabase.userDao().insertAll(new User("Angeles", email, password, "+56999999999"));
                    }
                }).start();

                NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("email_dv", email);
                editor.apply();

                //Update credentials shown in header
                View headerView = (navigationView.getHeaderView(0));
                TextView textViewmail = headerView.findViewById(R.id.emailView);
                TextView textviewnombre = headerView.findViewById(R.id.Textviewnavnombre);
                textViewmail.setText(email);
                textviewnombre.setText("Angeles");

                credentialManager.guardarCredenciales(email,password);

                //Fragment fragment = new SearchRouteFragment();
                //getSupportFragmentManager().beginTransaction().replace(R.id.framenew,fragment).addToBackStack("null").commitAllowingStateLoss();
            }
        }
    }

    @Override
    public AppDatabase getDB() {
        return appDatabase;
    }

    @Override
    public SharedPreferences getSharedPreferences() {
        return sharedPref;
    }

    @Override
    public Activity getActivity() {
        return this;
    }
}
