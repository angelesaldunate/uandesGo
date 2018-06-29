package com.example.angeles.uandesgo;

import android.app.Activity;
import android.content.SharedPreferences;

import com.example.angeles.uandesgo.db.AppDatabase;

/**
 * Created by Angeles on 6/29/2018.
 */

public interface iComunicator {
    public SharedPreferences getSharedPreferences();
    public CredentialManage getCredentialManage();
    public AppDatabase getDb();
}
