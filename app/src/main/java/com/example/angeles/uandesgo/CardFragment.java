package com.example.angeles.uandesgo;


import android.arch.persistence.room.Room;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.angeles.uandesgo.db.AppDatabase;
import com.example.angeles.uandesgo.db.Route;

import java.util.ArrayList;
import java.util.List;

public class CardFragment extends Fragment {

    ArrayList<Route> listitems = new ArrayList<>();
    RecyclerView MyRecyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_card, container, false);
        MyRecyclerView = (RecyclerView) view.findViewById(R.id.cardView);
        MyRecyclerView.setHasFixedSize(true);
        LinearLayoutManager MyLayoutManager = new LinearLayoutManager(getActivity());
        MyLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        if (listitems.size() > 0 & MyRecyclerView != null) {
            MyRecyclerView.setAdapter(new MyAdapter(listitems));
        }
        MyRecyclerView.setLayoutManager(MyLayoutManager);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
        private ArrayList<Route> list;

        public MyAdapter(ArrayList<Route> Data) {
            list = Data;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent,int viewType) {
            // create a new view
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recycle_items, parent, false);
            MyViewHolder holder = new MyViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {
            holder.titleTextView.setText(list.get(position).getPlaceId());
            holder.likeImageView.setTag(R.drawable.ic_car);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView titleTextView;
        public ImageView likeImageView;
        public ImageView shareImageView;

        public MyViewHolder(View v) {
            super(v);
            titleTextView = (TextView) v.findViewById(R.id.titleTextView);
            likeImageView = (ImageView) v.findViewById(R.id.likeImageView);
            shareImageView = (ImageView) v.findViewById(R.id.shareImageView);
            likeImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    int id = (int)likeImageView.getTag();
                    if( id == R.drawable.ic_car){

                        likeImageView.setTag(R.drawable.ic_car_green);
                        likeImageView.setImageResource(R.drawable.ic_car_green);

                        Toast.makeText(getActivity(),"caca added to favourites",Toast.LENGTH_SHORT).show();

                    }else{

                        likeImageView.setTag(R.drawable.ic_car);
                        likeImageView.setImageResource(R.drawable.ic_car);
                        Toast.makeText(getActivity(),"caca removed from favourites",Toast.LENGTH_SHORT).show();
                    }
                }
            });



        }
    }

    public void initializeList() {
        listitems.clear();
        final AppDatabase appDatabase = Room.databaseBuilder(getContext(),AppDatabase.class, "movies_id").fallbackToDestructiveMigration().build();
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Route> all = appDatabase.routeDao().getAllRoutes();
                listitems.addAll(all);
            }
        }) .start();




    }
}
