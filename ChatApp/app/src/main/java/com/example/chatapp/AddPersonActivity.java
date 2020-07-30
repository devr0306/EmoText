package com.example.chatapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.example.chatapp.Adapters.AddPersonRecyclerViewAdapter;
import com.example.chatapp.Models.app.SwipeListener;
import com.example.chatapp.Models.app.SwipeListenerInterface;
import com.example.chatapp.ResponseObjects.UserListResponse;
import com.example.chatapp.Models.app.User;
import com.example.chatapp.RetrofitClients.ContactsAPIClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddPersonActivity extends AppCompatActivity implements SwipeListenerInterface {

    private RecyclerView addList;
    private AddPersonRecyclerViewAdapter addPersonRecyclerViewAdapter;

    private ArrayList<User> userList;

    private EditText addEditText;

    private int mainPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_person);

        getThisView().setOnTouchListener(new SwipeListener(this));
        init();
    }

    public void init(){

        Intent fromMain = getIntent();

        if(fromMain != null){

            int position = fromMain.getIntExtra("position", -1);

            if(position != -1)
                mainPosition = position;
        }

        findViewById(R.id.layout_for_friend_request_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddPersonActivity.this, RequestsActivity.class));
            }
        });

        findViewById(R.id.back_arrow_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateOut();
            }
        });

        userList = new ArrayList<>();
        addList = findViewById(R.id.add_people_recycler_view);

        addPersonRecyclerViewAdapter = new AddPersonRecyclerViewAdapter(AddPersonActivity.this);
        addPersonRecyclerViewAdapter.setcontactsList(userList);

        addList.setAdapter(addPersonRecyclerViewAdapter);
        addList.setLayoutManager(new LinearLayoutManager(AddPersonActivity.this));
        addList.setOnTouchListener(new SwipeListener(this));

        addEditText = findViewById(R.id.edit_text_for_person_add);

        addEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if(keyCode == KeyEvent.KEYCODE_ENTER)
                    setEditTextAndList();

                return true;
            }
        });

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

                    if(ulr.getContacts() != null){

                        userList = ulr.convertToList();

                        Toast.makeText(AddPersonActivity.this, userList.toString(), Toast.LENGTH_SHORT).show();
                        addPersonRecyclerViewAdapter.setcontactsList(userList);
                    }

                    else
                        Toast.makeText(AddPersonActivity.this, Arrays.toString(ulr.getContacts()), Toast.LENGTH_SHORT).show();

                }
                else{
                    try {
                        String errorBody = response.errorBody().string();
                        int index = errorBody.indexOf("\"message\":");

                        if(index != -1){

                            String errorSub = errorBody.substring(index + 10);
                            errorBody = errorSub.substring(1, errorSub.length() - 2);
                        }

                        Toast.makeText(AddPersonActivity.this, errorBody, Toast.LENGTH_SHORT).show();
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

    private void animateOut() {
        Animation slideAnim = AnimationUtils.loadAnimation(this,R.anim.slide_out_down);
        slideAnim.setFillAfter(true);
        slideAnim.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationStart(Animation paramAnimation) { }
            public void onAnimationRepeat(Animation paramAnimation) { }
            public void onAnimationEnd(Animation paramAnimation) {
                getThisView().clearAnimation();
                finish();

                overridePendingTransition(0, 0);
            }
        });
        getThisView().startAnimation(slideAnim);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                /*Intent backToMain = new Intent(AddPersonActivity.this, MainActivity.class);
                backToMain.putExtra("position", mainPosition);
                startActivity(backToMain);*/
            }
        }, 500);
    }

    public View getThisView(){
        return getWindow().getDecorView().findViewById(android.R.id.content);
    }

    @Override
    public void onBackPressed() {
        animateOut();
        return;
    }

    @Override
    public void onRightToLeftSwipe(View v) {

    }

    @Override
    public void onLeftToRightSwipe(View v) {

    }

    @Override
    public void onTopToBottomSwipe(View v) {

        if(!addList.canScrollVertically(-1))
            animateOut();
    }

    @Override
    public void onBottomToTopSwipe(View v) {

    }
}