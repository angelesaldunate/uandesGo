package com.example.angeles.uandesgo.db;


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

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "email")
    private String email;

    @ColumnInfo(name = "password")
    private String password;

    private String tel_number;



    public User(String name, String email, String password, String tel_number) {
        this.name = name;
        this.email = email;
        this.password=password;
        this.tel_number = tel_number;

    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getTel_number() {
        return tel_number;
    }

    public void setTel_number(String tel_number) {
        this.tel_number = tel_number;
    }
}
