package com.emotext.chatapp.ResponseObjects;

import com.emotext.chatapp.Models.API.Chat;
import com.google.gson.annotations.SerializedName;

public class ChatListResponse {

    @SerializedName("success")
    boolean success;

    @SerializedName("message")
    String message;

    @SerializedName("chats")
    private Chat[] chats;

    public ChatListResponse(boolean success, String message, Chat[] chats) {
        this.success = success;
        this.message = message;
        this.chats = chats;
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

    public Chat[] getChats() {
        return chats;
    }

    public void setChats(Chat[] chats) {
        this.chats = chats;
    }
}
