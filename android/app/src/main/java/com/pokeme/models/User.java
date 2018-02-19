package com.pokeme.models;

import com.google.gson.annotations.SerializedName;


public class User {
    /*
        This class represents API User
    */

    @SerializedName("username")
    private String username;

    @SerializedName("token")
    private String token;


    public String getUsername() {
        return username;
    }

    public String getToken() {
        return token;
    }
}
