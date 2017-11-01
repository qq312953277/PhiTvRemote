package com.phicomm.remotecontrol.modules.main.controlpanel;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.phicomm.remotecontrol.R;
import com.phicomm.remotecontrol.base.BaseFragment;
import com.phicomm.remotecontrol.constant.Commands;
import com.phicomm.remotecontrol.constant.KeyCode;
import com.phicomm.remotecontrol.constant.PhiConstants;
import com.phicomm.remotecontrol.event.GestureDelectorSimlpeListener;
import com.phicomm.remotecontrol.util.CommonUtils;
import com.phicomm.remotecontrol.util.DevicesUtil;
import com.phicomm.remotecontrol.util.SettingUtil;

import org.greenrobot.eventbus.EventBus;

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
    RelativeLayout mTouchArea;

    @BindView(R.id.scrollView)
    ScrollView mScrollView;

    @BindView(R.id.iv_slide_left_hint)
    ImageView mIvSlideLeft;

    @BindView(R.id.iv_slide_right_hint)
    ImageView mIvSlideRight;

    @BindView(R.id.iv_slide_up_hint)
    ImageView mIvSlideUp;

    @BindView(R.id.iv_slide_down_hint)
    ImageView mIvSlideDown;

    @BindView(R.id.iv_slide_left_arrow)
    ImageView mIvLfetArrow;

    @BindView(R.id.iv_slide_right_arrow)
    ImageView mIvRightArrow;

    @BindView(R.id.iv_slide_up_arrow)
    ImageView mIvUpArrow;

    @BindView(R.id.iv_slide_down_arrow)
    ImageView mIvDownArrow;


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
                        SettingUtil.checkVibrate();
                        hideSlideHint();
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
    @OnClick({R.id.btn_vol_up, R.id.btn_vol_down, R.id.btn_home, R.id.btn_back, R.id.btn_setting, R.id.btn_menu, R.id.btn_power})
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.btn_vol_up:
                mPresenter.sendKeyEvent(KeyCode.VOL_UP);
                break;
            case R.id.btn_vol_down:
                mPresenter.sendKeyEvent(KeyCode.VOL_DOWN);
                break;
            case R.id.btn_home:
                mPresenter.sendKeyEvent(KeyCode.HOME);
                break;
            case R.id.btn_back:
                mPresenter.sendKeyEvent(KeyCode.BACK);
                break;
            case R.id.btn_setting:
                mPresenter.sendCommand(Commands.OPEN_SETTING);
                break;
            case R.id.btn_menu:
                mPresenter.sendKeyEvent(KeyCode.MENU);
                break;
            case R.id.btn_power:
                mPresenter.sendKeyEvent(KeyCode.POWER);
            default:
                break;
        }
    }

    @Override
    public void showDirection(int position) {
        switch (position) {
            case PhiConstants.SLIDE_RIGHT:
                mIvRightArrow.setVisibility(View.VISIBLE);
                hideArrow(mIvRightArrow);
                mPresenter.sendKeyEvent(KeyCode.RIGHT);
                break;
            case PhiConstants.SLIDE_LEFT:
                mIvLfetArrow.setVisibility(View.VISIBLE);
                hideArrow(mIvLfetArrow);
                mPresenter.sendKeyEvent(KeyCode.LEFT);
                break;
            case PhiConstants.SLIDE_DOWN:
                mIvDownArrow.setVisibility(View.VISIBLE);
                hideArrow(mIvDownArrow);
                mPresenter.sendKeyEvent(KeyCode.DOWN);
                break;
            case PhiConstants.SLIDE_UP:
                mIvUpArrow.setVisibility(View.VISIBLE);
                hideArrow(mIvUpArrow);
                mPresenter.sendKeyEvent(KeyCode.UP);
                break;
            default:
                showSlideHint();
                CommonUtils.showShortToast(getString(R.string.click_force_hint));
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

    @Override
    public void connectFail() {
        EventBus.getDefault().post(new LogoffNoticeEvent(true));
        DevicesUtil.setTarget(null);
        CommonUtils.showShortToast(getString(R.string.unable_to_connect_device));
    }

    private void showSlideHint() {
        mIvSlideUp.setVisibility(View.VISIBLE);
        mIvSlideDown.setVisibility(View.VISIBLE);
        mIvSlideLeft.setVisibility(View.VISIBLE);
        mIvSlideRight.setVisibility(View.VISIBLE);
    }

    private void hideSlideHint() {
        mIvSlideUp.setVisibility(View.GONE);
        mIvSlideDown.setVisibility(View.GONE);
        mIvSlideLeft.setVisibility(View.GONE);
        mIvSlideRight.setVisibility(View.GONE);
    }

    private void hideArrow(final View view) {
        //缩放动画
        Animation scaleAnimation = new ScaleAnimation(1.0f, 0f, 1.0f, 0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(1000);
        scaleAnimation.setFillAfter(true);
        view.startAnimation(scaleAnimation);
        scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.GONE);
                showSlideHint();
            }
        });
//         渐变消失动画
//        AlphaAnimation disappearAnimation = new AlphaAnimation(1, 0);
//        disappearAnimation.setDuration(500);
//        view.startAnimation(disappearAnimation);
//        disappearAnimation.setAnimationListener(new Animation.AnimationListener() {
//
//            @Override
//            public void onAnimationStart(Animation animation) {}
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {}
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                view.setVisibility(View.GONE);
//            }
//        });
    }
}
