package com.example.chatapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Guideline;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.chatapp.Models.app.Message;
import com.example.chatapp.Adapters.UserMessagesRecyclerViewAdapter;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText messagePanel;
    private Guideline topGuideline, textGuideline, messageGuideline;
    private ImageView sendButton, imageOfPerson;
    private TextView nameOfPerson;

    private RecyclerView messagesRecyclerView;
    private UserMessagesRecyclerViewAdapter messageRecViewAdapter;
    ArrayList<Message> messageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

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
    }

    public void getIntentInfo(){

        Intent getDetails = getIntent();
        if(getDetails != null) {

            String personImage = getDetails.getStringExtra("imageOfPerson");

            if (personImage != null) {
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
}