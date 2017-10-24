package com.phicomm.remotecontrol.event;

import android.view.GestureDetector;
import android.view.MotionEvent;

import com.phicomm.remotecontrol.constant.PhiConstants;

/**
 * Created by hao04.wu on 2017/8/8.
 */

public class GestureDelectorSimlpeListener extends GestureDetector.SimpleOnGestureListener {

    private GestureCallBackListener mGestureCallBackListener;
    private final int SLIDE_DISTANCE = 200;

    @Override
    public boolean onDown(MotionEvent e) {
        return true;
    }

    /**
     * 当用户在触摸屏上拖过时触发该方法，action up后触发
     *
     * @param e1
     * @param e2
     * @param velocityX
     * @param velocityY
     * @return
     */
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                           float velocityY) {
        drawTouch(e1.getX(), e1.getY(), e2.getX(), e2.getY());
        return true;
    }

    /**
     * 用户在轻击时，触发此方法
     *
     * @param e
     * @return
     */
    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        if (mGestureCallBackListener != null) {
            mGestureCallBackListener.showDirection(-1);
        }
        return false;
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
        if (toX - fromX > SLIDE_DISTANCE) {
            direction = PhiConstants.SLIDE_RIGHT;
        } else if (fromX - toX > SLIDE_DISTANCE) {
            direction = PhiConstants.SLIDE_LEFT;

        } else if (toY - fromY > SLIDE_DISTANCE) {
            direction = PhiConstants.SLIDE_DOWN;

        } else if (fromY - toY > SLIDE_DISTANCE) {
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
