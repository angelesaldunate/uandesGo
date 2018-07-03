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
    public String getDate(Long time);
    public void make_phone_call(String number);
    public void send_email(String destination);
}
