package com.emotext.chatapp.APIs;

import com.emotext.chatapp.ResponseObjects.SentimentResponse;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface MachineLearningAPI {

    @POST("predict")
    Call<SentimentResponse> predict(
            @Body RequestBody params
    );
}
