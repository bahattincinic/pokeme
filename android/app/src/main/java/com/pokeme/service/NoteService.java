package com.pokeme.service;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;


public class NoteService extends BaseService {

    public static JsonObjectRequest getNotes(final String token, final VolleyCallback callback) throws JSONException {

        return makeRequestWithToken(
                Config.getURL(Config.NOTE_LIST_URL),
                Request.Method.GET,
                token,
                callback,
                null
        );
    }

    public static JsonObjectRequest deleteNote(final String token, int noteId, final VolleyCallback callback) throws JSONException {

        return makeRequestWithToken(
                String.format(Config.getURL(Config.NOTE_DETAIL), noteId),
                Request.Method.DELETE,
                token,
                callback,
                null
        );
    }

    public static JsonObjectRequest createNote(String title, String text, Integer category, String token, final VolleyCallback callback) throws JSONException {
        JSONObject data = new JSONObject();
        data.put("title", title);
        data.put("text", text);
        data.put("category", category);

        return makeRequestWithToken(
                Config.getURL(Config.NOTE_LIST_URL),
                Request.Method.POST,
                token,
                callback,
                data
        );
    }
}
