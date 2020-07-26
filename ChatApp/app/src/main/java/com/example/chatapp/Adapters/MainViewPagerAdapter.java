package com.example.chatapp.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.chatapp.Fragments.CameraTabFragment;
import com.example.chatapp.Fragments.ChatTabFragment;
import com.example.chatapp.Fragments.InteractTabFragment;

public class MainViewPagerAdapter extends FragmentPagerAdapter {

    public MainViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){

            case 0:
                return new ChatTabFragment();
            case 1:
                return new CameraTabFragment();
            case 2:
                return new InteractTabFragment();
            default:
                return new ChatTabFragment();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
