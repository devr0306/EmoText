package com.example.chatapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Guideline;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.chatapp.Models.API.Chat;
import com.example.chatapp.Models.API.Message;
import com.example.chatapp.Adapters.UserMessagesRecyclerViewAdapter;
import com.example.chatapp.Models.app.ChatSocket;
import com.example.chatapp.Models.app.SendChat;
import com.example.chatapp.Models.app.SwipeListener;
import com.example.chatapp.Models.app.SwipeListenerInterface;
import com.example.chatapp.Models.app.User;
import com.example.chatapp.ResponseObjects.ChatListResponse;
import com.example.chatapp.ResponseObjects.ChatResponse;
import com.example.chatapp.ResponseObjects.MessageListResponse;
import com.example.chatapp.ResponseObjects.MessageResponse;
import com.example.chatapp.RetrofitClients.ChatRetrofitClient;
import com.example.chatapp.RetrofitClients.MessagingRetrofitClient;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.r0adkll.slidr.Slidr;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener, SwipeListenerInterface, SendChat {

    private EditText messagePanel;
    private CircleImageView imageOfPerson;
    private TextView nameOfPerson;

    //private GestureDetector chatGestureDetector;

    private RecyclerView messagesRecyclerView;
    private UserMessagesRecyclerViewAdapter messageRecViewAdapter;
    ArrayList<Message> messageList;

    private User person;
    private Chat chat;

    private int messagePart;

    private Calendar calendar;

    private Socket mSocket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Slidr.attach(this);

        ChatSocket chatSocket = new ChatSocket();
        mSocket = chatSocket.getSocket();

        mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
        mSocket.on(Socket.EVENT_CONNECT, onConnect);
        mSocket.on(Socket.EVENT_DISCONNECT, onDisconnect);
        mSocket.connect();

        getThisView().setOnTouchListener(new SwipeListener(this));
        init();
    }

    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.i("Testing Socket", "" + mSocket.connected());
        }
    };

    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {

            Log.i("Testing Socket", "Doesn't work");
        }
    };

    private Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {

            Log.i("Testing Socket", "Disconnected");
        }
    };


    /*private GestureDetector.SimpleOnGestureListener gestureListener = new GestureDetector.SimpleOnGestureListener(){

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

            final float THRESHOLD = 20;
            float diffY = e2.getY() - e1.getY();


            if (diffY < 0 && diffY >= THRESHOLD) {
                if (!messagesRecyclerView.canScrollVertically(1)) {

                    Log.i("TestingChat", "It works");
                    messagesRecyclerView.setPadding(messagesRecyclerView.getPaddingLeft(), messagesRecyclerView.getPaddingTop(),
                            messagesRecyclerView.getPaddingRight(), messagesRecyclerView.getPaddingBottom() + 1);

                    messagesRecyclerView.setClipToPadding(false);
                }
            }
            return false;
        }
    };*/


    /*@Override
    public boolean onTouch(View v, MotionEvent event) {

        chatGestureDetector.onTouchEvent(event);
        return true;
    }*/

    public void init(){

            messagePanel = findViewById(R.id.textPanel);
            imageOfPerson = findViewById(R.id.person_image);
            nameOfPerson = findViewById(R.id.nameOfPerson);

            messagesRecyclerView = findViewById(R.id.user_messages);

            messageRecViewAdapter = new UserMessagesRecyclerViewAdapter(this);
            messageList = new ArrayList<>();

            messagePart = 1;

            calendar = Calendar.getInstance();

            //chatGestureDetector = new GestureDetector(ChatActivity.this, gestureListener);

            setMessagesRecyclerView();
            getIntentInfo();
    }

    public void setMessagesRecyclerView(){

        messageRecViewAdapter.setMessagesList(messageList);

        messagesRecyclerView.setAdapter(messageRecViewAdapter);
        messagesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        messagesRecyclerView.scrollBy(0,5);
        //messagesRecyclerView.setOnTouchListener(new SwipeListener(this));

        messagesRecyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {

                if(messageList.size() > 0)
                    messagesRecyclerView.scrollToPosition(messageList.size() - 1);

            }
        });
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

                    Message m = mr.getMsg();

                    int hour = calendar.get(Calendar.HOUR_OF_DAY);
                    String am_pm = hour/12 == 0 ? "AM" : "PM";

                    hour = hour % 12;
                    int minute = calendar.get(Calendar.MINUTE);

                    Date date = calendar.getTime();
                    String time = String.format("%d", hour) + String.format(":%02d ", minute) + am_pm;

                    SimpleDateFormat dateToString = new SimpleDateFormat("MMM, dd yyyy");
                    String dateString = dateToString.format(date);

                    SimpleDateFormat stringToDate = new SimpleDateFormat("MMM, dd yyyy");

                    try {
                        date = stringToDate.parse(dateString);

                        String year = dateString.substring(dateString.length() - 4);
                        dateString = dateString.substring(0, dateString.length() - 5);

                        m.setDate(date);
                        m.setDateString(dateString);
                        m.setYear(year);
                        m.setTime(time);

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

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
                Toast.makeText(ChatActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void loadMessageList(Chat ch){

        Log.i("Testing", "This user: " + person.getId());
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

                    for(int i = 0; i < messagesTempList.size(); i++){

                        String[] createdAt = messagesTempList.get(i).getCreatedAt().split("[A-Z]");

                        SimpleDateFormat toDate = new SimpleDateFormat("yyyy-MM-dd");

                        try {

                            Date date = toDate.parse(createdAt[0]);
                            SimpleDateFormat toString = new SimpleDateFormat("MMM, dd yyyy");
                            String dateString = toString.format(date);

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