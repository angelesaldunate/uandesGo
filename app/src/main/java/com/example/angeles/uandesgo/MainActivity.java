package com.example.angeles.uandesgo;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    static final int SEND_MESSAGE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //aca tengo el header para editarlo de aca
        View headerView = (navigationView.getHeaderView(0));
        TextView email = headerView.findViewById(R.id.emailView);


        /*CredentialManage nueva = new CredentialManage();
        if (!nueva.verificarCredenciales(this)) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.putExtra(EXTRA_MESSAGE,"Sent!");
            //iniciaractividad solo si no existe anteriormente
            startActivityForResult(intent,SEND_MESSAGE);
        }*/
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

            getSupportFragmentManager().beginTransaction().replace(R.id.framenew,fragment).addToBackStack("null").commit();

            // Handle the camera action
        } else if (id == R.id.nav_search) {
            fragment=new SearchRouteFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.framenew,fragment).addToBackStack("null").commit();


        } else if (id == R.id.nav_myroutes) {
            fragment=new MyRoutesFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.framenew,fragment).addToBackStack("null").commit();


        } else if (id == R.id.nav_request) {
            fragment=new RequestedRoutesFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.framenew,fragment).addToBackStack("null").commit();


        } else if (id == R.id.nav_profile) {
            fragment=new ProfileFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.framenew,fragment).addToBackStack("null").commit();


        } else if (id == R.id.nav_logout) {
            logOut();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void logOut() {
        CredentialManage nueva = new CredentialManage();
        nueva.borrarCredenciales(this);
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra(EXTRA_MESSAGE,"Sent!");
        //iniciaractividad solo si no existe anteriormente
        startActivityForResult(intent,SEND_MESSAGE);

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == SEND_MESSAGE) {

            if (resultCode == RESULT_OK) {

                String email = data.getStringExtra("email_devuelto");
                String password = data.getStringExtra("password_devuelto");

                NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
                //aca tengo el header para editarlo de aca
                View headerView = (navigationView.getHeaderView(0));
                TextView textViewmail = headerView.findViewById(R.id.emailView);

                textViewmail.setText(email);
                //TextView textViewpass = findViewById(R.id.textViewPassword);
                //textViewpass.setText(password);

                CredentialManage nueva = new CredentialManage();
                nueva.guardarCredenciales(this,email,password);



            }
        }
    }

}
