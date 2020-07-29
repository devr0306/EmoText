package com.example.chatapp.ResponseObjects;

import com.example.chatapp.Models.app.User;
import com.google.gson.annotations.SerializedName;

public class UserResponse {

    @SerializedName("success")
    boolean success;

    @SerializedName("message")
    String message;

    User user;

    public UserResponse(boolean success, String message, User user) {
        this.success = success;
        this.message = message;
        this.user = user;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User getUser(){
        return user;
    }

    public void setUser(User user){
        this.user = user;
    }
}
