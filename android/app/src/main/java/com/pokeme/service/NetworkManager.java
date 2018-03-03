package com.pokeme.service;


import android.content.Context;
import android.support.v4.app.FragmentActivity;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;


public class NetworkManager {
    private static NetworkManager instance;
    private RequestQueue queue;

    private NetworkManager(Context context) {
        queue = Volley.newRequestQueue(context);
    }

    public static NetworkManager getInstance(FragmentActivity activity) {
        if (instance == null) {
            instance = new NetworkManager(activity.getApplicationContext());
        }
        return instance;
    }

    public static NetworkManager getInstance(Context context) {
        if (instance == null) {
            instance = new NetworkManager(context.getApplicationContext());
        }
        return instance;
    }

    public void add(JsonObjectRequest request) {
        queue.add(request);
    }
}
