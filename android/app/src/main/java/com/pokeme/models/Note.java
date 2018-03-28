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

    @SerializedName("is_archived")
    private Boolean is_archived;

    @SerializedName("reminder_date")
    private Boolean reminder_date;

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

    public Boolean getisArchived() {
        return is_archived;
    }

    public Boolean getReminderDate() {
        return reminder_date;
    }

    @Override
    public String toString() {
        return getTitle();
    }
}
