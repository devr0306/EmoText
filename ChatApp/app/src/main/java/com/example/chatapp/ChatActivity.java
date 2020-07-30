package com.example.chatapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Guideline;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.chatapp.Models.app.Message;
import com.example.chatapp.Adapters.UserMessagesRecyclerViewAdapter;
import com.example.chatapp.Models.app.SwipeListener;
import com.example.chatapp.Models.app.SwipeListenerInterface;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener, SwipeListenerInterface {

    private EditText messagePanel;
    private Guideline topGuideline, textGuideline, messageGuideline;
    private ImageView sendButton;
    private CircleImageView imageOfPerson;
    private TextView nameOfPerson;

    private RecyclerView messagesRecyclerView;
    private UserMessagesRecyclerViewAdapter messageRecViewAdapter;
    ArrayList<Message> messageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        getThisView().setOnTouchListener(new SwipeListener(this));
        init();


    }

    public void init(){

            messagePanel = findViewById(R.id.textPanel);
            /*topGuideline = findViewById(R.id.top_panel_guideline);
            textGuideline = findViewById(R.id.typing_panel_guideline);
            messageGuideline = findViewById(R.id.message_guideline);*/
            sendButton = findViewById(R.id.sendButton);
            imageOfPerson = findViewById(R.id.person_image);
            nameOfPerson = findViewById(R.id.nameOfPerson);
            messagesRecyclerView = findViewById(R.id.user_messages);

            messageRecViewAdapter = new UserMessagesRecyclerViewAdapter(this);
            messageList = new ArrayList<>();


            setMessagesRecyclerView();
            getIntentInfo();
    }

    public void setMessagesRecyclerView(){

        messageRecViewAdapter.setMessagesList(messageList);

        messagesRecyclerView.setAdapter(messageRecViewAdapter);
        messagesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        messagesRecyclerView.scrollBy(0,5);
        messagesRecyclerView.setOnTouchListener(new SwipeListener(this));
    }

    public void getIntentInfo(){

        Intent getDetails = getIntent();
        if(getDetails != null) {

            String personImage = getDetails.getStringExtra("imageOfPerson");

            if(personImage == null || personImage.length() < 1)
                imageOfPerson.setImageResource(R.drawable.ic_launcher_background);

            else {
                Glide.with(this)
                        .asBitmap()
                        .load(personImage)
                        .into(imageOfPerson);
            }



            String personName = getDetails.getStringExtra("nameOfPerson");

            if (personName != null)
                nameOfPerson.setText(personName);
        }
    }

    @Override
    public void onClick(View v) {

        if(v.getId() == messagePanel.getId()){
        }

        if(v.getId() == R.id.sendButton) {

            String text = messagePanel.getText().toString();

            if(!text.equals("")) {

                messageList.add(new Message(text));
                messagePanel.setText("");
                messageRecViewAdapter.setMessagesList(messageList);
            }

        }
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

    public View getThisView(){
        return getWindow().getDecorView().findViewById(android.R.id.content);
    }

    @Override
    public void onRightToLeftSwipe(View v) {

    }

    @Override
    public void onLeftToRightSwipe(View v) {

        if(!messagesRecyclerView.canScrollHorizontally(-1))
            animateOut();
    }

    @Override
    public void onTopToBottomSwipe(View v) {

    }

    @Override
    public void onBottomToTopSwipe(View v) {

    }
}