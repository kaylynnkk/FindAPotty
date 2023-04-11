package com.example.findapotty.feed;

// Article class to be used to retrieve the components of an article for newAPI
public class Article {
    private String author, title, description, url, urlToImage, publishedAt;
    private int image;

    //Constructors for the articles components which consist of title, description, url, urlToImage, and publishedAt
    public Article(String author, String title, String description, int image) {
        this.author = author;
        this.title = title;
        this.description = description;
        this.url = url;
        this.urlToImage = urlToImage;
        this.publishedAt = publishedAt;
        this.image = image;
    }

    //Getters and setters for title, description, url, urlToImage, and publishedAt

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

    public void setDescription(String description) {
        this.description = description;
    }

    //URL
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    //urlToImage
    public String getUrlToImage() {
        return urlToImage;
    }

    public void setUrlToImage(String urlToImage) {
        this.urlToImage = urlToImage;
    }

    //publishedAt
    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public int getImage() { return image;
    }
}