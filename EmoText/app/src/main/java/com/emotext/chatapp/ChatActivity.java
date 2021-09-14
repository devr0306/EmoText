package com.emotext.chatapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.emotext.chatapp.Models.API.Chat;
import com.emotext.chatapp.Models.API.Message;
import com.emotext.chatapp.Adapters.UserMessagesRecyclerViewAdapter;
import com.emotext.chatapp.Models.app.ChatSocket;
import com.emotext.chatapp.Models.app.SendChat;
import com.emotext.chatapp.Models.app.SwipeListener;
import com.emotext.chatapp.Models.app.SwipeListenerInterface;
import com.emotext.chatapp.Models.app.User;
import com.emotext.chatapp.ResponseObjects.ChatListResponse;
import com.emotext.chatapp.ResponseObjects.ChatResponse;
import com.emotext.chatapp.ResponseObjects.MessageListResponse;
import com.emotext.chatapp.ResponseObjects.MessageResponse;
import com.emotext.chatapp.ResponseObjects.SentimentResponse;
import com.emotext.chatapp.RetrofitClients.ChatRetrofitClient;
import com.emotext.chatapp.RetrofitClients.MachineLearningRetrofitClient;
import com.emotext.chatapp.RetrofitClients.MessagingRetrofitClient;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;
import com.r0adkll.slidr.Slidr;

