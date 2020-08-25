package com.example.chatapp.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.chatapp.Fragments.CameraTabFragment;
import com.example.chatapp.Fragments.PeopleTabFragment;
import com.example.chatapp.Fragments.ChatTabFragment;

public class MainViewPagerAdapter extends FragmentStatePagerAdapter {

    public MainViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:
                return new PeopleTabFragment();
            case 1:
                return new CameraTabFragment();
            case 2:
                return new ChatTabFragment();
        }

        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
