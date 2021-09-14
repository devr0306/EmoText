package com.emotext.chatapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.FrameLayout;

import com.emotext.chatapp.Fragments.Settings.SettingsFragment;

public class SettingsActivity extends AppCompatActivity {

    private static FragmentManager fragManager;
    private FragmentTransaction transaction;
    private FrameLayout settingsContainer;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        init();
    }

    public void init(){

        settingsContainer = findViewById(R.id.settings_container);

        fragManager = getSupportFragmentManager();
        changeFragment(new SettingsFragment());

    }

    public void changeFragment(Fragment fragment){

        transaction = fragManager.beginTransaction();
        transaction.replace(R.id.settings_container, fragment);
        transaction.commit();
    }
}