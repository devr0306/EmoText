package com.example.chatapp;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.chatapp.Models.app.User;

public class SharedPrefManager {

    private static final String SHARED_PREF_NAME = "my_shared_pref";

    private static SharedPrefManager sharedPrefManager;
    private Context context;

    private SharedPrefManager(Context cont){
        context = cont;
    }

    public static synchronized SharedPrefManager getInstance(Context cont){

        if(sharedPrefManager == null)
            sharedPrefManager = new SharedPrefManager(cont);

        return sharedPrefManager;
    }

    public void saveUser(User user){

        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("name", user.getName());
        editor.putString("username", user.getUsername());
        editor.putString("email", user.getEmail());
        editor.putString("password", user.getPassword());

        editor.apply();
    }

    public void login(String token){

        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("token", token);

        editor.apply();
    }

    public boolean isLoggedIn(){

        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("save", false) && (sharedPreferences.getString("token", null) != null);
    }

    public void setProfilePic(String profilePic){

        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("profilePic", profilePic);
        editor.apply();
    }

    public User getUser(){

        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

        User user = new User(
                sharedPreferences.getString("name", null),
                sharedPreferences.getString("username", null),
                sharedPreferences.getString("email", null),
                sharedPreferences.getString("password", null)
                );

        user.setToken(sharedPreferences.getString("token", null));
        user.setProfilePictureURL(sharedPreferences.getString("profilePic", null));

        return user;
    }

    public void rememberUser(){

        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean("save", true);
        editor.apply();
    }

    public void clear(){

        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.clear();
        editor.apply();
    }
}
