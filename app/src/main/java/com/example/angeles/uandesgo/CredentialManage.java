package com.example.angeles.uandesgo;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Angeles on 5/23/2018.
 */

public class CredentialManage {
    public void guardarCredenciales(Activity act, String email, String password ){
        SharedPreferences sharedPref = act.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("email_guardado", email);
        editor.putString("password_guardada", password);
        editor.commit();
    }
    public boolean verificarCredenciales(Activity act){
        SharedPreferences preferences = act.getSharedPreferences("text", 0);
        String value1 = preferences.getString("email_devuelto",null);
        String value2 = preferences.getString("password_devuelto",null);
        if (value1 == null && value2==null) {
            return false;
        }
        return true;
    }
    public void borrarCredenciales(Activity act ){
        SharedPreferences sharedPref = act.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove("email_guardado");
        editor.remove("password_guardada");
        editor.commit();
    }

}