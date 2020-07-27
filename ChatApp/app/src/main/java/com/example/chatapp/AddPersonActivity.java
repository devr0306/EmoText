package com.example.chatapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.chatapp.Adapters.AddPersonRecyclerViewAdapter;
import com.example.chatapp.Models.API.UserListResponse;
import com.example.chatapp.Models.app.User;
import com.example.chatapp.RetrofitClients.ContactsAPIClient;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddPersonActivity extends AppCompatActivity {

    private RecyclerView addList;
    private AddPersonRecyclerViewAdapter addPersonRecyclerViewAdapter;

    private ArrayList<User> userList;

    private EditText addEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_person);

        init();
    }

    public void init(){

        findViewById(R.id.layout_for_friend_request_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddPersonActivity.this, RequestsActivity.class));
            }
        });

        findViewById(R.id.back_arrow_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddPersonActivity.this, MainActivity.class));
            }
        });

        userList = new ArrayList<>();
        addList = findViewById(R.id.add_people_recycler_view);

        addPersonRecyclerViewAdapter = new AddPersonRecyclerViewAdapter(AddPersonActivity.this);
        addPersonRecyclerViewAdapter.setcontactsList(userList);

        addList.setAdapter(addPersonRecyclerViewAdapter);
        addList.setLayoutManager(new LinearLayoutManager(AddPersonActivity.this));

        addEditText = findViewById(R.id.edit_text_for_person_add);

        addEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if(keyCode == KeyEvent.KEYCODE_ENTER)
                    setEditTextAndList();

                return true;
            }
        });
        setEditTextAndList();

    }

    public void setEditTextAndList(){

        Call<UserListResponse> findContactsCall = ContactsAPIClient
                .getInstance()
                .getContactsAPI()
                .findContacts(addEditText.getText().toString());

        findContactsCall.enqueue(new Callback<UserListResponse>() {
            @Override
            public void onResponse(Call<UserListResponse> call, Response<UserListResponse> response) {

                if(response.isSuccessful()){

                    UserListResponse ulr = response.body();
                    userList = ulr.convertToList();
                    Toast.makeText(AddPersonActivity.this, userList.toString(), Toast.LENGTH_SHORT).show();
                    addPersonRecyclerViewAdapter.setcontactsList(userList);
                }
                else{
                    try {
                        Toast.makeText(AddPersonActivity.this, response.errorBody().string(), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<UserListResponse> call, Throwable t) {
                Toast.makeText(AddPersonActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
}