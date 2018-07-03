package com.example.angeles.uandesgo;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.example.angeles.uandesgo.db.AppDatabase;
import com.example.angeles.uandesgo.db.RequestedRoute.RequestedRoute;
import com.example.angeles.uandesgo.db.Route.Route;
import com.example.angeles.uandesgo.db.User.User;

import java.util.List;

public class MyRoutesFragment extends Fragment {
    private static final String DATABASE_NAME = "uandesGo_db";
    private List<Route> all;
    private iComunicator mListener;
    private AppDatabase appDatabase;
    private SharedPreferences sharedPreferences;
    private CredentialManage credentialManager;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof iComunicator) {
            mListener = (iComunicator) context;
        }
    }

    public MyRoutesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appDatabase = mListener.getDb();
        sharedPreferences = mListener.getSharedPreferences();
        credentialManager = mListener.getCredentialManage();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_routes, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){

        final SwipeMenuListView lv = view.findViewById(R.id.list_myRoutes);
        final String value1 = sharedPreferences.getString("email_guardado",null);
        new Thread(new Runnable() {
            @Override
            public void run() {
                final User u = appDatabase.userDao().getOneUser(value1);
                all = appDatabase.routeDao().getAllMYRoutes(u.getUid());
                Handler mainHandler = new Handler(getActivity().getMainLooper());
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        final RoutesAdapter adapter = new RoutesAdapter(getContext(), all);
                        lv.setAdapter(adapter);
                        SwipeMenuCreator creator = new SwipeMenuCreator() {

                            @Override
                            public void create(SwipeMenu menu) {
                                // create "open" item
                                SwipeMenuItem openItem = new SwipeMenuItem(getContext());
                                // set item background
                                openItem.setBackground(new ColorDrawable(Color.RED));
                                // set item width
                                openItem.setWidth(200);
                                // set item title
                                openItem.setIcon(R.drawable.ic_delete_white);
                                // set item title fontsize
                                openItem.setTitleSize(18);
                                // set item title font color
                                openItem.setTitleColor(Color.WHITE);
                                // add to menu
                                menu.addMenuItem(openItem);
                            }
                        };

// set creator
                        lv.setMenuCreator(creator);
                    }
                });
                lv.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(final int position, SwipeMenu menu, int index) {
                        switch (index) {
                            case 0:
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        int ide_route = all.get(position).getRid();
                                        Route route = appDatabase.routeDao().getRoutebyId(ide_route);
                                        appDatabase.routeDao().delete(route.getRid());
                                    }
                                }).start();

                                Toast.makeText(getContext(),"Ruta Eliminada",Toast.LENGTH_SHORT).show();
                                Fragment requested_fragment = new MyRoutesFragment();
                                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.framenew,requested_fragment).addToBackStack("null").commit();
                                break;
                        }
                        // false : close the menu; true : not close the menu
                        return false;
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
}
