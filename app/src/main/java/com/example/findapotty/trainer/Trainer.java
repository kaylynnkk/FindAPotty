package com.example.findapotty.trainer;

public class Trainer {
    private int image;
    private String title, description;

    // Assigning values to image, title, and description
    public Trainer(int image, String title, String description) {
        this.image = image;
        this.title = title;
        this.description = description;
    }

    public int getImage() { return image; }

    public String getTitle() { return title; }

    public String getDescription() { return description; }
}

