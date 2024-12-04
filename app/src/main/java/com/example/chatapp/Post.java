package com.example.chatapp;

import java.io.Serializable;

public class Post implements Serializable {
    private String title;
    private String content;
    private String imageUrl;

    public Post(String title, String content, String imageUrl) {
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}