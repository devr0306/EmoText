package com.example.chatapp.RetrofitClients;

import com.example.chatapp.APIs.AuthAPI;
import com.example.chatapp.APIs.ChatAPI;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChatRetrofitClient {

    private static final String BASE_URL = "http://134.209.28.184:5000/api/chat/";
    private static ChatRetrofitClient chatRetrofitClient;
    private Retrofit retrofit;

    private HttpLoggingInterceptor httpLoggingInterceptor;
    private OkHttpClient okHttpClient;

    private ChatRetrofitClient(){

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

    public static synchronized ChatRetrofitClient getInstance(){

        if(chatRetrofitClient == null){
            chatRetrofitClient = new ChatRetrofitClient();
        }

        return chatRetrofitClient;
    }

    public ChatAPI getChatAPI(){
        return retrofit.create(ChatAPI.class);
    }
}
