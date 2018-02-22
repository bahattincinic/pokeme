package com.pokeme.service;


public class Config {
    public static String API_DOMAIN = "http://localhost:8080";
    public static String LOGIN_URL = "/token/";
    public static String SIGNUP_URL = "/signup/";
    public static String TODO_LIST_URL = "/todos/";
    public static String PROFILE_URL = "/me/";

    public static String TOKEN_SESSION_KEY = "AccessToken";

    public static String getURL(String url) {
        return API_DOMAIN + url;
    }
}
