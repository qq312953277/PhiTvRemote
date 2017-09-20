package com.phicomm.remotecontrol.widget.refreshlayout;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.phicomm.remotecontrol.R;

import java.text.SimpleDateFormat;
import java.util.Date;

import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrUIHandler;
import in.srain.cube.views.ptr.indicator.PtrIndicator;

/**
 * Created by hk on 2017/1/5.
 */
public class CustomPtrHeader extends FrameLayout implements PtrUIHandler {
    private final static String KEY_SharedPreferences = "cube_ptr_classic_last_update";
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private int mRotateAniTime = 1000;
    private RotateAnimation mFlipAnimation;
    private RotateAnimation mReverseFlipAnimation;
    private Animation mLoadingAnimation;
    private TextView mTvTitle;
    private ImageView mProgressView;
    private View mArrowView;
    private long mLastUpdateTime = -1;
    private TextView mLastUpdateTextView;
    private String mLastUpdateTimeKey;
    private boolean mShouldShowLastUpdate;

    private LastUpdateTimeUpdater mLastUpdateTimeUpdater = new LastUpdateTimeUpdater();
    private boolean mIsRefreshing = false;

    //可以仿照PtrClassicDefaultHeader或者看 http://www.ithao123.cn/content-10625186.html
    public CustomPtrHeader(Context context) {
        super(context);
        initView(null);
    }

