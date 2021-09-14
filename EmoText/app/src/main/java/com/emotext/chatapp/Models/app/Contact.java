package com.emotext.chatapp.Models.app;

public class Contact {

    private String name;
    private String imageURL;

    public Contact(String name, String imageURL) {
        this.name = name;
        this.imageURL = imageURL;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getName() {
        return name;
    }

    public String getImageURL() {
        return imageURL;
    }
}

