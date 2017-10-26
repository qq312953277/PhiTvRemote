package com.phicomm.remotecontrol.modules.devices.searchdevices;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

import com.phicomm.remotecontrol.R;
import com.phicomm.remotecontrol.base.BaseApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kang.sun on 2017/9/20.
 */

public class DiscoveryProgressbar extends View {
    private Paint mPaint;
    private int mMaxWidth = 260;
    private int mMaxWidthWithAndroid7 = 180;
    private boolean mIsStarting = false;
    private List<String> mAlphaList = new ArrayList<>();
    private List<String> mStartWidthList = new ArrayList<>();
    public static final String mInitAlphaList = "255";
    public static final String mInitStartWidthList = "0";
    public static final int CIRCLE_MIN_RADIUS = 30;

    public DiscoveryProgressbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public DiscoveryProgressbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DiscoveryProgressbar(Context context) {
        super(context);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setColor(getResources().getColor(R.color.white_normal));
        mAlphaList.add(mInitAlphaList);
        mStartWidthList.add(mInitStartWidthList);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int maxWidth = (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) ? mMaxWidthWithAndroid7 : mMaxWidth;

        setBackgroundColor(Color.TRANSPARENT);
        for (int i = 0; i < mAlphaList.size(); i++) {
            int mAlpha = Integer.parseInt(mAlphaList.get(i));
            int mStartWidth = Integer.parseInt(mStartWidthList.get(i));
            mPaint.setAlpha(mAlpha);
            canvas.drawCircle(getWidth() / 2, getHeight() / 2, mStartWidth + CIRCLE_MIN_RADIUS,
                    mPaint);
            if (mIsStarting && mAlpha > 0 && mStartWidth < maxWidth) {
                mAlphaList.set(i, (mAlpha - 1) + "");
                mStartWidthList.set(i, (mStartWidth + 1) + "");
            }
        }
        String startLength = mStartWidthList.get(mStartWidthList.size() - 1);
        if (mIsStarting && Integer.parseInt(startLength) == maxWidth / 5) {
            mAlphaList.add(mInitAlphaList);
            mStartWidthList.add(mInitStartWidthList);
        }
        if (mIsStarting && mStartWidthList.size() == 4) {
            mStartWidthList.remove(0);
            mAlphaList.remove(0);
        }
        invalidate();
    }

    public void start() {
        mIsStarting = true;
    }

}

