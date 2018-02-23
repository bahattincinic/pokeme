package com.pokeme.service;


import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;


public class TodoService extends BaseService {
    public static JsonObjectRequest getTodos(final String token, final VolleyCallback callback) throws JSONException {
        return makeRequestWithToken(
                Config.getURL(Config.TODO_LIST_URL),
                Request.Method.GET,
                token,
                callback,
                null
        );
    }

    public static JsonObjectRequest deleteTodo(final String token, int todoId, final VolleyCallback callback) throws JSONException {
        return makeRequestWithToken(
                String.format(Config.getURL(Config.TODO_DETAIL), todoId),
                Request.Method.DELETE,
                token,
                callback,
                null
        );
    }

    public static JsonObjectRequest createTodo(String title, String text, String due_date, Boolean is_completed, String token, final VolleyCallback callback) throws JSONException {
        JSONObject data = new JSONObject();
        data.put("title", title);
        data.put("text", text);

        if (due_date != null || due_date.isEmpty()) {
            data.put("due_date", due_date);
        }

        data.put("is_completed", is_completed);

        return makeRequestWithToken(
                Config.getURL(Config.TODO_LIST_URL),
                Request.Method.GET,
                token,
                callback,
                data
        );
    }
}
