package com.emotext.chatapp.ResponseObjects;

import com.emotext.chatapp.Models.app.User;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class UserListResponse {

    @SerializedName("success")
    boolean success;

    @SerializedName("message")
    String message;

    @SerializedName("people")
    User[] people;

    public UserListResponse(boolean success, String message, User[] contacts) {
        this.success = success;
        this.message = message;
        this.people = contacts;
    }

    public ArrayList<User> convertToList(){

        ArrayList<User> userList = new ArrayList<>();

        for(User us: people)
            userList.add(us);

        return userList;
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
        return people;
    }

    public void setContacts(User[] contacts) {
        this.people = contacts;
    }
}
