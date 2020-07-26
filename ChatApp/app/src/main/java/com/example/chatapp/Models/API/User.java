package com.example.chatapp.Models.API;

import java.util.ArrayList;

public class User {
    /*"id": "399f2b30-cd03-11ea-9fb1-af911206c707",
            "name": "dev123",
            "email": "dev123@gmail.com",
            "password": "$2a$10$whJnR5qGf2.AlCveYY78juq3HHP1QKD1i8EuDGZADrtjACe5g90Um",
            "profilePictureURL": null,
            "bio": null,
            "isDeleted": null,
            "username": "dev123",
            "seeRealName": null,
            "seeEmail": null,
            "textMe": null,
            "isOnline": null,
            "lastSeen": null,
            "addToGroupChats": null,
            "createdAt": "2020-07-23T16:40:32.356Z",
            "updatedAt": "2020-07-23T16:40:32.356Z"*/

    private String id;
    private String name;
    private String email;
    private String password;
    private String profilePictureURL;
    private String bio;
    private boolean isDeleted;
    private String username;
    private boolean seeRealName;
    private boolean seeEmail;
    private boolean textMe;
    private boolean isOnline;
    private String lastSeen;
    private boolean addToGroupChats;
    private String createdAt;
    private String updatedAt;

    private ArrayList<User> friends;

    private String token;

    public User(String name, String username, String email, String password){

        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;

        instantiateFriends();
    }

    public User(String id, String name, String email, String password, String profilePictureURL, String bio, boolean isDeleted,
                String username, boolean seeRealName, boolean seeEmail, boolean textMe, boolean isOnline, String lastSeen,
                boolean addToGroupChats, String createdAt, String updatedAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.profilePictureURL = profilePictureURL;
        this.bio = bio;
        this.isDeleted = isDeleted;
        this.username = username;
        this.seeRealName = seeRealName;
        this.seeEmail = seeEmail;
        this.textMe = textMe;
        this.isOnline = isOnline;
        this.lastSeen = lastSeen;
        this.addToGroupChats = addToGroupChats;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;

        instantiateFriends();
    }

    public void instantiateFriends(){

        if(friends == null)
            friends = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfilePictureURL() {
        return profilePictureURL;
    }

    public void setProfilePictureURL(String profilePictureURL) {
        this.profilePictureURL = profilePictureURL;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isSeeRealName() {
        return seeRealName;
    }

    public void setSeeRealName(boolean seeRealName) {
        this.seeRealName = seeRealName;
    }

    public boolean isSeeEmail() {
        return seeEmail;
    }

    public void setSeeEmail(boolean seeEmail) {
        this.seeEmail = seeEmail;
    }

    public boolean isTextMe() {
        return textMe;
    }

    public void setTextMe(boolean textMe) {
        this.textMe = textMe;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    public String getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(String lastSeen) {
        this.lastSeen = lastSeen;
    }

    public boolean isAddToGroupChats() {
        return addToGroupChats;
    }

    public void setAddToGroupChats(boolean addToGroupChats) {
        this.addToGroupChats = addToGroupChats;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setToken(String token){
        this.token = token;
    }

    public String getToken(){
        return token;
    }
}
