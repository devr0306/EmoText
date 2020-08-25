package com.example.chatapp.ResponseObjects;

import com.example.chatapp.Models.API.Message;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class MessageListResponse {

    @SerializedName("success")
    boolean success;

    @SerializedName("message")
    String message;

    @SerializedName("messages")
    Message[] messages;

    public MessageListResponse(boolean success, String message, Message[] messages) {
        this.success = success;
        this.message = message;
        this.messages = messages;
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

    public Message[] getMessages() {
        return messages;
    }

    public void setMessages(Message[] messages) {
        this.messages = messages;
    }

    public ArrayList<Message> getMessagesArrayList(){

        ArrayList<Message> messagesList = new ArrayList<>();

        for(Message m: messages)
            messagesList.add(m);

        return messagesList;
    }
}
