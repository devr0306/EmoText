package com.example.chatapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.chatapp.APIs.ContactsAPI;
import com.example.chatapp.Adapters.RequestsRecyclerViewAdapter;
import com.example.chatapp.Models.API.FriendRequest;
import com.example.chatapp.Models.app.User;
import com.example.chatapp.ResponseObjects.FriendRequestsResponse;
import com.example.chatapp.RetrofitClients.ContactsAPIClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequestsActivity extends AppCompatActivity {

    private RecyclerView requestsRecyclerView;
    private ArrayList<FriendRequest> friendRequests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests);

        init();
    }

    public void init(){

        requestsRecyclerView = findViewById(R.id.requests_recycler_view);
        initializeLists();



    }

    public void initializeLists(){

        friendRequests = new ArrayList<>();

        Call<FriendRequestsResponse> getFriendRequestsCall = ContactsAPIClient
                .getInstance()
                .getContactsAPI()
                .getFriendRequests(SharedPrefManager.getInstance(RequestsActivity.this).getUser().getToken());

        getFriendRequestsCall.enqueue(new Callback<FriendRequestsResponse>() {
            @Override
            public void onResponse(Call<FriendRequestsResponse> call, Response<FriendRequestsResponse> response) {

                if(response.isSuccessful()){

                    FriendRequestsResponse frr = response.body();
                    Toast.makeText(RequestsActivity.this, Arrays.toString(frr.getRequests()), Toast.LENGTH_SHORT).show();


                    if(frr.getRequests() != null && frr.getRequests().length != 0){

                        friendRequests = frr.returnFriendsList();
                        RequestsRecyclerViewAdapter requestsRecyclerViewAdapter = new RequestsRecyclerViewAdapter(RequestsActivity.this);
                        requestsRecyclerViewAdapter.setLists(friendRequests);

                        requestsRecyclerView.setAdapter(requestsRecyclerViewAdapter);
                        requestsRecyclerView.setLayoutManager(new LinearLayoutManager(RequestsActivity.this));
                    }
                }

                else{
                    try {
                        String errorBody = response.errorBody().string();
                        int index = errorBody.indexOf("\"message\":");

                        if(index != -1){

                            String errorSub = errorBody.substring(index + 10);
                            errorBody = errorSub.substring(1, errorSub.length() - 2);
                        }

                        Toast.makeText(RequestsActivity.this, errorBody, Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<FriendRequestsResponse> call, Throwable t) {

            }
        });
    }
}