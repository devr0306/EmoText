package com.example.chatapp.Models.API;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Chat {
    /*
        "id": "3310d930-db45-11ea-b0ce-73de8337134c",
        "userID": "f00c16b0-db42-11ea-aca0-1d196dcaf245",
        "isGroupChat": false,
        "name": "abc",
        "pictureURL": "",
        "lastMessage": {}     */

    private String id;
    private String userID;
    private boolean isGroupChat;
    private String name;
    private String pictureURL;
    private Message lastMessage;

    public Chat(String id, String userID, String name, String pictureURL, boolean isGroupChat, Message lastMessage) {
        this.id = id;
        this.userID = userID;
        this.name = name;
        this.pictureURL = pictureURL;
        this.isGroupChat = isGroupChat;
        this.lastMessage = lastMessage;
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

    public boolean isGroupChat() {
        return isGroupChat;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getPictureURL() {
        return pictureURL;
    }

    public void setPictureURL(String pictureURL) {
        this.pictureURL = pictureURL;
    }

    public Message getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(Message lastMessage) {
        this.lastMessage = lastMessage;
    }

    public void setGroupChat(boolean groupChat) {
        isGroupChat = groupChat;
    }

    public int compareDateTo(Chat other) throws ParseException {

        Message otherMessage = other.lastMessage;

        Log.i("TestingChat", lastMessage.getCreatedAt() + " " + otherMessage.getCreatedAt());

        SimpleDateFormat toDateThis = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        SimpleDateFormat toDateOther = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

        if(lastMessage == null && otherMessage == null)
            return 0;

        if(lastMessage == null)
            return 1;

        if(otherMessage == null)
            return -1;

        Date thisDate = toDateThis.parse(lastMessage.getCreatedAt());
        Date otherDate = toDateOther.parse(otherMessage.getCreatedAt());

        Log.i("TestingChat", thisDate.toString() + " " + otherDate.toString());

        return thisDate.compareTo(otherDate);
    }
}
