package com.phicomm.remotecontrol.modules.devices.searchdevices;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.phicomm.remotecontrol.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kang.sun on 2017/9/20.
 */

public class DiscoveryProgressbar extends View {
    private Paint mPaint;
    private int mMaxWidth = 255;
    private boolean mIsStarting = false;
    private List<String> mAlphaList = new ArrayList<String>();
    private List<String> mStartWidthList = new ArrayList<String>();
    public static final String mInitAlphaList = "255";
    public static final String mInitStartWidthList = "0";

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
        mPaint.setColor(getResources().getColor(R.color.discovery_circle));
        mAlphaList.add(mInitAlphaList);
        mStartWidthList.add(mInitStartWidthList);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        setBackgroundColor(Color.TRANSPARENT);
        for (int i = 0; i < mAlphaList.size(); i++) {
            int mAlpha = Integer.parseInt(mAlphaList.get(i));
            int mStartWidth = Integer.parseInt(mStartWidthList.get(i));
            mPaint.setAlpha(mAlpha);
            canvas.drawCircle(getWidth() / 2, getHeight() / 2, mStartWidth + 50,
                    mPaint);
            if (mIsStarting && mAlpha > 0 && mStartWidth < mMaxWidth) {
                mAlphaList.set(i, (mAlpha - 1) + "");
                mStartWidthList.set(i, (mStartWidth + 1) + "");
            }
        }
        if (mIsStarting
                && Integer
                .parseInt(mStartWidthList.get(mStartWidthList.size() - 1)) == mMaxWidth / 5) {
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

    public void stop() {
        mIsStarting = false;
    }

    public boolean isStarting() {
        return mIsStarting;
    }
}

