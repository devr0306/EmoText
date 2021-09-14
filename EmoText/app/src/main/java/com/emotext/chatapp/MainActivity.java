package com.emotext.chatapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.emotext.chatapp.Adapters.MainViewPagerAdapter;
import com.emotext.chatapp.Models.app.ViewPagerSwipeControlled;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    private static FragmentManager mainActivityManager;

    public static RelativeLayout chatButtonLayout, peopleButtonLayout;
    private ImageView chatButton, cameraButton, peopleButton;
    private TextView chatButtonText, peopleButtonText;

    private CircleImageView profileImage;
    private CardView mainActivityToolbar, tempCardView;

    private Typeface lightNeris, boldNeris;

    public static ViewPagerSwipeControlled mainViewPager;
    private MainViewPagerAdapter mainViewPagerAdapter;

    private int currentPosition = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //No StatusBar
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(R.layout.activity_main);

        init();
    }



    public void init(){

        //mainNavView = findViewById(R.id.navigation_for_main_fragments);
        //mainNavView.setOnNavigationItemSelectedListener(navListener);

        Intent getFromAdd = getIntent();

        if(getFromAdd != null){

            int position = getFromAdd.getIntExtra("position", -1);

            if(position != -1)
                currentPosition = position;
        }

        mainActivityManager = getSupportFragmentManager();

        profileImage = findViewById(R.id.profile_pic_main_activity);

        String profilePicUrl = SharedPrefManager.getInstance(MainActivity.this).getUser().getProfilePictureURL();

        if(profilePicUrl == null)
            profileImage.setImageResource(R.drawable.ic_launcher_background);

        else{
            Glide.with(MainActivity.this)
                    .asBitmap()
                    .load(profilePicUrl)
                    .into(profileImage);
        }

        chatButtonLayout = findViewById(R.id.chat_tab_button);
        peopleButtonLayout = findViewById(R.id.people_tab_button);

        chatButton = findViewById(R.id.image_for_chat_button);
        cameraButton = findViewById(R.id.capture_button);
        peopleButton = findViewById(R.id.image_for_people_button);

        tempCardView = findViewById(R.id.temporary_support_cardview);
        mainActivityToolbar = findViewById(R.id.toolbar_for_main_activity);

        chatButtonText = findViewById(R.id.text_for_chat_button);
        peopleButtonText = findViewById(R.id.text_for_people_button);

        lightNeris = ResourcesCompat.getFont(MainActivity.this, R.font.neris_semi_bold);
        boldNeris = ResourcesCompat.getFont(MainActivity.this, R.font.neris_black);

        mainViewPager = findViewById(R.id.layout_for_main_fragments);
        mainViewPagerAdapter = new MainViewPagerAdapter(mainActivityManager, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mainViewPager.setAdapter(mainViewPagerAdapter);
        mainViewPager.setOnPageChangeListener(pageChangeListener);
        changeTab(currentPosition);

        findViewById(R.id.add_person_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toAddPerson = new Intent(MainActivity.this, AddPersonActivity.class);
                toAddPerson.putExtra("position", currentPosition);
                startActivity(toAddPerson);
                overridePendingTransition(R.anim.slide_in_up, R.anim.stay_stationary);
            }
        });

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toAddPerson = new Intent(MainActivity.this, UserInfoActivity.class);
                toAddPerson.putExtra("position", currentPosition);
                toAddPerson.putExtra("name", SharedPrefManager.getInstance(MainActivity.this).getUser().getName());
                toAddPerson.putExtra("image", SharedPrefManager.getInstance(MainActivity.this).getUser().getProfilePictureURL());
                toAddPerson.putExtra("toSettings", true);
                startActivity(toAddPerson);
                overridePendingTransition(R.anim.slide_in_up, R.anim.stay_stationary);
            }
        });

        chatButtonLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeTab(2);
            }
        });

        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    changeTab(1);
            }
        });

        peopleButtonLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeTab(0);
            }
        });
    }


    private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

            switch(position){

                case 0:

                    //Toolbar
                    mainActivityToolbar.setVisibility(View.VISIBLE);

                    //All image modifications
                    chatButton.setColorFilter(ContextCompat.getColor(MainActivity.this,R.color.darkGrey));

                    /*cameraButton.setColorFilter(ContextCompat.getColor(MainActivity.this,R.color.darkGrey));
                    int dimensionInDpForPerson = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 70, getResources().getDisplayMetrics());
                    cameraButton.getLayoutParams().height = dimensionInDpForPerson;
                    cameraButton.getLayoutParams().width = dimensionInDpForPerson;
                    cameraButton.requestLayout();*/
                    cameraButton.setVisibility(View.VISIBLE);

                    peopleButton.setColorFilter(ContextCompat.getColor(MainActivity.this,R.color.orange));

                    //All text modifications
                    chatButtonText.setTypeface(lightNeris);
                    chatButtonText.setTextColor(getResources().getColor(R.color.darkGrey));

                    peopleButtonText.setTypeface(boldNeris);
                    peopleButtonText.setTextColor(getResources().getColor(R.color.orange));

                    //Show the temporary CardView on the top
                    tempCardView.setVisibility(View.VISIBLE);

                    break;


                case 1:

                    //Toolbar
                    mainActivityToolbar.setVisibility(View.GONE);

                    //All image modifications
                    chatButton.setColorFilter(ContextCompat.getColor(MainActivity.this,R.color.grey));

                    /*cameraButton.setColorFilter(ContextCompat.getColor(MainActivity.this,R.color.white));
                    int dimensionInDpForCamera = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80, getResources().getDisplayMetrics());
                    cameraButton.getLayoutParams().height = dimensionInDpForCamera;
                    cameraButton.getLayoutParams().width = dimensionInDpForCamera;
                    cameraButton.requestLayout();*/
                    cameraButton.setVisibility(View.GONE);

                    peopleButton.setColorFilter(ContextCompat.getColor(MainActivity.this,R.color.grey));

                    //All text modifications
                    chatButtonText.setTypeface(lightNeris);
                    chatButtonText.setTextColor(getResources().getColor(R.color.grey));

                    peopleButtonText.setTypeface(lightNeris);
                    peopleButtonText.setTextColor(getResources().getColor(R.color.grey));

                    //Hide the temporary CardView on the top
                    tempCardView.setVisibility(View.GONE);

                    break;


                case 2:

                    //Toolbar
                    mainActivityToolbar.setVisibility(View.VISIBLE);

                    //All image modifications
                    chatButton.setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.orange));

                    /*cameraButton.setColorFilter(ContextCompat.getColor(MainActivity.this,R.color.darkGrey));
                    int dimensionInDpForChat = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 70, getResources().getDisplayMetrics());
                    cameraButton.getLayoutParams().height = dimensionInDpForChat;
                    cameraButton.getLayoutParams().width = dimensionInDpForChat;
                    cameraButton.requestLayout();*/
                    cameraButton.setVisibility(View.VISIBLE);

                    peopleButton.setColorFilter(ContextCompat.getColor(MainActivity.this,R.color.darkGrey));

                    //All text modifications
                    chatButtonText.setTypeface(boldNeris);
                    chatButtonText.setTextColor(getResources().getColor(R.color.orange));

                    peopleButtonText.setTypeface(lightNeris);
                    peopleButtonText.setTextColor(getResources().getColor(R.color.darkGrey));

                    //Show the temporary CardView on the top
                    tempCardView.setVisibility(View.VISIBLE);

                    break;
            }

            currentPosition = position;
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };


    public void changeTab(int position){

        mainViewPager.setCurrentItem(position);
    }

    public String getURLForResource (int resourceId) {
        //use BuildConfig.APPLICATION_ID instead of R.class.getPackage().getName() if both are not same
        return Uri.parse("android.resource://"+R.class.getPackage().getName()+"/" +resourceId).toString();
    }

    @Override
    public void onBackPressed() {
        this.finishAffinity();
    }
}