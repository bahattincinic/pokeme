package com.pokeme;


public class ApiClient {
    /*
    This Class contains API Integrations.
    */

    public static final String API_DOMAIN = "http://localhost:8080";
    public static final String LOGIN_URL = "/token/";
    public static final String SIGNUP_URL = "/signup/";
    public static final String TODO_LIST_URL = "/todos/";

    public String getURL(String url) {
        return API_DOMAIN + url;
    }
}