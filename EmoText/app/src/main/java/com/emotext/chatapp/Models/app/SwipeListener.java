package com.emotext.chatapp.Models.app;

import android.view.MotionEvent;
import android.view.View;

public class SwipeListener implements View.OnTouchListener {

    private final String logTag = "Swipe worked";
    private final int MIN_DIST_X = 200;
    private final int MIN_DIST_Y = 500;
    private SwipeListenerInterface activity;
    private float downX, downY, upX, upY;

    public SwipeListener(SwipeListenerInterface activity) {
        this.activity = activity;
    }

    public void onRightToLeftSwipe(View v) {
        activity.onRightToLeftSwipe(v);
    }

    public void onLeftToRightSwipe(View v) {
        activity.onLeftToRightSwipe(v);
    }

    public void onTopToBottomSwipe(View v) {
        activity.onTopToBottomSwipe(v);
    }

    public void onBottomToTopSwipe(View v) {
        activity.onBottomToTopSwipe(v);
    }

    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                downX = event.getX();
                downY = event.getY();
                return true;
            }
            case MotionEvent.ACTION_UP: {
                upX = event.getX();
                upY = event.getY();
                float deltaX = downX - upX;
                float deltaY = downY - upY;

                if(Math.abs(deltaY) > MIN_DIST_Y){

                    if (deltaY < 0) {
                        this.onTopToBottomSwipe(v);
                        return true;
                    }
                    if (deltaY > 0) {
                        this.onBottomToTopSwipe(v);
                        return true;
                    }
                }

                if (Math.abs(deltaX) > MIN_DIST_X){

                    if (deltaX < 0 ) {
                        this.onLeftToRightSwipe(v);
                        return true;
                    }
                    if (deltaX > 0 ) {
                        this.onRightToLeftSwipe(v);
                        return true;
                    }
                }
            }
        }
        return false;
    }

}