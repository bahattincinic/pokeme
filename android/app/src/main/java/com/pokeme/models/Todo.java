package com.pokeme.models;

import com.google.gson.annotations.SerializedName;


public class Todo {
    /*
        This class represents User Todo
    */

    @SerializedName("id")
    private String id;

    @SerializedName("title")
    private String title;

    @SerializedName("text")
    private String text;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("due_date")
    private String dueDate;

    @SerializedName("is_completed")
    private Boolean isCompleted;

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

    public String getDueDate() {
        return dueDate;
    }

    public Boolean getIsCompleted() {
        return isCompleted;
    }
}
