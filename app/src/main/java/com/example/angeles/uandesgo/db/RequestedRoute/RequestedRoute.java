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
        (foreignKeys = {@ForeignKey(entity = Route.class,
                parentColumns = "rid",
                childColumns = "routeId",
                onDelete = CASCADE),@ForeignKey(entity = User.class,
                parentColumns = "uid",
                childColumns = "userId", onDelete = CASCADE)})
public class RequestedRoute {

    public int getRrid() {
        return rrid;
    }

    public void setRrid(int rrid) {
        this.rrid = rrid;
    }

    public int getRouteId() {
        return routeId;
    }

    public void setRouteId(int routeId) {
        this.routeId = routeId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @PrimaryKey (autoGenerate = true)
    private int rrid;
    private int routeId;
    private int userId;




}
