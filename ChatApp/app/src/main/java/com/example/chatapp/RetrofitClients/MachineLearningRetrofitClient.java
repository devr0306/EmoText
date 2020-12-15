package com.example.chatapp.RetrofitClients;

import com.example.chatapp.APIs.MachineLearningAPI;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MachineLearningRetrofitClient {

    private static final String BASE_URL = "http://134.209.28.184:8000/";
    private static MachineLearningRetrofitClient machineLearningRetrofitClient;
    private Retrofit retrofit;

    private HttpLoggingInterceptor httpLoggingInterceptor;
    private OkHttpClient okHttpClient;

    private MachineLearningRetrofitClient(){
        httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        okHttpClient = new OkHttpClient
                .Builder()
                .addInterceptor(httpLoggingInterceptor)
                .build();

        retrofit = new Retrofit
                .Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
    }

    public static synchronized MachineLearningRetrofitClient getInstance(){

        if(machineLearningRetrofitClient == null)
            machineLearningRetrofitClient = new MachineLearningRetrofitClient();

        return machineLearningRetrofitClient;
    }

    public MachineLearningAPI getAPI(){
        return retrofit.create(MachineLearningAPI.class);
    }
}
