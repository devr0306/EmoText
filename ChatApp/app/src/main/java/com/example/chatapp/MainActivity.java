package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.example.chatapp.Adapters.MainViewPagerAdapter;
import com.example.chatapp.Fragments.CameraTabFragment;
import com.example.chatapp.Fragments.ChatTabFragment;
import com.example.chatapp.Fragments.InteractTabFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private static FragmentManager mainActivityManager;
    private FragmentTransaction mainFT;

    private ViewPager mainViewPager;
    private MainViewPagerAdapter mainViewPagerAdapter;

    private BottomNavigationView mainNavView;

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

    private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

            switch(position){

                case 0:
                    mainNavView.setVisibility(View.VISIBLE);
                    mainNavView.getMenu().findItem(R.id.chat_main_menu_tab).setChecked(true);
                    break;

                case 1:
                    mainNavView.getMenu().findItem(R.id.camera_main_menu_tab).setChecked(true);
                    mainNavView.setVisibility(View.GONE);
                    break;

                case 2:
                    mainNavView.setVisibility(View.VISIBLE);
                    mainNavView.getMenu().findItem(R.id.interact_main_menu_tab).setChecked(true);
                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    public void init(){

        mainNavView = findViewById(R.id.navigation_for_main_fragments);
        mainNavView.setOnNavigationItemSelectedListener(navListener);

        mainActivityManager = getSupportFragmentManager();

        mainViewPager = findViewById(R.id.layout_for_main_fragments);
        mainViewPagerAdapter = new MainViewPagerAdapter(mainActivityManager, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mainViewPager.setAdapter(mainViewPagerAdapter);
        mainViewPager.setOnPageChangeListener(pageChangeListener);
    }

    public void changeTab(int position){

        mainViewPager.setCurrentItem(position);
    }
}