package com.phicomm.remotecontrol.event;

import android.view.GestureDetector;
import android.view.MotionEvent;

import com.phicomm.remotecontrol.constant.PhiConstants;
import com.phicomm.remotecontrol.util.CommonUtils;

/**
 * Created by hao04.wu on 2017/8/8.
 */

public class GestureDelectorSimlpeListener extends GestureDetector.SimpleOnGestureListener {

    private GestureCallBackListener mGestureCallBackListener;
    private final int mSlideDistance = 200;
    @Override
    public boolean onDown(MotionEvent e) {
        return true;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                           float velocityY) {
        drawTouch(e1.getX(), e1.getY(), e2.getX(), e2.getY());
        return true;
    }


    /**
     * 手势判断
     *
     * @param fromX
     * @param fromY
     * @param toX
     * @param toY
     * @return
     */
    private int drawTouch(float fromX, float fromY, float toX, float toY) {

        int direction = -1;
        //水平滑动
        if (toX - fromX > mSlideDistance) {
            direction = PhiConstants.SLIDE_RIGHT;
        } else if (fromX - toX > mSlideDistance) {
            direction = PhiConstants.SLIDE_LEFT;

        } else if (toY - fromY > mSlideDistance) {
            direction = PhiConstants.SLIDE_DOWN;

        } else if (fromY - toY > mSlideDistance) {
            direction = PhiConstants.SLIDE_UP;

        }
        if (mGestureCallBackListener != null) {
            mGestureCallBackListener.showDirection(direction);
        }
        return direction;
    }

    public void setmGestureCallBackListener(GestureCallBackListener mGestureCallBackListener) {
        this.mGestureCallBackListener = mGestureCallBackListener;
    }

    public interface GestureCallBackListener {
        void showDirection(int direction);
    }
}
