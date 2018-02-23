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


public class TodoService {
    public static JsonObjectRequest getTodos(final String token, final VolleyCallback callback) throws JSONException {

        return new JsonObjectRequest(
                Request.Method.GET,
                Config.getURL(Config.TODO_LIST_URL),
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

    public static JsonObjectRequest deleteTodo(final String token, int todoId, final VolleyCallback callback) throws JSONException {

        return new JsonObjectRequest(
                Request.Method.DELETE,
                String.format(Config.getURL(Config.TODO_DETAIL), todoId),
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

    public static JsonObjectRequest createTodo(String title, String text, String due_date, Boolean is_completed, final VolleyCallback callback) throws JSONException {
        JSONObject params = new JSONObject();
        params.put("title", title);
        params.put("text", text);

        if (due_date != null || due_date.isEmpty()) {
            params.put("due_date", due_date);
        }

        params.put("is_completed", is_completed);

        return new JsonObjectRequest(
                Request.Method.POST,
                Config.getURL(Config.TODO_LIST_URL),
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
}
