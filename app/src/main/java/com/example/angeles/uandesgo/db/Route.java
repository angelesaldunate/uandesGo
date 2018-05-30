package com.example.angeles.uandesgo.db;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

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

//    @ColumnInfo(name = "departure_time")
//    private ;
//
//    @ColumnInfo(name = "quantity")
//    private ;
//
//    @ColumnInfo(name = "starting_point")
//    private ;
//
//    public Route(String name, departure_time, quantity, starting_point) {
//        this.name = name;
//
//    }
//
//    public int getUid() {
//        return uid;
//    }
//
//    public void setUid(int uid) {
//        this.uid = uid;
//    }
//
}
