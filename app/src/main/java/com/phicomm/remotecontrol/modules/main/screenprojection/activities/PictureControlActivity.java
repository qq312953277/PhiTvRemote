package com.phicomm.remotecontrol.modules.main.screenprojection.activities;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.phicomm.remotecontrol.R;
import com.phicomm.remotecontrol.base.BaseActivity;
import com.phicomm.remotecontrol.base.BaseApplication;
import com.phicomm.remotecontrol.constant.KeyCode;
import com.phicomm.remotecontrol.modules.main.controlpanel.PanelContract;
import com.phicomm.remotecontrol.modules.main.controlpanel.PanelPresenter;
import com.phicomm.remotecontrol.modules.main.screenprojection.presenter.PictureControlPresenter;
import com.phicomm.remotecontrol.modules.main.screenprojection.presenter.PictureControlPresenterImpl;
import com.phicomm.remotecontrol.util.CommonUtils;
import com.phicomm.remotecontrol.util.SettingUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnTouch;

/**
 * Created by kang.sun on 2017/8/28.
 */
public class PictureControlActivity extends BaseActivity implements PictureControlView {
    public static final int SLIDING_DISTANCE = 50;
    private PictureControlPresenter mPictureControlPresenter;
    private GestureDetector mGestureDetector;
    private PanelContract.Presenter mPanelPresenter;

    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @BindView(R.id.iv_back)
    ImageView mBack;

    @BindView(R.id.iv_showpicture)
    ImageView mImageView;

    @BindView(R.id.rl_title)
    RelativeLayout mRlTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showpicture);
        ButterKnife.bind(this);
        initTitleView();
        init();
        mPictureControlPresenter.showPicture(mImageView);
        showDialog();
    }

    private void init() {
        mPictureControlPresenter = new PictureControlPresenterImpl(this, (BaseApplication) getApplication());
        mGestureDetector = new GestureDetector(new MyGestureListener());
        mPanelPresenter = new PanelPresenter(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void showMessage(Object message) {
        CommonUtils.showToastBottom((String) message);
    }

    @Override
    public void onSuccess(Object message) {
    }

    @Override
    public void onFailure(Object message) {
    }

    @Override
    public void showDialog() {
    }

    @Override
    public void dismissDialog() {
    }

    @Override
    public void showPicture(Bitmap bitmap) {
        mImageView.setImageBitmap(bitmap);
    }

    @Override
    public void setTittle(String tittle) {
        mTvTitle.setText(tittle);
    }

    private void initTitleView() {
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingUtil.checkVibrate();
                mPanelPresenter.sendKeyEvent(KeyCode.BACK);
                finish();
            }
        });
    }

    @OnTouch(R.id.iv_showpicture)
    boolean imageView(MotionEvent event) {
        mGestureDetector.onTouchEvent(event);
        return true;
    }

    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (e1.getX() - e2.getX() > SLIDING_DISTANCE) {
                mPictureControlPresenter.showNextPicture(mImageView);
            } else if (e2.getX() - e1.getX() > SLIDING_DISTANCE) {
                mPictureControlPresenter.showPrePicture(mImageView);
            }
            return super.onFling(e1, e2, velocityX, velocityY);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            mPanelPresenter.sendKeyEvent(KeyCode.BACK);
        }
        return super.onKeyDown(keyCode, event);
    }
}


