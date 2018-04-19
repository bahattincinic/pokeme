package com.pokeme.service;

import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;


public class UserService extends BaseService {
    public static JsonObjectRequest authenticate(String username, String password, final VolleyCallback callback) throws JSONException {
        JSONObject data = new JSONObject();
        data.put("username", username);
        data.put("password", password);

        return makeRequest(
                Config.getURL(Config.LOGIN_URL),
                Request.Method.POST,
                callback,
                data
        );
    }

    public static JsonObjectRequest signup(String username, String password, final VolleyCallback callback) throws JSONException {
        JSONObject data = new JSONObject();
        data.put("username", username);
        data.put("password", password);

        return makeRequest(
                Config.getURL(Config.SIGNUP_URL),
                Request.Method.POST,
                callback,
                data
        );
    }

    public static JsonObjectRequest getProfile(final String token, final VolleyCallback callback) throws JSONException {
        return makeRequestWithToken(
                Config.getURL(Config.PROFILE_URL),
                Request.Method.GET,
                token,
                callback,
                null
        );
    }

    public static JsonObjectRequest updateProfile(final String token, final String username, final String password, final VolleyCallback callback) throws JSONException {
        JSONObject data = new JSONObject();
        data.put("username", username);

        if (!TextUtils.isEmpty(password)) {
            data.put("password", password);
        }

        return makeRequestWithToken(
                Config.getURL(Config.PROFILE_URL),
                Request.Method.PUT,
                token,
                callback,
                data
        );
    }
}
