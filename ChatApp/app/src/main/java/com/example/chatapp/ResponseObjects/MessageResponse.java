package com.example.chatapp.ResponseObjects;

import com.example.chatapp.Models.API.Message;
import com.google.gson.annotations.SerializedName;

public class MessageResponse {

    @SerializedName("success")
    boolean success;

    @SerializedName("message")
    String message;

    @SerializedName("msg")
    Message msg;

    public MessageResponse(boolean success, String message, Message msg) {
        this.success = success;
        this.message = message;
        this.msg = msg;
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

    public Message getMsg() {
        return msg;
    }

    public void setMsg(Message msg) {
        this.msg = msg;
    }
}
