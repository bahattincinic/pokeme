package com.pokeme;


import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;


public class ApiClient {
    /*
    This Class contains API Integrations.
    */

    private static ApiClient instance;
    private RequestQueue queue;
    private static final String API_DOMAIN = "http://localhost:8080";
    private static final String LOGIN_URL = "/token/";
    private static final String SIGNUP_URL = "/signup/";
    private static final String TODO_LIST_URL = "/todos/";

    private ApiClient(Context context) {
        queue = Volley.newRequestQueue(context.getApplicationContext());
    }

    public static ApiClient getInstance(Context context) {
        if (instance == null) {
            instance = new ApiClient(context);
        }
        return instance;
    }

    private String getURL(String url) {
        return API_DOMAIN + url;
    }
}
