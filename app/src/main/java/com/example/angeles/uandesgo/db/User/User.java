package com.example.angeles.uandesgo.db.User;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.io.Console;

/**
 * Created by Angeles on 5/30/2018.
 */


@Entity
public class User {

    @PrimaryKey (autoGenerate = true)
    private int uid;

    @ColumnInfo(name = "email")
    private String email;

    @ColumnInfo(name = "password")
    private String password;




    public User( String email, String password) {
        this.email = email;
        this.password=password;

    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
