package com.example.findapotty.model;

import com.google.firebase.database.Exclude;

public class Item {

    private String id;

    public Item() {
    }

    @Exclude
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
