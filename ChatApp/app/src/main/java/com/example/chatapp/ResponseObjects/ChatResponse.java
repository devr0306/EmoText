package com.example.chatapp.ResponseObjects;

import com.example.chatapp.Models.API.Chat;
import com.google.gson.annotations.SerializedName;

public class ChatResponse {

    @SerializedName("success")
    boolean success;

    @SerializedName("message")
    String message;

    @SerializedName("chat")
    private Chat chat;

    public ChatResponse(boolean success, String message, Chat chat) {
        this.success = success;
        this.message = message;
        this.chat = chat;
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

    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

}
