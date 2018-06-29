package com.example.angeles.uandesgo.db.Route;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import com.example.angeles.uandesgo.db.Place.Place;
import com.example.angeles.uandesgo.db.User.User;

import static android.arch.persistence.room.ForeignKey.CASCADE;

/**
 * Created by Angeles on 5/30/2018.
 */



@Entity
(foreignKeys = {@ForeignKey(entity = Place.class,
        parentColumns = "pid",
        childColumns = "placeId",
        onDelete = CASCADE),@ForeignKey(entity = User.class,
        parentColumns = "uid",
        childColumns = "userId", onDelete = CASCADE)})
public class Route {



    @PrimaryKey (autoGenerate = true)
    private int rid;
    @ColumnInfo(name = "dep_time")
    private String dep_time;
    @ColumnInfo(name = "quantity")
    private int quantity ;
    @ColumnInfo(name = "starting")
    private boolean starting_point;
    private int placeId;
    private int userId;




    public void setRid(int rid) {
        this.rid = rid;
    }


    public int getRid() {
        return rid;
    }


    public String getDep_time() {
        return dep_time;
    }

    public void setDep_time(String dep_time) {
        this.dep_time = dep_time;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public boolean getStarting_point() {
        return starting_point;
    }

    public void setStarting_point(boolean starting_point) {
        this.starting_point = starting_point;
    }

    public int getPlaceId() {
        return placeId;
    }

    public void setPlaceId(int placeId) {
        this.placeId = placeId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

}
