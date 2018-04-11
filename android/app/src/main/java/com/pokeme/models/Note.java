package com.pokeme.models;

import com.google.gson.annotations.SerializedName;


public class Note {
    /*
        This class represents User Note
    */

    @SerializedName("id")
    private Integer id;

    @SerializedName("title")
    private String title;

    @SerializedName("text")
    private String text;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("reminder_date")
    private Boolean reminder_date;

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public Integer getId() {
        return id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public Boolean getReminderDate() {
        return reminder_date;
    }

    @Override
    public String toString() {
        return getTitle();
    }
}
