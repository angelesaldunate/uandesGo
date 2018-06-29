package com.example.angeles.uandesgo.db.Place;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by Angeles on 5/30/2018.
 */


@Entity
public class Place {

    @PrimaryKey (autoGenerate = true)
    private int pid;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "zone")
    private String zone;



    public Place(String name, String zone) {
        this.name = name;
        this.zone = zone;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String email) {
        this.zone = zone;
    }

}
