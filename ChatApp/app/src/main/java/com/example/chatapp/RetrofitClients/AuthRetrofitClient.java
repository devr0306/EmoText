package com.example.chatapp.RetrofitClients;

import com.example.chatapp.APIs.AuthAPI;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AuthRetrofitClient {

    private static final String BASE_URL = "http://192.168.1.65:5000/api/auth/";
    private static AuthRetrofitClient authRetrofitClient;
    private Retrofit retrofit;

    private HttpLoggingInterceptor httpLoggingInterceptor;
    private OkHttpClient okHttpClient;

    private AuthRetrofitClient(){

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

    public static synchronized AuthRetrofitClient getInstance(){

        if(authRetrofitClient == null){
            authRetrofitClient = new AuthRetrofitClient();
        }

        return authRetrofitClient;
    }

    public AuthAPI getAuthApi(){
        return retrofit.create(AuthAPI.class);
    }
}
