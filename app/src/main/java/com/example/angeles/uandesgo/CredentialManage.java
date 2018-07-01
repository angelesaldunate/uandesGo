package com.example.angeles.uandesgo;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Angeles on 5/23/2018.
 */

public class CredentialManage {

    private Activity activity;
    private SharedPreferences preferences;

    CredentialManage(Activity activity) {
        this.activity = activity;
        preferences = activity.getPreferences(Context.MODE_PRIVATE);
    }
    public void guardarCredenciales(String email, String password,String name ){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("email_guardado", email);
        editor.putString("password_guardada", password);
        editor.putString("nombre_guardado",name);
        editor.apply();
    }
    public boolean verificarCredenciales(){
        String value1 = preferences.getString("email_guardado",null);
        String value2 = preferences.getString("password_guardada",null);
        return value1 != null || value2 != null;
    }
    public void borrarCredenciales(){
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("email_guardado");
        editor.remove("password_guardada");
        editor.remove("nombre_guardado");
        editor.apply();
    }
    public String getEmail() {
        return preferences.getString("email_guardado",null);
    }
    public String getName() {
        return preferences.getString("nombre_guardado",null);
    }


}