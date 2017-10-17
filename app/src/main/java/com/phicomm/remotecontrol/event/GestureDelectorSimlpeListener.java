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
     * 当用户在屏幕上滚动时触发，在onFling前执行
     *
     * @param downEvent 按下时的事件，可获取按下时的坐标等
     * @param moveEvent 移动的事件，获取移动后的坐标
     * @param distanceX
     * @param distanceY
     * @return
     */
    @Override
    public boolean onScroll(MotionEvent downEvent, MotionEvent moveEvent, float distanceX, float distanceY) {
        int direction = -1;

        if (Math.abs(distanceX) > SLIDE_DISTANCE) {
            if (distanceX > 0) {
                direction = PhiConstants.SLIDE_LEFT;
            } else {
                direction = PhiConstants.SLIDE_RIGHT;
            }

        } else if (Math.abs(distanceY) > SLIDE_DISTANCE) {
            if (distanceY > 0) {
                direction = PhiConstants.SLIDE_UP;
            } else {
                direction = PhiConstants.SLIDE_DOWN;
            }
        }
        if (mGestureCallBackListener != null) {
            mGestureCallBackListener.showArrowDirection(direction);
        }
        return false;
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
     * 当用户在触摸屏上按下，而且还未移动，松开后触发该方法
     *
     * @param e
     */
    @Override
    public void onShowPress(MotionEvent e) {
        if (mGestureCallBackListener != null) {
            mGestureCallBackListener.showDirection(-1);
        }
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

        void showArrowDirection(int direction);
    }
}