    public CustomPtrHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(attrs);
    }

    public CustomPtrHeader(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(attrs);
    }

    protected void initView(AttributeSet attr) {
//        if (attr != null) {
//            mRotateAniTime = attr.getInt(in.srain.cube.views.ptr.R.styleable.PtrClassicHeader_ptr_rotate_ani_time, mRotateAniTime);
//        }
        buildAnimation();
        buildLoadingAnimation();
        View header = LayoutInflater.from(getContext()).inflate(R.layout.ptr_header, this);

        mArrowView = header.findViewById(R.id.arrow_view);

        mTvTitle = (TextView) header.findViewById(R.id.tv_title);
        mLastUpdateTextView = (TextView) header.findViewById(R.id.tv_time);
        mProgressView = (ImageView) header.findViewById(R.id.img_rotate_loading);
        resetView();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mLastUpdateTimeUpdater != null) {
            mLastUpdateTimeUpdater.stop();
        }
    }

    public void setRotateAniTime(int time) {
        if (time == mRotateAniTime || time == 0) {
            return;
        }
        mRotateAniTime = time;
        buildAnimation();
    }

    /**
     * Specify the last update time by this key string
     *
     * @param key
     */
    public void setLastUpdateTimeKey(String key) {
        if (TextUtils.isEmpty(key)) {
            return;
        }
        mLastUpdateTimeKey = key;
    }

    /**
     * frame.setLastUpdateTimeRelateObject方法里调用此方法
     * <p>
     * Using an object to specify the last update time.
     *
     * @param object
     */
    public void setLastUpdateTimeRelateObject(Object object) {
        setLastUpdateTimeKey(object.getClass().getName());
    }

    /**
     * AccelerateDecelerateInterpolator 在动画开始与结束的地方速率改变比较慢，在中间的时候加速
     * <p>
     * AccelerateInterpolator  在动画开始的地方速率改变比较慢，然后开始加速
     * <p>
     * AnticipateInterpolator 开始的时候向后然后向前甩
     * <p>
     * AnticipateOvershootInterpolator 开始的时候向后然后向前甩一定值后返回最后的值
     * <p>
     * BounceInterpolator   动画结束的时候弹起
     * <p>
     * CycleInterpolator 动画循环播放特定的次数，速率改变沿着正弦曲线
     * <p>
     * DecelerateInterpolator 在动画开始的地方快然后慢
     * <p>
     * LinearInterpolator   以常量速率改变
     * <p>
     * OvershootInterpolator    向前甩一定值后再回到原来位置
     */
    private void buildAnimation() {
//        RotateAnimation (float fromDegrees, float toDegrees, int pivotXType, float pivotXValue, int pivotYType, float pivotYValue)
//        参数说明：
//        float fromDegrees：旋转的开始角度。
//        float toDegrees：旋转的结束角度。
//        int pivotXType：X轴的伸缩模式，可以取值为ABSOLUTE、RELATIVE_TO_SELF、RELATIVE_TO_PARENT。
//        float pivotXValue：X坐标的伸缩值。
//        int pivotYType：Y轴的伸缩模式，可以取值为ABSOLUTE、RELATIVE_TO_SELF、RELATIVE_TO_PARENT。
//        float pivotYValue：Y坐标的伸缩值。

        mFlipAnimation = new RotateAnimation(0, -180, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        //LinearInterpolator   以常量速率改变
        mFlipAnimation.setInterpolator(new LinearInterpolator());
        mFlipAnimation.setDuration(mRotateAniTime);
        //动画执行完后是否停留在执行完的状态
        mFlipAnimation.setFillAfter(true);

        mReverseFlipAnimation = new RotateAnimation(-180, 0, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        mReverseFlipAnimation.setInterpolator(new LinearInterpolator());
        mReverseFlipAnimation.setDuration(mRotateAniTime);
        mReverseFlipAnimation.setFillAfter(true);
    }
    private void buildLoadingAnimation(){
        mLoadingAnimation = AnimationUtils.loadAnimation(getContext(),R.animator.rotate__refresh_anim);
        mLoadingAnimation.setInterpolator(new LinearInterpolator());
    }

    private void resetView() {
        hideRotateView();
        hideLoadingView();
    }

    private void hideRotateView() {
        mArrowView.clearAnimation();
        mArrowView.setVisibility(GONE);
    }

    private void hideLoadingView(){
        mProgressView.clearAnimation();
        mProgressView.setVisibility(GONE);
    }
    /**
     * 重置，回到顶部的
     */
    @Override
    public void onUIReset(PtrFrameLayout frame) {
        resetView();
        mShouldShowLastUpdate = true;
        tryUpdateLastUpdateTime();
    }

    /**
     * 准备刷新，header将要出现时调用
     */
    @Override
    public void onUIRefreshPrepare(PtrFrameLayout frame) {
        mShouldShowLastUpdate = true;
        //这个方法有没有必要再调一遍
        tryUpdateLastUpdateTime();

        mLastUpdateTimeUpdater.start();
        hideLoadingView();
//        mProgressView.setVisibility(GONE);
        mArrowView.setVisibility(VISIBLE);
        mTvTitle.setVisibility(VISIBLE);
        if (frame.isPullToRefresh()) {
            mTvTitle.setText(getResources().getString(in.srain.cube.views.ptr.R.string.cube_ptr_pull_down));
        } else {
            mTvTitle.setText(getResources().getString(in.srain.cube.views.ptr.R.string.cube_ptr_pull_down_to_refresh));
        }
//        if (frame.isPullToRefresh()) {
//            mTvTitle.setText("下拉12");
//        } else {
//            mTvTitle.setText("下拉列表11");
//        }

    }


    /**
     * 开始刷新，header 进入刷新状态之前调用
     */
    @Override
    public void onUIRefreshBegin(PtrFrameLayout frame) {
        mIsRefreshing = true;
        mShouldShowLastUpdate = false;
        hideRotateView();
        mProgressView.setVisibility(VISIBLE);
        mProgressView.startAnimation(mLoadingAnimation);
        mTvTitle.setVisibility(VISIBLE);
        mTvTitle.setText(getResources().getString(R.string.refershlayout_is_loading));
//        mTvTitle.setText(getResources().getString(in.srain.cube.views.ptr.R.string.cube_ptr_refreshing));

        tryUpdateLastUpdateTime();
        mLastUpdateTimeUpdater.stop();
    }

    /**
     * 下拉过程中位置变化回调
     */

    @Override
    public void onUIPositionChange(PtrFrameLayout frame, boolean isUnderTouch, byte status, PtrIndicator ptrIndicator) {

        final int mOffsetToRefresh = frame.getOffsetToRefresh();
        final int currentPos = ptrIndicator.getCurrentPosY();
        final int lastPos = ptrIndicator.getLastPosY();

        if (currentPos < mOffsetToRefresh && lastPos >= mOffsetToRefresh) {
            if (isUnderTouch && status == PtrFrameLayout.PTR_STATUS_PREPARE) {
                crossRotateLineFromBottomUnderTouch(frame);
                if (mArrowView != null) {
                    mArrowView.clearAnimation();
                    mArrowView.startAnimation(mReverseFlipAnimation);
                }
            }
        } else if (currentPos > mOffsetToRefresh && lastPos <= mOffsetToRefresh) {
            if (isUnderTouch && status == PtrFrameLayout.PTR_STATUS_PREPARE) {
                crossRotateLineFromTopUnderTouch(frame);
                if (mArrowView != null) {
                    mArrowView.clearAnimation();
                    mArrowView.startAnimation(mFlipAnimation);
                }
            }
        }
    }

    /**
     * 下拉列表
     */
    private void crossRotateLineFromBottomUnderTouch(PtrFrameLayout frame) {
        mTvTitle.setVisibility(VISIBLE);
        if (frame.isPullToRefresh()) {
            mTvTitle.setText(getResources().getString(in.srain.cube.views.ptr.R.string.cube_ptr_pull_down_to_refresh));
        } else {
            mTvTitle.setText(getResources().getString(in.srain.cube.views.ptr.R.string.cube_ptr_pull_down));
        }
//        if (frame.isPullToRefresh()) {
//            mTvTitle.setText("下拉中。。。");
//        } else {
//            mTvTitle.setText("下拉列表12");
//        }
    }

    /**
     * 释放刷新
     */
    private void crossRotateLineFromTopUnderTouch(PtrFrameLayout frame) {
        if (!frame.isPullToRefresh()) {
            mTvTitle.setVisibility(VISIBLE);
            //mTvTitle.setText("释放列表");
            mTvTitle.setText(in.srain.cube.views.ptr.R.string.cube_ptr_release_to_refresh);
        }
    }

    /*
           * 加载完成
           * */
    @Override
    public void onUIRefreshComplete(PtrFrameLayout frame) {
        mIsRefreshing = false;
        hideRotateView();
        hideLoadingView();
//        mProgressView.setVisibility(GONE);
        mTvTitle.setVisibility(VISIBLE);
        mTvTitle.setText("");
//        mTvTitle.setText(getResources().getString(in.srain.cube.views.ptr.R.string.cube_ptr_refresh_complete));

        // update last update time
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(KEY_SharedPreferences, 0);
        if (!TextUtils.isEmpty(mLastUpdateTimeKey)) {
            mLastUpdateTime = new Date().getTime();
            sharedPreferences.edit().putLong(mLastUpdateTimeKey, mLastUpdateTime).commit();
        }
    }

    public boolean isRefreshing() {
        return mIsRefreshing;
    }

    public void setIsRefreshing(boolean isRefreshing) {
        this.mIsRefreshing = isRefreshing;
    }

    private void tryUpdateLastUpdateTime() {
        if (TextUtils.isEmpty(mLastUpdateTimeKey) || !mShouldShowLastUpdate) {
            mLastUpdateTextView.setVisibility(GONE);
        } else {
            String time = getLastUpdateTime();
            if (TextUtils.isEmpty(time)) {
                mLastUpdateTextView.setVisibility(GONE);
            } else {
                mLastUpdateTextView.setVisibility(GONE);
               // mLastUpdateTextView.setVisibility(VISIBLE);
                mLastUpdateTextView.setText(time);
            }
        }
    }

    private String getLastUpdateTime() {

        if (mLastUpdateTime == -1 && !TextUtils.isEmpty(mLastUpdateTimeKey)) {
            mLastUpdateTime = getContext().getSharedPreferences(KEY_SharedPreferences, 0).getLong(mLastUpdateTimeKey, -1);
        }
        if (mLastUpdateTime == -1) {
            return null;
        }
        long diffTime = new Date().getTime() - mLastUpdateTime;
        int seconds = (int) (diffTime / 1000);
        if (diffTime < 0) {
            return null;
        }
        if (seconds <= 0) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(getContext().getString(in.srain.cube.views.ptr.R.string.cube_ptr_last_update));

        if (seconds < 60) {
            sb.append(seconds + getContext().getString(in.srain.cube.views.ptr.R.string.cube_ptr_seconds_ago));
        } else {
            int minutes = (seconds / 60);
            if (minutes > 60) {
                int hours = minutes / 60;
                if (hours > 24) {
                    Date date = new Date(mLastUpdateTime);
                    sb.append(simpleDateFormat.format(date));
                } else {
                    sb.append(hours + getContext().getString(in.srain.cube.views.ptr.R.string.cube_ptr_hours_ago));
                }

            } else {
                sb.append(minutes + getContext().getString(in.srain.cube.views.ptr.R.string.cube_ptr_minutes_ago));
            }
        }
        return sb.toString();
    }

    private class LastUpdateTimeUpdater implements Runnable {
        private boolean mRunning = false;

        private void start() {

            if (TextUtils.isEmpty(mLastUpdateTimeKey)) {
                return;
            }
            mRunning = true;
            run();
        }

        private void stop() {
            mRunning = false;
            removeCallbacks(this);
        }

        @Override
        public void run() {
            tryUpdateLastUpdateTime();
            if (mRunning) {
                postDelayed(this, 1000);
            }
        }
    }

}
