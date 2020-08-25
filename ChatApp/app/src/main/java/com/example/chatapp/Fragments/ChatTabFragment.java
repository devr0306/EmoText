package com.example.chatapp.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chatapp.Adapters.ChatTabRecyclerViewAdapter;
import com.example.chatapp.Models.API.Chat;
import com.example.chatapp.Models.app.ConvertFieldsToUserObjects;
import com.example.chatapp.Models.app.User;
import com.example.chatapp.R;
import com.example.chatapp.ResponseObjects.ChatListResponse;
import com.example.chatapp.ResponseObjects.UserListResponse;
import com.example.chatapp.ResponseObjects.UserResponse;
import com.example.chatapp.RetrofitClients.AuthRetrofitClient;
import com.example.chatapp.RetrofitClients.ChatRetrofitClient;
import com.example.chatapp.SharedPrefManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ChatTabFragment extends Fragment {

    //Main View and the TextView for when no chats exist
    View contactFragmentView;
    private TextView noChats;

    //Everything related to the RecyclerView
    private ArrayList<User> users;
    RecyclerView chatsRecyclerView;
    private ChatTabRecyclerViewAdapter chatTabRecyclerViewAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        contactFragmentView = inflater.inflate(R.layout.fragment_chat_tab, container, false);

        init();
        setLists();

        // Inflate the layout for this fragment
        return contactFragmentView;
    }

    public void init(){

        //Initialize Views
        noChats = contactFragmentView.findViewById(R.id.no_chats_message);
        noChats.setVisibility(View.GONE);

        //Initialize RecyclerView and Adapter, and link the both
        chatsRecyclerView = contactFragmentView.findViewById(R.id.chats_recycler_view);
        chatTabRecyclerViewAdapter = new ChatTabRecyclerViewAdapter(getContext());
        chatsRecyclerView.setAdapter(chatTabRecyclerViewAdapter);
        chatsRecyclerView.setLayoutManager(new LinearLayoutManager(contactFragmentView.getContext()));

    }

    //Set the users ArrayList by first getting the Chats Array and then converting to User ArrayList
    public void setLists(){

        users = new ArrayList<User>();

        Call<ChatListResponse> getAllChats = ChatRetrofitClient
                .getInstance()
                .getChatAPI()
                .loadChats(SharedPrefManager.getInstance(getContext()).getUser().getToken());

        getAllChats.enqueue(new Callback<ChatListResponse>() {
            @Override
            public void onResponse(Call<ChatListResponse> call, Response<ChatListResponse> response) {

                if(response.isSuccessful()){

                    ChatListResponse clr = response.body();

                    Log.i("Testing", Arrays.toString(clr.getChats()));

                    if(clr.getChats() != null && clr.getChats().length > 0)
                        convertToUsers(clr.getChats());

                    else if(clr.getChats() == null || clr.getChats().length < 1 || users.size() < 1)
                        noChats.setVisibility(View.VISIBLE);
                }

                else{
                    try {
                        String errorBody = response.errorBody().string();
                        int index = errorBody.indexOf("\"message\":");

                        if(index != -1){

                            String errorSub = errorBody.substring(index + 10);
                            errorBody = errorSub.substring(1, errorSub.length() - 2);
                        }

                        Toast.makeText(getContext(), errorBody, Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ChatListResponse> call, Throwable t) {
                Log.i("Failure", t.toString());
            }
        });
    }

    //Convert the chats Array to a User ArrayList
    public void convertToUsers(Chat[] chats){

        for(Chat chat: chats){

            Call<UserResponse> getUser = AuthRetrofitClient
                    .getInstance()
                    .getAuthApi()
                    .getUserById(chat.getUserID());


            getUser.enqueue(new Callback<UserResponse>() {
                @Override
                public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {

                    if (response.isSuccessful()) {

                        UserResponse ur = response.body();
                        users.add(ur.getUser());
                        chatTabRecyclerViewAdapter.setUsersList(users);

                    } else {
                        try {
                            String errorBody = response.errorBody().string();
                            int index = errorBody.indexOf("\"message\":");

                            if (index != -1) {

                                String errorSub = errorBody.substring(index + 10);
                                //errorBody = errorSub.substring(1, errorSub.length() - 2);
                            }

                            Toast.makeText(getContext(), errorBody, Toast.LENGTH_SHORT).show();
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