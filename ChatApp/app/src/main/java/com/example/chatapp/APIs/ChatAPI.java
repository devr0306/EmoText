package com.example.chatapp.APIs;

import com.example.chatapp.ResponseObjects.ChatListResponse;
import com.example.chatapp.ResponseObjects.ChatResponse;
import com.example.chatapp.ResponseObjects.MessageListResponse;
import com.example.chatapp.ResponseObjects.UserListResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ChatAPI {

    @GET("load-chats/")
    Call<ChatListResponse> loadChats(
      @Query("token") String token
    );

    @GET("load-chat/")
    Call<UserListResponse> loadChat(
            @Query("chatId") String chatId,
            @Query("token") String token
    );

    @FormUrlEncoded
    @POST("create-chat/")
    Call<ChatResponse> createChat(
            @Field("userTo") String userTo,
            @Field("token") String token
    );

    @GET("load-messages")
    Call<MessageListResponse> loadMessages(
            @Query("part") int part,
            @Query("chatId") String chatId,
            @Query("token") String token
    );
}
