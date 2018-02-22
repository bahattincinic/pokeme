package com.pokeme.service;

import com.android.volley.VolleyError;
import org.json.JSONObject;


public interface VolleyCallback {
    void onSuccess(JSONObject instance);
    void onError(VolleyError error);
}
