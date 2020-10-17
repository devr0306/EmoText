package com.example.chatapp.Models.app;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

public class ViewPagerSwipeControlled extends ViewPager {

    private boolean swipeEnabled = true;

    public ViewPagerSwipeControlled(@NonNull Context context) {
        super(context);
    }

    public ViewPagerSwipeControlled(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void setSwipeEnabled(boolean swipeEnabled){
        this.swipeEnabled = swipeEnabled;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return swipeEnabled && super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        if(swipeEnabled)
            return super.onTouchEvent(ev);

        return true;
    }
}
