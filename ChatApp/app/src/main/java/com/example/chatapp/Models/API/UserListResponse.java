package com.example.chatapp.Models.API;

import com.google.gson.annotations.SerializedName;

public class UserListResponse {

    @SerializedName("success")
    boolean success;

    @SerializedName("message")
    String message;

    @SerializedName("contacts")
    User[] contacts;

    public UserListResponse(boolean success, String message, User[] contacts) {
        this.success = success;
        this.message = message;
        this.contacts = contacts;
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

    public User[] getContacts() {
        return contacts;
    }

    public void setContacts(User[] contacts) {
        this.contacts = contacts;
    }
}
