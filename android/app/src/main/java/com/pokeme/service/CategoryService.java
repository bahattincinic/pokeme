package com.pokeme.service;


import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;


public class CategoryService extends BaseService {
    public static JsonObjectRequest getCategories(final String token, final VolleyCallback callback) throws JSONException {
        return makeRequestWithToken(
                Config.getURL(Config.CATEGORY_LIST_URL),
                Request.Method.GET,
                token,
                callback,
                null
        );
    }

    public static JsonObjectRequest deleteCategory(final String token, int categoryId, final VolleyCallback callback) throws JSONException {
        return makeRequestWithToken(
                String.format(Config.getURL(Config.CATEGORY_DETAIL), categoryId),
                Request.Method.DELETE,
                token,
                callback,
                null
        );
    }

    public static JsonObjectRequest createCategory(String name, String token, final VolleyCallback callback) throws JSONException {
        JSONObject data = new JSONObject();
        data.put("name", name);

        return makeRequestWithToken(
                Config.getURL(Config.CATEGORY_LIST_URL),
                Request.Method.POST,
                token,
                callback,
                data
        );
    }
}
