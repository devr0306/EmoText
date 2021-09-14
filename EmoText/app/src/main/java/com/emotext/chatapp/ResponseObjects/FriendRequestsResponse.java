package com.emotext.chatapp.ResponseObjects;

import android.util.Log;

import com.emotext.chatapp.Models.API.FriendRequest;
import com.emotext.chatapp.Models.app.User;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class FriendRequestsResponse {

    @SerializedName("success")
    boolean success;

    @SerializedName("message")
    String message;

    @SerializedName("contacts")
    FriendRequest[] requests;

    @SerializedName("users")
    User[] people;

    public FriendRequestsResponse(boolean success, String message, FriendRequest[] requests, User[] people) {
        this.success = success;
        this.message = message;
        this.requests = requests;
        this.people = people;
    }

    public ArrayList<FriendRequest> returnFriendsList(){

        ArrayList<FriendRequest> friendRequestArrayList = new ArrayList<>();

        for(FriendRequest fr: requests)
            friendRequestArrayList.add(fr);

        Log.d("Listconvert", "" + friendRequestArrayList.size());
        return friendRequestArrayList;
    }

    public ArrayList<User> returnPeopleList(){

        ArrayList<User> peopleList = new ArrayList<>();

        for(User us: people)
            peopleList.add(us);

        return peopleList;
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

    public FriendRequest[] getRequests() {
        return requests;
    }

    public void setRequests(FriendRequest[] requests) {
        this.requests = requests;
    }

    public User[] getPeople() {
        return people;
    }

    public void setPeople(User[] people) {
        this.people = people;
    }
}
