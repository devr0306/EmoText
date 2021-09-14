package com.emotext.chatapp.Models.app;

public interface SendChat {

    public void checkIfPersonIsChat(String text);
    public void createChat(String text);
    public void sendMessage(String text);
}
