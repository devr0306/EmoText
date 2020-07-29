package com.example.chatapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.chatapp.Adapters.MainViewPagerAdapter;
import com.example.chatapp.Fragments.CameraTabFragment;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    private static FragmentManager mainActivityManager;
    private FragmentTransaction mainFT;

    private ImageView chatButton, cameraButton, peopleButton;
    private CircleImageView profileImage;
    private TextView chatButtonText, peopleButtonText;
    private CardView mainActivityToolbar;

    private Typeface lightNeris, boldNeris;

    private ViewPager mainViewPager;
    private MainViewPagerAdapter mainViewPagerAdapter;

    private int currentPosition = 1;

    /*private BottomNavigationView mainNavView;

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch(item.getItemId()){

                case R.id.chat_main_menu_tab:
                    mainViewPager.setCurrentItem(0);
                    break;

                case R.id.camera_main_menu_tab:
                    mainViewPager.setCurrentItem(1);
                    break;

                case R.id.interact_main_menu_tab:
                    mainViewPager.setCurrentItem(2);
                    break;
            }
            return true;
        }
    };
*/
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
                    chatButton.setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.orange));

                    cameraButton.setColorFilter(ContextCompat.getColor(MainActivity.this,R.color.darkGrey));
                    int dimensionInDpForChat = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 70, getResources().getDisplayMetrics());
                    cameraButton.getLayoutParams().height = dimensionInDpForChat;
                    cameraButton.getLayoutParams().width = dimensionInDpForChat;
                    cameraButton.requestLayout();

                    peopleButton.setColorFilter(ContextCompat.getColor(MainActivity.this,R.color.darkGrey));

                    //All text modifications
                    chatButtonText.setTypeface(boldNeris);
                    chatButtonText.setTextColor(getResources().getColor(R.color.orange));

                    peopleButtonText.setTypeface(lightNeris);
                    peopleButtonText.setTextColor(getResources().getColor(R.color.darkGrey));
                    break;

                case 1:

                    //Toolbar
                    mainActivityToolbar.setVisibility(View.GONE);



                    //All image modifications
                    chatButton.setColorFilter(ContextCompat.getColor(MainActivity.this,R.color.grey));

                    cameraButton.setColorFilter(ContextCompat.getColor(MainActivity.this,R.color.white));
                    int dimensionInDpForCamera = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80, getResources().getDisplayMetrics());
                    cameraButton.getLayoutParams().height = dimensionInDpForCamera;
                    cameraButton.getLayoutParams().width = dimensionInDpForCamera;
                    cameraButton.requestLayout();

                    peopleButton.setColorFilter(ContextCompat.getColor(MainActivity.this,R.color.grey));

                    //All text modifications
                    chatButtonText.setTypeface(lightNeris);
                    chatButtonText.setTextColor(getResources().getColor(R.color.grey));

                    peopleButtonText.setTypeface(lightNeris);
                    peopleButtonText.setTextColor(getResources().getColor(R.color.grey));
                    break;

                case 2:

                    //Toolbar
                    mainActivityToolbar.setVisibility(View.VISIBLE);

                    //All image modifications
                    chatButton.setColorFilter(ContextCompat.getColor(MainActivity.this,R.color.darkGrey));

                    cameraButton.setColorFilter(ContextCompat.getColor(MainActivity.this,R.color.darkGrey));
                    int dimensionInDpForPerson = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 70, getResources().getDisplayMetrics());
                    cameraButton.getLayoutParams().height = dimensionInDpForPerson;
                    cameraButton.getLayoutParams().width = dimensionInDpForPerson;
                    cameraButton.requestLayout();

                    peopleButton.setColorFilter(ContextCompat.getColor(MainActivity.this,R.color.orange));

                    //All text modifications
                    chatButtonText.setTypeface(lightNeris);
                    chatButtonText.setTextColor(getResources().getColor(R.color.darkGrey));

                    peopleButtonText.setTypeface(boldNeris);
                    peopleButtonText.setTextColor(getResources().getColor(R.color.orange));
                    break;
            }

            currentPosition = position;
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //No StatusBar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

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

        chatButton = findViewById(R.id.image_for_chat_button);
        cameraButton = findViewById(R.id.capture_button);
        peopleButton = findViewById(R.id.image_for_people_button);
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
                startActivity(toAddPerson);
                overridePendingTransition(R.anim.slide_in_up, R.anim.stay_stationary);
            }
        });

        chatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeTab(0);
            }
        });

        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(currentPosition == 1){

                    CameraTabFragment ctf = (CameraTabFragment) mainViewPagerAdapter.getItem(mainViewPager.getCurrentItem());
                    ctf.captureImage(MainActivity.this);
                    Log.d("Success", "It works");
                }
                else
                    changeTab(1);
            }
        });

        peopleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeTab(2);
            }
        });
    }

    public void changeTab(int position){

        mainViewPager.setCurrentItem(position);
    }

    public String getURLForResource (int resourceId) {
        //use BuildConfig.APPLICATION_ID instead of R.class.getPackage().getName() if both are not same
        return Uri.parse("android.resource://"+R.class.getPackage().getName()+"/" +resourceId).toString();
    }
}