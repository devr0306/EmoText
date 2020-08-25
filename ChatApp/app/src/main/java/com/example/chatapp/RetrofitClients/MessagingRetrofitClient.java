package com.example.chatapp.RetrofitClients;

import com.example.chatapp.APIs.ChatAPI;
import com.example.chatapp.APIs.MessagingAPI;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MessagingRetrofitClient {

    private static final String BASE_URL = "http://10.0.0.185:5000/api/messaging/";
    private static MessagingRetrofitClient messagingRetrofitClient;
    private Retrofit retrofit;

    private HttpLoggingInterceptor httpLoggingInterceptor;
    private OkHttpClient okHttpClient;

    private MessagingRetrofitClient(){

        httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        okHttpClient = new OkHttpClient
                .Builder()
                .addInterceptor(httpLoggingInterceptor)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
    }

    public static synchronized MessagingRetrofitClient getInstance(){

        if(messagingRetrofitClient == null){
            messagingRetrofitClient = new MessagingRetrofitClient();
        }

        return messagingRetrofitClient;
    }

    public MessagingAPI getMessagingAPI(){
        return retrofit.create(MessagingAPI.class);
    }
}
