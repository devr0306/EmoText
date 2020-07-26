package com.example.chatapp.Models.API;

import com.google.gson.annotations.SerializedName;

public class DefaultResponse {

    @SerializedName("success")
    boolean success;

    @SerializedName("message")
    String message;

    public DefaultResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
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
}
