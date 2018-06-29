package com.example.angeles.uandesgo;

import android.app.Activity;
import android.content.SharedPreferences;

import com.example.angeles.uandesgo.db.AppDatabase;

public interface iComunicator {
    public AppDatabase getDB();
    public SharedPreferences getSharedPreferences();
    public Activity getActivity();
}
