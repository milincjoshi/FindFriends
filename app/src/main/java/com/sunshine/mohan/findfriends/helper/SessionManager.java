package com.sunshine.mohan.findfriends.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by FEBIELGIVA on 6/7/2016.
 */

// to manage where login in or log out
public class SessionManager {

    public static final String LOG_TAG = SessionManager.class.getSimpleName();
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    Context context;

    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "LoginApiTest";
    private static final String KEY_IS_LOGGEDIN = "isLoggedIn";

    public SessionManager(Context context){
        this.context = context;
        preferences = context.getSharedPreferences(PREF_NAME,PRIVATE_MODE);
        editor = preferences.edit();
    }

    public void setLogin(boolean isLoggedIn){
        editor.putBoolean(KEY_IS_LOGGEDIN,isLoggedIn);
        editor.commit();
        Log.d(LOG_TAG,"User Login Modified In Pref");
    }

    public boolean isLoggedIn(){

        return preferences.getBoolean(KEY_IS_LOGGEDIN,false);
    }

    public void setPrefName(String emailID){
        editor.putString(PREF_NAME, emailID);
        editor.commit();
    }

    public String getPrefName(){
        return preferences.getString(PREF_NAME, "LoginApiTest");

    }

}
