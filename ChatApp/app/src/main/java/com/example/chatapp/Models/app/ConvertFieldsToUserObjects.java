package com.example.chatapp.Models.app;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.chatapp.Models.API.Chat;
import com.example.chatapp.ResponseObjects.UserResponse;
import com.example.chatapp.RetrofitClients.AuthRetrofitClient;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConvertFieldsToUserObjects {

    private Context context;
    private static ConvertFieldsToUserObjects convertFieldsToUserObjects;
    public static ArrayList<User> users;
    private static int index;

    private ConvertFieldsToUserObjects(Context context) {

        this.context = context;
    }

    public static void resetVariables() {

        index = 0;
        users = new ArrayList<>();
    }

    public static ConvertFieldsToUserObjects getInstance(Context context) {

        if (convertFieldsToUserObjects == null)
            convertFieldsToUserObjects = new ConvertFieldsToUserObjects(context);

        resetVariables();

        return convertFieldsToUserObjects;
    }


    public void getUsersFromChats(Chat[] chats) {

        if (index < chats.length) {

            Call<UserResponse> getUser = AuthRetrofitClient
                    .getInstance()
                    .getAuthApi()
                    .getUserById(chats[index].getUserID());

            Log.i("Testing Method", chats[index].getUserID());

            getUser.enqueue(new Callback<UserResponse>() {
                @Override
                public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {

                    if (response.isSuccessful()) {

                        UserResponse ur = response.body();
                        Log.i("User", ur.getUser().getName());
                        users.add(ur.getUser());
                        Log.i("List", users.toString());

                        index++;
                        getUsersFromChats(chats);

                    } else {
                        try {
                            String errorBody = response.errorBody().string();
                            int index = errorBody.indexOf("\"message\":");

                            if (index != -1) {

                                String errorSub = errorBody.substring(index + 10);
                                //errorBody = errorSub.substring(1, errorSub.length() - 2);
                            }

                            Toast.makeText(context, errorBody, Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<UserResponse> call, Throwable t) {
                    Log.i("F up", t.toString());
                }
            });
        }
    }
}
