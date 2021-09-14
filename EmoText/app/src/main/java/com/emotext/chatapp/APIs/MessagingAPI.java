package com.emotext.chatapp.APIs;

import com.emotext.chatapp.ResponseObjects.MessageListResponse;
import com.emotext.chatapp.ResponseObjects.MessageResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface MessagingAPI {

    @FormUrlEncoded
    @POST("send-message/")
    Call<MessageResponse> sendMessage(
            @Field("text") String text,
            @Field("token") String token,
            @Field("chatID") String chatID,
            @Field("isGroupChat") boolean isGroupChat
    );

    @GET("search-message/")
    Call<MessageListResponse> searchMessage(
            @Query("chatID") String chatID,
            @Query("searchQuery") String searchQuery,
            @Query("token") String token,
            @Query("isGroupChat") boolean isGroupChat
    );
}
