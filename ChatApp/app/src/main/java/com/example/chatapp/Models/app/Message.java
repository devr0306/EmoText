package com.example.chatapp.Models.app;

public class Message {

    private final static int RIGHT_MESSAGE = 0;
    private final static int LEFT_MESSAGE = 1;

    private String text;

    public Message(){
        this("");
    }

    public Message(String txt){
        text = txt;
        checkMessageSide();
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    //TODO- finish this after the Database
    public int checkMessageSide(){

        return RIGHT_MESSAGE;
    }

}
