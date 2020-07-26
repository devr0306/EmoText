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
import android.widget.Toast;

import com.example.chatapp.Adapters.MainViewPagerAdapter;
import com.example.chatapp.Fragments.CameraTabFragment;
import com.example.chatapp.Fragments.ChatTabFragment;
import com.example.chatapp.Fragments.InteractTabFragment;
import com.example.chatapp.Models.API.UserListResponse;
import com.example.chatapp.RetrofitClients.ContactsAPIClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

        setContacts();
    }

    public void setContacts(){

        Call<UserListResponse> getContactsCall = ContactsAPIClient
                .getInstance()
                .getContactsAPI()
                .getContacts(SharedPrefManager.getInstance(MainActivity.this).getUser().getToken());

        getContactsCall.enqueue(new Callback<UserListResponse>() {
            @Override
            public void onResponse(Call<UserListResponse> call, Response<UserListResponse> response) {

                if(response.isSuccessful()){
                    UserListResponse ulr = response.body();
                    Toast.makeText(MainActivity.this, Arrays.toString(ulr.getContacts()), Toast.LENGTH_SHORT).show();
                }

                else{
                    try {
                        Toast.makeText(MainActivity.this, response.errorBody().string(), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<UserListResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void changeTab(int position){

        mainViewPager.setCurrentItem(position);
    }
}