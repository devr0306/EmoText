package com.example.chatapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserInfoActivity extends AppCompatActivity {

    private TextView name;
    private ImageView settingsButton, backButton;
    private CircleImageView profilePic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        init();
    }

    public void init(){

        name = findViewById(R.id.account_user_name);
        settingsButton = findViewById(R.id.settings_button_profile);
        backButton = findViewById(R.id.back_arrow_button_profile);
        profilePic = findViewById(R.id.big_account_profile_image);

        name.setText(SharedPrefManager.getInstance(UserInfoActivity.this).getUser().getName());

        String profilePicUrl = SharedPrefManager.getInstance(UserInfoActivity.this).getUser().getProfilePictureURL();

        if(profilePicUrl == null)
            profilePic.setImageResource(R.drawable.ic_launcher_background);

        else{
            Glide.with(UserInfoActivity.this)
                    .asBitmap()
                    .load(profilePicUrl)
                    .into(profilePic);
        }

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateOut();
            }
        });

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserInfoActivity.this, SettingsActivity.class));
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
                getWindow().getDecorView().findViewById(android.R.id.content).clearAnimation();
                finish();
                // if you call NavUtils.navigateUpFromSameTask(activity); instead,
                // the screen will flicker once after the animation. Since FrontActivity is
                // in front of BackActivity, calling finish() should give the same result.
                overridePendingTransition(0, 0);
            }
        });
        getWindow().getDecorView().findViewById(android.R.id.content).startAnimation(slideAnim);
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
        animateOut();
        return;
    }

    public String getURLForResource (int resourceId) {
        //use BuildConfig.APPLICATION_ID instead of R.class.getPackage().getName() if both are not same
        return Uri.parse("android.resource://"+R.class.getPackage().getName()+"/" +resourceId).toString();
    }
}