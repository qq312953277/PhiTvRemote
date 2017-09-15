package com.phicomm.remotecontrol.modules.main.controlpanel;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.phicomm.remotecontrol.R;
import com.phicomm.remotecontrol.base.BaseFragment;
import com.phicomm.remotecontrol.constant.KeyCode;
import com.phicomm.remotecontrol.constant.PhiConstants;
import com.phicomm.remotecontrol.event.GestureDelectorSimlpeListener;
import com.phicomm.remotecontrol.util.CommonUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by xufeng02.zhou on 2017/7/13.
 */

public class TouchPanelFragment extends BaseFragment implements PanelContract.View, GestureDelectorSimlpeListener.GestureCallBackListener, android.view.View.OnClickListener {
    private GestureDetector mGestureDetector;
    PanelContract.Presenter mPresenter;
    private Toast mToast;
    @BindView(R.id.ll_touch_area)
    LinearLayout mTouchArea;
    @BindView(R.id.scrollView)
    ScrollView mScrollView;


    public static TouchPanelFragment newInstance() {
        return new TouchPanelFragment();
    }

    public TouchPanelFragment() {

    }

    @Override
    public android.view.View onCreateView(LayoutInflater inflater, ViewGroup container,
                                          Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_touchpanel, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    public void init() {
        GestureDelectorSimlpeListener gestureDelectorSimlpeListener = new GestureDelectorSimlpeListener();
        gestureDelectorSimlpeListener.setmGestureCallBackListener(this);
        mGestureDetector = new GestureDetector(getContext(), gestureDelectorSimlpeListener);
        mTouchArea.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    //首先DOWN事件
                    //可以调用requestDisallowInterceptTouchEvent，让Scroll
                    //View不拦截MOVE事件
                    case MotionEvent.ACTION_DOWN:
                        mScrollView.requestDisallowInterceptTouchEvent(true);//子View告诉父容器不要拦截
                        break;
                    case MotionEvent.ACTION_MOVE:

                        break;
                    case MotionEvent.ACTION_UP:
                        //只有重写自己用到的View的onTouchEvent方法，在其ACTION_DOWN的时候，
                        // 调用父View的requestDisallowInterceptTouchEvent(true)方法设置，在ACTION_UP或者ACTION_CANCEL的时候，
                        // 调用调用父View的requestDisallowInterceptTouchEvent(false)方法重置
                        mScrollView.requestDisallowInterceptTouchEvent(false);
                }
                return mGestureDetector.onTouchEvent(event);
            }
        });

    }

    @Override
    public void setPresenter(PanelContract.Presenter presenter) {
        mPresenter = presenter;
        mPresenter.setView(this);
    }


    @Override
    @OnClick({R.id.btn_vol_up, R.id.btn_vol_down, R.id.btn_chanel_up, R.id.btn_chanel_down, R.id.btn_home, R.id.btn_back})
    public void onClick(View view) {
        super.onClick(view);

        switch (view.getId()) {
            case R.id.btn_vol_up:
                CommonUtils.showShortToast("btn_vol_up");
                mPresenter.sendKeyEvent(KeyCode.VOL_UP);
                break;
            case R.id.btn_vol_down:
                CommonUtils.showShortToast("btn_vol_down");
                mPresenter.sendKeyEvent(KeyCode.VOL_DOWN);
                break;
            case R.id.btn_chanel_up:
                CommonUtils.showShortToast("btn_chanel_up");
                mPresenter.sendKeyEvent(KeyCode.CHANEL_UP);
                break;
            case R.id.btn_chanel_down:
                CommonUtils.showShortToast("btn_chanel_down");
                mPresenter.sendKeyEvent(KeyCode.CHANEL_DOWN);
                break;
            case R.id.btn_home:
                CommonUtils.showShortToast("btn_home");
                mPresenter.sendKeyEvent(KeyCode.HOME);
                break;
            case R.id.btn_back:
                CommonUtils.showShortToast("btn_back");
                mPresenter.sendKeyEvent(KeyCode.BACK);
                break;
            default:
                break;
        }

    }

    @Override
    public void showDirection(int position) {
        switch (position) {
            case PhiConstants.SLIDE_RIGHT:
                CommonUtils.showShortToast("RIGHT");
                mPresenter.sendKeyEvent(KeyCode.RIGHT);
                break;
            case PhiConstants.SLIDE_LEFT:
                CommonUtils.showShortToast("LEFT");
                mPresenter.sendKeyEvent(KeyCode.LEFT);
                break;
            case PhiConstants.SLIDE_DOWN:
                CommonUtils.showShortToast("DOWN");
                mPresenter.sendKeyEvent(KeyCode.DOWN);
                break;
            case PhiConstants.SLIDE_UP:
                CommonUtils.showShortToast("UP");
                mPresenter.sendKeyEvent(KeyCode.UP);
                break;
            default:
                break;

        }
    }

    @Override
    public void toastMessage(String msg) {
        if (mToast == null) {
            mToast = Toast.makeText(this.getContext(), msg, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(msg);
        }
        mToast.show();
    }
}
