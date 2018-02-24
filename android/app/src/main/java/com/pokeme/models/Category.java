package com.pokeme.models;

import com.google.gson.annotations.SerializedName;


public class Category {
    /*
        This class represents User Category
    */

    @SerializedName("id")
    private String id;

    @SerializedName("title")
    private String name;

    @SerializedName("created_at")
    private String createdAt;

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getCreatedAt() {
        return createdAt;
    }
}
