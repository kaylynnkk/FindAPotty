package com.example.findapotty.developer;

// Developer class holds developer information
public class Developer {
    private int image;
    private String name;
    private String school;
    private String email;
    private String quote;

    // Assigning values to image, name, school, email, and quote
    public Developer(int image, String name, String school, String email, String quote) {
        this.image = image;
        this.name = name;
        this.school = school;
        this.email = email;
        this.quote = quote;
    }

    public int getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public String getSchool() {
        return school;
    }

    public String getEmail() {
        return email;
    }

    public String getQuote() {
        return quote;
    }
}
