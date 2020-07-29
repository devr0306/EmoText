package com.example.chatapp.APIs;

import com.example.chatapp.ResponseObjects.ContactListResponse;
import com.example.chatapp.ResponseObjects.DefaultResponse;
import com.example.chatapp.ResponseObjects.FriendRequestsResponse;
import com.example.chatapp.ResponseObjects.UserListResponse;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ContactsAPI {

    @GET("get-contacts/")
    Call<ContactListResponse> getContacts(
            @Query("token") String token
    );

    @GET("get-friend-requests/")
    Call<FriendRequestsResponse> getFriendRequests(
            @Query("token") String token
    );

    @GET("find-contacts/")
    Call<UserListResponse> findContacts(
            @Query("searchQuery") String searchQuery
    );

    @FormUrlEncoded
    @POST("send-friend-request/")
    Call<DefaultResponse> sendFriendRequest(
            @Field("token") String token,
            @Field("friend") String friendId
    );


    @FormUrlEncoded
    @POST("accept-friend-request/")
    Call<DefaultResponse> acceptFriendRequest(
            @Field("token") String token,
            @Field("requestID") String requestID
    );

    @FormUrlEncoded
    @POST("decline-friend-request/")
    Call<DefaultResponse> declineFriendRequest(
            @Field("token") String token,
            @Field("requestID") String requestID
    );

    @FormUrlEncoded
    @POST("ignore-friend-request/")
    Call<DefaultResponse> ignoreFriendRequest(
            @Field("token") String token,
            @Field("requestID") String requestID
    );

    @FormUrlEncoded
    @POST("cancel-friend-request/")
    Call<DefaultResponse> cancelFriendRequest(
            @Field("token") String token,
            @Field("requestID") String requestID
    );
}
