package com.pokeme.models;

import com.google.gson.annotations.SerializedName;


public class Category {
    /*
        This class represents User Category
    */

    @SerializedName("id")
    private Integer id;

    @SerializedName("name")
    private String name;

    public String getName() {
        return name;
    }

    public Integer getId() {
        return id;
    }

    @Override
    public String toString() {
        return getName();
    }
}
