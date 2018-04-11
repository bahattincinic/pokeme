package com.pokeme.service;


public class Config {
    public static String API_DOMAIN = "http://192.168.0.11:8080";
    public static String LOGIN_URL = "/token/";
    public static String SIGNUP_URL = "/signup/";
    public static String CATEGORY_LIST_URL = "/categories/";
    public static String CATEGORY_DETAIL = "/categories/%d/";
    public static String CATEGORY_NOTES = "/categories/%d/notes/";
    public static String NOTE_LIST_URL = "/notes/";
    public static String NOTE_DETAIL = "/notes/%d/";
    public static String PROFILE_URL = "/me/";

    public static String API_TOKEN_SESSION_KEY = "AccessToken";
    public static String FIREBASE_TOKEN = "FirebaseToken";

    public static String getURL(String url) {
        return API_DOMAIN + url;
    }
}
