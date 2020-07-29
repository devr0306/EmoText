package com.example.chatapp.Models.API;

public class FriendRequest {

    private String id;
    private String userFrom;
    private String userTo;
    private boolean didAccept;
    private boolean didReject;
    private boolean didIgnore;

    public FriendRequest(String id, String userFrom, String userTo, boolean didAccept, boolean didReject, boolean didIgnore) {
        this.id = id;
        this.userFrom = userFrom;
        this.userTo = userTo;
        this.didAccept = didAccept;
        this.didReject = didReject;
        this.didIgnore = didIgnore;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserFrom() {
        return userFrom;
    }

    public void setUserFrom(String userFrom) {
        this.userFrom = userFrom;
    }

    public String getUserTo() {
        return userTo;
    }

    public void setUserTo(String userTo) {
        this.userTo = userTo;
    }

    public boolean isDidAccept() {
        return didAccept;
    }

    public void setDidAccept(boolean didAccept) {
        this.didAccept = didAccept;
    }

    public boolean isDidReject() {
        return didReject;
    }

    public void setDidReject(boolean didReject) {
        this.didReject = didReject;
    }

    public boolean isDidIgnore() {
        return didIgnore;
    }

    public void setDidIgnore(boolean didIgnore) {
        this.didIgnore = didIgnore;
    }
}
