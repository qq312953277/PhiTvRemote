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
    private int mWidth;
    private int mHeight;
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
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int measuredHeight = measureHeight(heightMeasureSpec);
        int measuredWidth = measureWidth(widthMeasureSpec);

        setMeasuredDimension(measuredWidth, measuredHeight);

        mWidth = getWidth();
        mHeight = getHeight();

    }

    /**
     * 测量宽度
     *
     * @param measureSpec
     * @return
     */
    private int measureWidth(int measureSpec) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        int result = 0;

        if (specMode == MeasureSpec.AT_MOST) {
            result = getWidth();
        } else if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        }
        return result;
    }

    /**
     * 测量高度
     *
     * @param measureSpec
     * @return
     */
    private int measureHeight(int measureSpec) {

        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        int result = 0;

        if (specMode == MeasureSpec.AT_MOST) {

            result = specSize;
        } else if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        }
        return result;
    }


    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        setBackgroundColor(Color.TRANSPARENT);
        for (int i = 0; i < mAlphaList.size(); i++) {
            int mAlpha = Integer.parseInt(mAlphaList.get(i));
            int mStartWidth = Integer.parseInt(mStartWidthList.get(i));
            mPaint.setAlpha(mAlpha);
            int radius = mStartWidth + CIRCLE_MIN_RADIUS;
            if (radius <= mWidth / 2) {
                canvas.drawCircle(mWidth / 2, mHeight / 2, radius, mPaint);
            } else {
                continue;
            }
            if (mIsStarting && mAlpha > 0 && mStartWidth < 11 * mWidth / 28) {
                mAlphaList.set(i, (mAlpha - 1) + "");
                mStartWidthList.set(i, (mStartWidth + 1) + "");
            }
        }
        String startLength = mStartWidthList.get(mStartWidthList.size() - 1);
        if (mIsStarting && Integer.parseInt(startLength) == mWidth / 7) {
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

