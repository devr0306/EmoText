package com.emotext.chatapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.emotext.chatapp.Adapters.RequestsRecyclerViewAdapter;
import com.emotext.chatapp.Models.API.FriendRequest;
import com.emotext.chatapp.ResponseObjects.FriendRequestsResponse;
import com.emotext.chatapp.RetrofitClients.ContactsAPIClient;
import com.r0adkll.slidr.Slidr;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequestsActivity extends AppCompatActivity {

    private RecyclerView requestsRecyclerView;
    private ArrayList<FriendRequest> friendRequests;

    private TextView noRequests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests);

        Slidr.attach(this);

        init();
    }

    public void init(){

        requestsRecyclerView = findViewById(R.id.requests_recycler_view);
        noRequests = findViewById(R.id.no_requests_text_view);

        noRequests.setVisibility(View.GONE);

        initializeLists();

        findViewById(R.id.back_arrow_requests_activity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateOut();
            }
        });
    }

    public void initializeLists(){

        friendRequests = new ArrayList<>();

        Call<FriendRequestsResponse> getFriendRequestsCall = ContactsAPIClient
                .getInstance()
                .getContactsAPI()
                .getFriendRequests(SharedPrefManager.getInstance(RequestsActivity.this).getUser().getToken());

        getFriendRequestsCall.enqueue(new Callback<FriendRequestsResponse>() {
            @Override
            public void onResponse(Call<FriendRequestsResponse> call, Response<FriendRequestsResponse> response) {

                if(response.isSuccessful()){

                    FriendRequestsResponse frr = response.body();

                    if(frr.getRequests() != null && frr.getRequests().length != 0){

                        noRequests.setVisibility(View.GONE);
                        friendRequests = frr.returnFriendsList();

                        RequestsRecyclerViewAdapter requestsRecyclerViewAdapter = new RequestsRecyclerViewAdapter(RequestsActivity.this);
                        requestsRecyclerViewAdapter.setLists(friendRequests);

                        //TODO- Check why multiple friend requests send in the AddPersonRecyclerView class and remove this part
                        Set<String> set = new HashSet<>();

                        for(int i = friendRequests.size() - 1; i >= 0; i--){

                            if(!set.add(friendRequests.get(i).getUserFrom()))
                                friendRequests.remove(i);
                        }

                        requestsRecyclerView.setAdapter(requestsRecyclerViewAdapter);
                        requestsRecyclerView.setLayoutManager(new LinearLayoutManager(RequestsActivity.this));
                    }

                    else
                        noRequests.setVisibility(View.VISIBLE);

                }

                else{
                    try {
                        String errorBody = response.errorBody().string();
                        int index = errorBody.indexOf("\"message\":");

                        if(index != -1){

                            String errorSub = errorBody.substring(index + 10);
                            errorBody = errorSub.substring(1, errorSub.length() - 2);
                        }

                        Toast.makeText(RequestsActivity.this, errorBody, Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<FriendRequestsResponse> call, Throwable t) {

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
}