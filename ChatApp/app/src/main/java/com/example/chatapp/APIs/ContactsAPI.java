package com.example.chatapp.APIs;

import com.example.chatapp.Models.API.UserListResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ContactsAPI {

    @GET("get-contacts/")
    Call<UserListResponse> getContacts(
            @Query("token") String token
    );
}
