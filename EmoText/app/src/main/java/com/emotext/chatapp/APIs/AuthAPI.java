package com.emotext.chatapp.APIs;

import com.emotext.chatapp.ResponseObjects.DefaultResponse;
import com.emotext.chatapp.ResponseObjects.LoginResponse;
import com.emotext.chatapp.ResponseObjects.UserResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface AuthAPI {

    @FormUrlEncoded
    @POST("signup/")
    Call<DefaultResponse> signup(
      @Field("name") String name,
      @Field("username") String username,
      @Field("email") String email,
      @Field("password") String password
    );

    @FormUrlEncoded
    @POST("signin/")
    Call<LoginResponse> signin(
            @Field("email") String email,
            @Field("password") String password
    );

    @GET("logout/")
    Call<DefaultResponse> logout(
            @Query("token") String token
    );

    @GET("get-user-by-token/")
    Call<UserResponse> getUserFromToken(
            @Query("token") String token
    );

    @GET("get-user-by-id/")
    Call<UserResponse> getUserById(
            @Query("id") String id
    );
}
