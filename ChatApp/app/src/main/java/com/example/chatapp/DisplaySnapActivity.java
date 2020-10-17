package com.example.chatapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;

public class DisplaySnapActivity extends AppCompatActivity {

    private CircleImageView profilePic;
    private TextView name;
    private ImageView snap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //No StatusBar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_display_snap);

        init();
    }

    private void init(){

        profilePic = findViewById(R.id.friend_profile_pic_snap);
        name = findViewById(R.id.text_snap);

        snap = findViewById(R.id.snap_image);

        getIntentInfo();
    }

    private void getIntentInfo(){

        Intent getInfo = new Intent();

        if(getInfo != null){

            String profileUrl = getInfo.getStringExtra("profileUrl");
            String personName = getInfo.getStringExtra("name");
            String snapImage = getInfo.getStringExtra("snap");

            if(profileUrl == null || profileUrl.length() < 1)
                profilePic.setImageResource(R.drawable.ic_launcher_background);

            else
                Glide.with(this)
                .asBitmap()
                .load(profileUrl)
                .into(profilePic);

            if(personName != null && personName.length() > 0)
                name.setText(personName);

            if(snapImage != null && snapImage.length() > 0)
                Glide.with(this)
                .asBitmap()
                .load(snapImage)
                .into(snap);
        }
    }
}