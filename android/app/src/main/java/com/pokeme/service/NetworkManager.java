package com.pokeme.service;


import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;


public class NetworkManager {
    /*
    This Class contains API Integrations.
    */

    private static NetworkManager instance;
    private RequestQueue queue;

    private NetworkManager(Context context) {
        queue = Volley.newRequestQueue(context.getApplicationContext());
    }

    public static NetworkManager getInstance(Context context) {
        if (instance == null) {
            instance = new NetworkManager(context);
        }
        return instance;
    }

    public void add(JsonObjectRequest request) {
        queue.add(request);
    }
}
