package com.example.findapotty.feed;

// Article class to be used to retrieve the components of an article for newAPI
public class Article {
    private String author, title, description;
    private int image;

    //Constructors for the articles components which consist of title, description, url, urlToImage, and publishedAt
    public Article(String author, String title, String description, int image) {
        this.author = author;
        this.title = title;
        this.description = description;
        this.image = image;
    }

    //Getters and setters for title, description, and image

    //Author
    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    //Title
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    //Description
    public String getDescription() {
        return description;
    }

    public int getImage() { return image;
    }
}