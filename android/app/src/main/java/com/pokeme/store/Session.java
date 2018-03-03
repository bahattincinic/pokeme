package com.pokeme.store;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;

import com.pokeme.service.Config;


public class Session {
    private SharedPreferences prefs;
    private static Session instance;

    private Session(Context context) {
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static Session getInstance(Context context) {
        if (instance == null) {
            instance = new Session(context);
        }
        return instance;
    }

    public static Session getInstance(FragmentActivity activity) {
        if (instance == null) {
            instance = new Session(activity.getApplicationContext());
        }
        return instance;
    }

    public void setToken(String token) {
        prefs.edit().putString(Config.TOKEN_SESSION_KEY, token).apply();
    }

    public void clearSession() {
        prefs.edit().clear().apply();
    }

    public String getToken() {
        return prefs.getString(Config.TOKEN_SESSION_KEY, "");
    }
}