package com.example.chatapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.chatapp.Adapters.PeopleTabRecyclerViewAdapter;
import com.example.chatapp.Adapters.SendPictureRecyclerViewAdapter;
import com.example.chatapp.Fragments.CameraTabFragment;
import com.example.chatapp.Models.app.User;
import com.example.chatapp.ResponseObjects.ContactListResponse;
import com.example.chatapp.RetrofitClients.ContactsAPIClient;
import com.r0adkll.slidr.Slidr;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SendPictureActivity extends AppCompatActivity {

    private ArrayList<User> friends;
    private EditText searchFriends;

    private RecyclerView friendsRecyclerView;
    private SendPictureRecyclerViewAdapter sendPictureRecyclerViewAdapter;

    private Button sendButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_picture);

        Slidr.attach(this);

        init();
    }

    private void init(){

        friends = new ArrayList<>();

        searchFriends = findViewById(R.id.search_friends_send_activity);
        sendButton = findViewById(R.id.send_button_send_activity);

        friendsRecyclerView = findViewById(R.id.send_people_list);


        findViewById(R.id.back_arrow_button_send_activity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CameraTabFragment.isPicture = true;
                animateOut();
            }
        });

        sendPictureRecyclerViewAdapter = new SendPictureRecyclerViewAdapter(SendPictureActivity.this);
        sendPictureRecyclerViewAdapter.setFriendsList(friends);

        friendsRecyclerView.setAdapter(sendPictureRecyclerViewAdapter);
        friendsRecyclerView.setLayoutManager(new LinearLayoutManager(SendPictureActivity.this));

        setFriendsList();
    }

    private void setFriendsList(){

        Call<ContactListResponse> getFriendsCall = ContactsAPIClient
                .getInstance()
                .getContactsAPI()
                .getContacts(SharedPrefManager.getInstance(SendPictureActivity.this).getUser().getToken());

        getFriendsCall.enqueue(new Callback<ContactListResponse>() {
            @Override
            public void onResponse(Call<ContactListResponse> call, Response<ContactListResponse> response) {

                if(response.isSuccessful()){
                    ContactListResponse clr = response.body();

                    if (clr.getUsers() != null && clr.getUsers().length > 0) {

                        friends = clr.getUserAsList();
                        sendPictureRecyclerViewAdapter.setFriendsList(friends);
                    }

                }

                else{
                    try {
                        String errorBody = response.errorBody().string();
                        int index = errorBody.indexOf("\"message\":");

                        if(index != -1){

                            String errorSub = errorBody.substring(index + 10);
                            errorBody = errorSub.substring(1, errorSub.length() - 2);
                        }

                        Toast.makeText(SendPictureActivity.this, errorBody, Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ContactListResponse> call, Throwable t) {

            }
        });
    }

    public View getThisView(){
        return getWindow().getDecorView().findViewById(android.R.id.content);
    }

    private void animateOut() {
        Animation slideAnim = AnimationUtils.loadAnimation(this,R.anim.slide_out_right);
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

    @Override
    public void onBackPressed() {
        CameraTabFragment.isPicture = true;

    }
}