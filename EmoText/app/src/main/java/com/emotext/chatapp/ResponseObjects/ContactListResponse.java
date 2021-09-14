package com.emotext.chatapp.ResponseObjects;

import com.emotext.chatapp.Models.API.Contact;
import com.emotext.chatapp.Models.app.User;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ContactListResponse {

    @SerializedName("success")
    boolean success;

    @SerializedName("message")
    String message;

    @SerializedName("contacts")
    Contact[] contacts;

    @SerializedName("users")
    User[] users;

    public ContactListResponse(boolean success, String message, Contact[] contacts, User[] users) {
        this.success = success;
        this.message = message;
        this.contacts = contacts;
        this.users = users;
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

    public Contact[] getContacts() {
        return contacts;
    }

    public void setContacts(Contact[] contacts) {
        this.contacts = contacts;
    }

    public ArrayList<Contact> getContactsasList(){

        ArrayList<Contact> contactsList = new ArrayList<>();

        for(Contact c: contacts)
            contactsList.add(c);

        return contactsList;
    }

    public User[] getUsers() {
        return users;
    }

    public void setUsers(User[] users) {
        this.users = users;
    }

    public ArrayList<User> getUserAsList(){

        ArrayList<User> userArrayList = new ArrayList<>();

        for(User us: users)
            userArrayList.add(us);

        return userArrayList;
    }
}