import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener, SwipeListenerInterface, SendChat {

    //The EditText which contains the text for texting
    private EditText messagePanel;

    //Name of person and their image on the top panel
    private CircleImageView imageOfPerson;
    private TextView nameOfPerson;

    private View sentimentView;

    //RecyclerView, RecyclerViewAdapter, and the List which contains Message objects which will be in the RecyclerView
    private RecyclerView messagesRecyclerView;
    private UserMessagesRecyclerViewAdapter messageRecViewAdapter;
    ArrayList<Message> messageList;

    //The other User and the Chat object associated with that User(Both are necessary for the API's)
    private User person;
    private Chat chat;

    //The int which tells the API's which part of the messages to display(0-25, 26-50)
    private int messagePart;

    //Class which can be used to get the current date and time. Useful for time-stamping for sent messages
    private Calendar calendar;

    //The Socket object used for real-time message sending and receiving
    private Socket mSocket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Default
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        //Slidr class; used for swiping between Activities.
        Slidr.attach(this);

        //Initializing the Socket and connecting it
        ChatSocket chatSocket = new ChatSocket();
        mSocket = chatSocket.getSocket();
        mSocket.connect();

        //TODO- Check if this is needed
        getThisView().setOnTouchListener(new SwipeListener(this));

        //Set up the Activity and configure all the variables
        init();
    }


    //Initialize all the variables and call some methods
    public void init(){

        messagePanel = findViewById(R.id.textPanel);
        imageOfPerson = findViewById(R.id.person_image);
        nameOfPerson = findViewById(R.id.nameOfPerson);

        sentimentView = findViewById(R.id.sentiment_view);
        sentimentView.setVisibility(View.GONE);

        messagesRecyclerView = findViewById(R.id.user_messages);

        messageRecViewAdapter = new UserMessagesRecyclerViewAdapter(this);
        messageList = new ArrayList<>();

        messagePart = 1;

        findViewById(R.id.back_arrow_chat_activity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateOut();
            }
        });

        calendar = Calendar.getInstance();

        //chatGestureDetector = new GestureDetector(ChatActivity.this, gestureListener);

        setMessagesRecyclerView();
        getIntentInfo();
    }


    //Set up the RecyclerViewAdapter and related fields
    public void setMessagesRecyclerView(){

        messageRecViewAdapter.setMessagesList(messageList);
        messagesRecyclerView.setAdapter(messageRecViewAdapter);

        LinearLayoutManager recyclerViewLinearLayoutManager = new LinearLayoutManager(this);

        //Shows items from bottom to top. Useful for displaying messages.
        recyclerViewLinearLayoutManager.setStackFromEnd(true);

        messagesRecyclerView.setLayoutManager(recyclerViewLinearLayoutManager);

        //Makes RecyclerView scroll to bottom when EditText is clicked
        messagesRecyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {

                if(messageList.size() > 0)
                    messagesRecyclerView.smoothScrollToPosition(messageList.size() - 1);

            }
        });
    }


    //Method to get the User from the MainActivity intent
    public void getIntentInfo() {

        Intent getDetails = getIntent();
        if (getDetails != null) {

            //User object from Intent
            person = (User) (getDetails.getSerializableExtra("userObject"));
            checkIfPersonIsChat("");

            //If object is null, set image resource to default, else set name to the name of the User.
            if (person == null)
                imageOfPerson.setImageResource(R.drawable.ic_launcher_background);

            else {

                nameOfPerson.setText(person.getName());

                //Again check if the image url is null or empty and set resource to default, else load the image into the view
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


    //OnClickListeners
    @Override
    public void onClick(View v) {

        //Send button. If the EditText is not empty and if chat is not null, call method to send message, else create a chat first.
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


    //Supposed to be the join room API for the Socket TODO- Configure the Socket
    private Emitter.Listener joinRoom = new Emitter.Listener() {
        @Override
        public void call(Object... args) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(ChatActivity.this, "Room joined", Toast.LENGTH_SHORT).show();
                }
            });
        }
    };


    //Read in all the Chats in the User's Contacts list and find out which chat belongs to the User object obtained from the intent.
    public void checkIfPersonIsChat(String text){

        if(person != null){

            //API call
            Call<ChatListResponse> getAllContacts = ChatRetrofitClient
                    .getInstance()
                    .getChatAPI()
                    .loadChats(SharedPrefManager.getInstance(ChatActivity.this).getUser().getToken());

            //Carry out the API call.
            getAllContacts.enqueue(new Callback<ChatListResponse>() {
                @Override
                public void onResponse(Call<ChatListResponse> call, Response<ChatListResponse> response) {

                    if(response.isSuccessful()){

                        //List of Chats
                        ChatListResponse clr = response.body();

                        if(clr.getChats() == null || clr.getChats().length < 1)
                            createChat(text);

                        //Look for the Chat which contains the User ID of person
                        else{
                            for(Chat c: clr.getChats()){
                                if(c.getUserID().equals(person.getId())){

                                    chat = c;
                                    Emitter e = mSocket.emit("open-chat", chat.isGroupChat(), chat.getId());
                                    Log.i("Testing Socket", e.toString());

                                    //If method is called in the beginning of the Activity, load the messages
                                    if(text.equals(""))
                                        loadMessageList(chat);

                                    //Send the text passed from the EditText.
                                    if(!text.equals(""))
                                        sendMessage(text);
                                }
                            }

                            //Create a new Chat if no Chat correlates to person
                            if(chat == null){
                                createChat(text);
                            }
                        }
                    }

                    //If API call doesn't give desired results, create a message to the User
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

    //Create a new Chat and either load the messages or send the message
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

    //Send the message and time-stamp it
    public void sendMessage(String text){

        Call<MessageResponse> sendMessageCall = MessagingRetrofitClient
                .getInstance()
                .getMessagingAPI()
                .sendMessage(text, SharedPrefManager.getInstance(ChatActivity.this).getUser().getToken(), chat.getId(), chat.isGroupChat());

        sendMessageCall.enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {

                if(response.isSuccessful()){

                    //Message obtainted from the API call
                    MessageResponse mr = response.body();
                    Message m = mr.getMsg();

                    //Get the current hour and find out AM/PM from that
                    int hour = calendar.get(Calendar.HOUR_OF_DAY);
                    String am_pm = hour/12 == 0 ? "AM" : "PM";

                    //Convert the hour from 24-base to 12-base
                    hour = hour % 12;
                    int minute = calendar.get(Calendar.MINUTE);

                    //Combine the hour, minute, and am/pm to get a time String which will be used to display the time the message was sent.
                    String time = String.format("%d:%02d%s", hour, minute, am_pm);

                    //Find out an accurate date and time which can be used for comparisons
                    Date date = calendar.getTime();

                    //Format the date to a String which can be displayed to the user
                    SimpleDateFormat dateToString = new SimpleDateFormat("MMM, dd yyyy");
                    String dateString = dateToString.format(date);

                    //TODO- Check if converting the date to a simpler format is necessary
                    SimpleDateFormat stringToDate = new SimpleDateFormat("MMM, dd yyyy");

                    try {

                        //TODO- Check if necessary
                        date = stringToDate.parse(dateString);

                        //Year will only be displayed in the date when the year is less than the current one. So it is split
                        String year = dateString.substring(dateString.length() - 4);
                        dateString = dateString.substring(0, dateString.length() - 5);

                        //Set date, dateString, year, and time to the message object
                        m.setDate(date);
                        m.setDateString(dateString);
                        m.setYear(year);
                        m.setTime(time);

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    //Empty the EditText and add the message to the Adapter's list
                    messagePanel.setText("");
                    messageRecViewAdapter.addToMessagesList(m);
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
                Toast.makeText(ChatActivity.this, "Something went wrong. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }


    //Load all the messages from the API and time-stamp each of them
    public void loadMessageList(Chat ch){

        Call<MessageListResponse> loadMessagesCall = ChatRetrofitClient
                .getInstance()
                .getChatAPI()
                .loadMessages(messagePart, chat.getId(), SharedPrefManager.getInstance(ChatActivity.this).getUser().getToken());

        loadMessagesCall.enqueue(new Callback<MessageListResponse>() {
            @Override
            public void onResponse(Call<MessageListResponse> call, Response<MessageListResponse> response) {

                if(response.isSuccessful()){

                    //List of messages
                    MessageListResponse ml = response.body();
                    ArrayList<Message> messagesTempList = ml.getMessagesArrayList();

                    //For each message, time-stamp it and add it to the list
                    for(int i = 0; i < messagesTempList.size(); i++){

                        //The date from the Message object consists of Date and Time, split using letters
                        String[] createdAt = messagesTempList.get(i).getCreatedAt().split("[A-Z]");

                        SimpleDateFormat toDate = new SimpleDateFormat("yyyy-MM-dd");

                        try {

                            //Format the date and convert it into a user-friendly format
                            Date date = toDate.parse(createdAt[0]);
                            SimpleDateFormat toString = new SimpleDateFormat("MMM, dd yyyy");
                            String dateString = toString.format(date);

                            //Split dateString into year and dateString for better display
                            String year = dateString.substring(dateString.length() - 4);
                            dateString = dateString.substring(0, dateString.length() - 5);

                            String[] times = createdAt[1].split(":");

                            int hour = Integer.parseInt(times[0]);
                            int minute = Integer.parseInt(times[1]);
                            String am_pm = hour/12 == 0 ? "AM" : "PM";

                            hour = hour % 12;

                            String time = String.format("%d:%02d %s", hour, minute, am_pm);

                            messagesTempList.get(i).setYear(year);
                            messagesTempList.get(i).setDate(date);
                            messagesTempList.get(i).setDateString(dateString);
                            messagesTempList.get(i).setTime(time);

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }

                    determineSentiment(messagesTempList);

                    messageList = messagesTempList;
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

    private void determineSentiment(ArrayList<Message> messagesList){

        String texts = "";

        for(int i = 0; i < Math.min(25, messagesList.size()); i++)
            texts += messagesList.get(messagesList.size() - 1 - i).getText() + "||";

        if (texts.length() > 2) {

            texts = texts.substring(0, texts.length() - 2);
            Log.i("Testing ML", texts);

            Map<String, String> map = new ArrayMap<>();
            map.put("texts", texts);

            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), (new JSONObject(map)).toString());

            Call<SentimentResponse> sentimentResponseCall = MachineLearningRetrofitClient
                    .getInstance()
                    .getAPI()
                    .predict(body);

            sentimentResponseCall.enqueue(new Callback<SentimentResponse>() {
                @Override
                public void onResponse(Call<SentimentResponse> call, Response<SentimentResponse> response) {

                    if (response.isSuccessful()) {

                        SentimentResponse sr = response.body();

                        //TODO- Change this
                        if (sr.getMessage().equals("negative"))
                            sentimentView.setBackgroundResource(R.drawable.person_emotion_positive);

                        else if (sr.getMessage().equals("positive"))
                            sentimentView.setBackgroundResource(R.drawable.person_emotion_negative);

                        sentimentView.setVisibility(View.VISIBLE);
                    } else {
                        try {
                            String errorBody = response.errorBody().string();
                            int index = errorBody.indexOf("\"message\":");

                            if (index != -1) {

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
                public void onFailure(Call<SentimentResponse> call, Throwable throwable) {
                    Log.i("Testing ML Failure", throwable.toString());
                }
            });
        }
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

    public void onRightToLeftSwipe(View v) {

    }

    @Override
    public void onLeftToRightSwipe(View v) {

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