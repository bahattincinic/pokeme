package com.pokeme.models;

import com.google.gson.annotations.SerializedName;


public class Category {
    /*
        This class represents User Category
    */

    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }
}
