package com.pokeme.store;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

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

    public void setToken(String token) {
        prefs.edit().putString(Config.TOKEN_SESSION_KEY, token).apply();
    }

    public String getToken() {
        return prefs.getString(Config.TOKEN_SESSION_KEY, "");
    }
}