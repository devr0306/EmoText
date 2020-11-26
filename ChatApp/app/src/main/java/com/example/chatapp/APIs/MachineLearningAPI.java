package com.example.chatapp.APIs;

import com.example.chatapp.ResponseObjects.SentimentResponse;

import java.util.ArrayList;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface MachineLearningAPI {

    @POST("predict")
    Call<SentimentResponse> predict(
            @Body RequestBody params
    );
}
