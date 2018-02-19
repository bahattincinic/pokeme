package com.pokeme.models;

import com.google.gson.annotations.SerializedName;


public class Note {
    /*
        This class represents User Note
    */

    @SerializedName("id")
    private String id;

    @SerializedName("title")
    private String title;

    @SerializedName("text")
    private String text;

    @SerializedName("created_at")
    private String createdAt;

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public String getId() {
        return id;
    }

    public String getCreatedAt() {
        return createdAt;
    }
}
