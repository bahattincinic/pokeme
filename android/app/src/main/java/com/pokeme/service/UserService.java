package com.pokeme.service;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class UserService {
    public static JsonObjectRequest authenticate(String username, String password, final VolleyCallback callback) throws JSONException {
        JSONObject params = new JSONObject();
        params.put("username", username);
        params.put("password", password);

        return new JsonObjectRequest(
                Request.Method.POST,
                Config.getURL(Config.LOGIN_URL),
                params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        callback.onSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onError(error);
                    }
                }
        );
    }

    public static JsonObjectRequest signup(String username, String password, final VolleyCallback callback) throws JSONException {
        JSONObject params = new JSONObject();
        params.put("username", username);
        params.put("password", password);

        return new JsonObjectRequest(
                Request.Method.POST,
                Config.getURL(Config.SIGNUP_URL),
                params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        callback.onSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onError(error);
                    }
                }
        );
    }

    public static JsonObjectRequest profile(final String token, final VolleyCallback callback) throws JSONException {

        return new JsonObjectRequest(
                Request.Method.GET,
                Config.getURL(Config.PROFILE_URL),
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        callback.onSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onError(error);
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Token " + token);
                return params;
            }
        };
    }
}
