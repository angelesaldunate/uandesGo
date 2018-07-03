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
import android.widget.AdapterView;
import android.widget.ImageView;
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

import java.util.ArrayList;
import java.util.List;


public class  RequestedRoutesFragment extends Fragment {
    private static final String DATABASE_NAME = "uandesGo_db";
    private List<Route> allroutes = new ArrayList<>();
    private List<Integer> idesroutes;
    private iComunicator mListener;
    private CredentialManage credentialManager;
    static private AppDatabase appDatabase;
    static private SharedPreferences sharedPreferences;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof iComunicator) {
            mListener = (iComunicator) context;
        }
    }

    public RequestedRoutesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appDatabase = mListener.getDb();
        sharedPreferences = mListener.getSharedPreferences();
        credentialManager = new CredentialManage(getActivity());

    }

    public void onViewCreated(View view, Bundle savedInstanceState){
        final SwipeMenuListView listView = view.findViewById(R.id.list_requested);
        final String value1 = sharedPreferences.getString("email_guardado",null);
        new Thread(new Runnable() {
            @Override
            public void run() {
                final User u = appDatabase.userDao().getOneUser(value1);
                idesroutes = appDatabase.requestedDao().getAllIdRoute(u.getUid());

                for(int k=0; k<idesroutes.size() ; k++){
                    Route route = appDatabase.routeDao().getRoutebyId(idesroutes.get(k));
                    allroutes.add(route);
                }
                Handler mainHandler = new Handler(getActivity().getMainLooper());
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        final RoutesAdapter adapter = new RoutesAdapter(getContext(), allroutes);
                        listView.setAdapter(adapter);
                        SwipeMenuCreator creator = new SwipeMenuCreator() {

                            @Override
                            public void create(SwipeMenu menu) {
                                // create "open" item
                                SwipeMenuItem openItem = new SwipeMenuItem(getContext());
                                // set item background
                                openItem.setBackground(new ColorDrawable(Color.TRANSPARENT));
                                // set item width
                                openItem.setWidth(170);
                                // set item title
                                openItem.setIcon(R.drawable.ic_delete);
                                // set item title fontsize
                                openItem.setTitleSize(24);
                                // set item title font color
                                openItem.setTitleColor(Color.RED);
                                // add to menu
                                menu.addMenuItem(openItem);

                                SwipeMenuItem openItem1 = new SwipeMenuItem(getContext());
                                // set item background
                                openItem1.setBackground(new ColorDrawable(Color.TRANSPARENT));
                                // set item width
                                openItem1.setWidth(170);
                                // set item title
                                openItem1.setTitle("Perfil");
                                // set item title fontsize
                                openItem1.setTitleSize(18);
                                // set item title font color
                                openItem1.setTitleColor(Color.BLACK);
                                // add to menu
                                menu.addMenuItem(openItem1);



                            }
                        };

// set creator
                        listView.setMenuCreator(creator);
                        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(final int position, SwipeMenu menu, int index) {
                                switch (index) {
                                    case 0:
                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                int ide_route = allroutes.get(position).getRid();
                                                User current = appDatabase.userDao().getOneUser(credentialManager.getEmail());
                                                Route route = appDatabase.routeDao().getRoutebyId(ide_route);
                                                RequestedRoute clicked_route = appDatabase.requestedDao().getOneRequestedRoute(current.getUid(),route.getRid());
                                                appDatabase.requestedDao().delete(clicked_route.getRrid());
                                            }
                                        }).start();

                                        Toast.makeText(getContext(),"Pedido Eliminado",Toast.LENGTH_SHORT).show();
                                        Fragment requested_fragment = new RequestedRoutesFragment();
                                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.framenew,requested_fragment).addToBackStack("null").commit();

                                        break;
                                    case 1:

                                        Fragment info_route = new OtherUserProfileFragment();
                                        Bundle ide = new Bundle();
                                        ide.putInt("user_id",allroutes.get(position).getUserId());
                                        info_route.setArguments(ide);
                                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.framenew,info_route).addToBackStack("null").commit();

                                        break;
                                }
                                // false : close the menu; true : not close the menu
                                return false;
                            }
                        });

                    }
                });
            }
        }) .start();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_requested_routes, container, false);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
