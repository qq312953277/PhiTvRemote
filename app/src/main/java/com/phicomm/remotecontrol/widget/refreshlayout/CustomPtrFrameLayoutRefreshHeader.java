package com.phicomm.remotecontrol.widget.refreshlayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;

import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * Created by hk on 2017/1/6.
 */
public class CustomPtrFrameLayoutRefreshHeader extends PtrFrameLayout {
    private CustomPtrHeader mCustomPtrHeader;

    public CustomPtrFrameLayoutRefreshHeader(Context context) {
        super(context);
        initViews();
        initGesture();
    }

    public CustomPtrFrameLayoutRefreshHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews();
        initGesture();
    }

    public CustomPtrFrameLayoutRefreshHeader(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initViews();
        initGesture();
    }

    private void initViews() {
        mCustomPtrHeader = new CustomPtrHeader(getContext());
        setHeaderView(mCustomPtrHeader);
        addPtrUIHandler(mCustomPtrHeader);
    }

    public CustomPtrHeader getHeader() {
        return mCustomPtrHeader;
    }

    /**
     * Specify the last update time by this key string
     *
     * @param key
     */
    public void setLastUpdateTimeKey(String key) {
        if (mCustomPtrHeader != null) {
            mCustomPtrHeader.setLastUpdateTimeKey(key);
        }
    }

    /**
     * Using an object to specify the last update time.
     *
     * @param object
     */
    public void setLastUpdateTimeRelateObject(Object object) {
        if (mCustomPtrHeader != null) {
            mCustomPtrHeader.setLastUpdateTimeRelateObject(object);
        }
    }

    private GestureDetector mDetector;
    private boolean mIsDisallowIntercept = false;

    private void initGesture() {
        mDetector = new GestureDetector(getContext(), gestureListener);
    }

    private boolean mIsHorizontalMode = false;
    private boolean mIsFirst = true;
    private boolean mIsLoading = false;

    @Override
    public boolean dispatchTouchEvent(MotionEvent e) {
        mDetector.onTouchEvent(e);
        if (e.getAction() == MotionEvent.ACTION_UP) {
            mIsFirst = true;
            mIsHorizontalMode = false;
            mIsDisallowIntercept = false;
            return super.dispatchTouchEvent(e);
        }
        if (mDetector.onTouchEvent(e) && mIsDisallowIntercept && mIsHorizontalMode) {
            return dispatchTouchEventSupper(e);
        }
        if (mIsHorizontalMode) {
            return dispatchTouchEventSupper(e);
        }
        return super.dispatchTouchEvent(e);
    }

    /**
     * 对于底层的View来说，有一种方法可以阻止父层的View截获touch事件，就是调用getParent().requestDisallowInterceptTouchEvent(true);方法。
     * 一旦底层View收到touch的action后调用这个方法那么父层View就不会再调用onInterceptTouchEvent了，也无法截获以后的action。
     */
    @Override
    public void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        this.mIsDisallowIntercept = disallowIntercept;
        super.requestDisallowInterceptTouchEvent(disallowIntercept);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        // if(isLoading){ return  true;}
        if (mCustomPtrHeader.isRefreshing()) {
            return true;
        }
        return super.onInterceptTouchEvent(ev);
    }

    private GestureDetector.OnGestureListener gestureListener = new GestureDetector.OnGestureListener() {
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public void onShowPress(MotionEvent e) {
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            float disX, disY;
            disX = Math.abs(distanceX);
            disY = Math.abs(distanceY);

            if (disX > disY) {
                if (mIsFirst) {
                    mIsHorizontalMode = true;
                    mIsFirst = false;
                }
            } else {
                if (mIsFirst) {
                    mIsHorizontalMode = false;
                    mIsFirst = false;
                }
                return false;//垂直滑动会返回false
            }

            return true;//水平滑动会返回true
        }

        @Override
        public void onLongPress(MotionEvent e) {

        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return false;
        }
    };
}
