package com.phicomm.remotecontrol.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * Created by hao04.wu on 2017/8/9.
 */

public class CustomScrollView extends ScrollView {
    GestureDetector mGestureDetector;

    public CustomScrollView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public CustomScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    public CustomScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
    }

    public void setGestureDetector(GestureDetector gestureDetector) {
        this.mGestureDetector = gestureDetector;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        // TODO Auto-generated method stub
        super.onTouchEvent(ev);
        return mGestureDetector.onTouchEvent(ev);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        mGestureDetector.onTouchEvent(ev);
        super.dispatchTouchEvent(ev);
        return true;
    }
}
