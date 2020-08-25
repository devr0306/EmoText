package com.example.chatapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Guideline;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.chatapp.APIs.MessagingAPI;
import com.example.chatapp.Models.API.Chat;
import com.example.chatapp.Models.app.ConvertFieldsToUserObjects;
import com.example.chatapp.Models.API.Message;
import com.example.chatapp.Adapters.UserMessagesRecyclerViewAdapter;
import com.example.chatapp.Models.app.SwipeListener;
import com.example.chatapp.Models.app.SwipeListenerInterface;
import com.example.chatapp.Models.app.User;
import com.example.chatapp.ResponseObjects.ChatListResponse;
import com.example.chatapp.ResponseObjects.ChatResponse;
import com.example.chatapp.ResponseObjects.DefaultResponse;
import com.example.chatapp.ResponseObjects.MessageListResponse;
import com.example.chatapp.ResponseObjects.MessageResponse;
import com.example.chatapp.RetrofitClients.ChatRetrofitClient;
import com.example.chatapp.RetrofitClients.MessagingRetrofitClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener, SwipeListenerInterface {

    private EditText messagePanel;
    private Guideline topGuideline, textGuideline, messageGuideline;
    private ImageView sendButton;
    private CircleImageView imageOfPerson;
    private TextView nameOfPerson;

    private RecyclerView messagesRecyclerView;
    private UserMessagesRecyclerViewAdapter messageRecViewAdapter;
    ArrayList<Message> messageList;

    private User person;
    private Chat chat;

    private int messagePart;

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

            messagePart = 0;

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

    public void getIntentInfo() {

        Intent getDetails = getIntent();
        if (getDetails != null) {


            person = (User) (getDetails.getSerializableExtra("userObject"));
            checkIfPersonIsChat("");

            if (person == null)
                imageOfPerson.setImageResource(R.drawable.ic_launcher_background);

            else {

                nameOfPerson.setText(person.getName());

                if(person.getProfilePictureURL() == null || person.getProfilePictureURL().length() < 1)
                    imageOfPerson.setImageResource(R.drawable.ic_launcher_background);


                else{
                    Glide.with(this)
                            .asBitmap()
                            .load(person.getProfilePictureURL())
                            .into(imageOfPerson);
                }
            }
        }
    }

    public void checkIfPersonIsChat(String text){

        Log.i("Testing", text);

        if(person != null){

            Call<ChatListResponse> getAllContacts = ChatRetrofitClient
                    .getInstance()
                    .getChatAPI()
                    .loadChats(SharedPrefManager.getInstance(ChatActivity.this).getUser().getToken());

            getAllContacts.enqueue(new Callback<ChatListResponse>() {
                @Override
                public void onResponse(Call<ChatListResponse> call, Response<ChatListResponse> response) {

                    if(response.isSuccessful()){

                        ChatListResponse clr = response.body();
                        Log.i("Testing", Arrays.toString(clr.getChats()));

                        if(clr.getChats() == null || clr.getChats().length < 1)
                            createChat(text);

                        else{
                            for(Chat c: clr.getChats()){
                                if(c.getUserID().equals(person.getId())){

                                    chat = c;

                                    if(text.equals(""))
                                        loadMessageList(chat);

                                    if(!text.equals(""))
                                        sendMessage(text);
                                }
                            }

                            if(chat == null){
                                createChat(text);
                            }
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

                            Toast.makeText(ChatActivity.this, errorBody, Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ChatListResponse> call, Throwable t) {
                }
            });
        }
    }

    public void createChat(String text){

        Call<ChatResponse> createNewChatCall = ChatRetrofitClient
                .getInstance()
                .getChatAPI()
                .createChat(person.getId(), SharedPrefManager.getInstance(ChatActivity.this).getUser().getToken());

        createNewChatCall.enqueue(new Callback<ChatResponse>() {
            @Override
            public void onResponse(Call<ChatResponse> call, Response<ChatResponse> response) {

                if(response.isSuccessful()){

                    ChatResponse cr = response.body();
                    chat = cr.getChat();

                    if(text.equals(""))
                        loadMessageList(chat);

                    if(!text.equals(""))
                        sendMessage(text);
                }
            }

            @Override
            public void onFailure(Call<ChatResponse> call, Throwable t) {

            }
        });
    }

    @Override
    public void onClick(View v) {

        if(v.getId() == messagePanel.getId()){
        }

        if(v.getId() == R.id.sendButton) {

            String text = messagePanel.getText().toString();

            if(!text.equals("")) {

                if(chat != null)
                    sendMessage(text);

                else
                    checkIfPersonIsChat(text);
            }
        }
    }

    public void sendMessage(String text){

        Call<MessageResponse> sendMessageCall = MessagingRetrofitClient
                .getInstance()
                .getMessagingAPI()
                .sendMessage(text, SharedPrefManager.getInstance(ChatActivity.this).getUser().getToken(), chat.getId(), chat.isGroupChat());


        sendMessageCall.enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {

                if(response.isSuccessful()){

                    MessageResponse mr = response.body();

                    messagePanel.setText("");
                    messageList.add(mr.getMsg());
                    messageRecViewAdapter.setMessagesList(messageList);
                }

                else{
                    try {
                        String errorBody = response.errorBody().string();
                        int index = errorBody.indexOf("\"message\":");

                        if(index != -1){

                            String errorSub = errorBody.substring(index + 10);
                            errorBody = errorSub.substring(1, errorSub.length() - 2);
                        }

                        Toast.makeText(ChatActivity.this, errorBody, Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<MessageResponse> call, Throwable t) {
                Toast.makeText(ChatActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
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

    public void loadMessageList(Chat ch){

        Call<MessageListResponse> loadMessagesCall = ChatRetrofitClient
                .getInstance()
                .getChatAPI()
                .loadMessages(messagePart, chat.getId(), SharedPrefManager.getInstance(ChatActivity.this).getUser().getToken());

        loadMessagesCall.enqueue(new Callback<MessageListResponse>() {
            @Override
            public void onResponse(Call<MessageListResponse> call, Response<MessageListResponse> response) {

                if(response.isSuccessful()){

                    MessageListResponse ml = response.body();
                    ArrayList<Message> messagesTempList = ml.getMessagesArrayList();
                    Log.i("Testing", messagesTempList.toString());

                    messagesTempList.addAll(messageList);
                    messageList = messagesTempList;

                    Log.i("Testing", messageList.toString());

                    messageRecViewAdapter.setMessagesList(messageList);
                }

                else{
                    try {
                        String errorBody = response.errorBody().string();
                        int index = errorBody.indexOf("\"message\":");

                        if(index != -1){

                            String errorSub = errorBody.substring(index + 10);
                            errorBody = errorSub.substring(1, errorSub.length() - 2);
                        }

                        Toast.makeText(ChatActivity.this, errorBody, Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<MessageListResponse> call, Throwable t) {
                Log.i("Failure", t.toString());
            }
        });
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

    @Override
    public void onBackPressed() {
        animateOut();
    }
}