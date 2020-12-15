package com.example.chatapp.RetrofitClients;

import com.example.chatapp.APIs.AuthAPI;
import com.example.chatapp.APIs.ContactsAPI;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ContactsAPIClient {

    private static final String BASE_URL = "http://134.209.28.184:5000/api/contacts/";
    private static ContactsAPIClient contactsAPIClient;
    private Retrofit retrofit;

    private HttpLoggingInterceptor httpLoggingInterceptor;
    private OkHttpClient okHttpClient;

    private ContactsAPIClient(){

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

    public static synchronized ContactsAPIClient getInstance(){

        if(contactsAPIClient == null){
            contactsAPIClient = new ContactsAPIClient();
        }

        return contactsAPIClient;
    }

    public ContactsAPI getContactsAPI(){
        return retrofit.create(ContactsAPI.class);
    }
}
