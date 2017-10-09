package com.phicomm.remotecontrol.modules.main.screenprojection.activities;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.phicomm.remotecontrol.R;
import com.phicomm.remotecontrol.base.BaseActivity;
import com.phicomm.remotecontrol.base.BaseApplication;
import com.phicomm.remotecontrol.modules.main.screenprojection.presenter.PictureControlPresenter;
import com.phicomm.remotecontrol.modules.main.screenprojection.presenter.PictureControlPresenterImpl;
import com.phicomm.remotecontrol.util.CommonUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnTouch;

import static com.phicomm.remotecontrol.constant.PhiConstants.TITLE_BAR_HEIGHT_DP;

/**
 * Created by kang.sun on 2017/8/28.
 */
public class PictureControlActivity extends BaseActivity implements PictureControlView {
    public static final int SLIDING_DISTANCE = 50;
    public static final int TITTLE_LIMIT_LENGTH = 20;
    public static final int TITTLE_FRONT_LENGTH = 10;
    public static final int TITTLE_BACK_LENGTH = 4;
    private PictureControlPresenter mPictureControlPresenter;
    private GestureDetector mGestureDetector;
    @BindView(R.id.rl_title)
    RelativeLayout mRlTitle;

    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @BindView(R.id.iv_back)
    ImageView mBack;
    @BindView(R.id.iv_showpicture)
    ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showpicture);
        ButterKnife.bind(this);
        initTitleView();
        init();
        mPictureControlPresenter.showPicture();
        showDialog();
    }

    private void init() {
        mPictureControlPresenter = new PictureControlPresenterImpl(this, (BaseApplication) getApplication());
        mGestureDetector = new GestureDetector(new MyGestureListener());
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
        if (tittle.length() <= TITTLE_LIMIT_LENGTH) {
            mTvTitle.setText(tittle);
        } else {
            String mShortTvTitle = tittle.substring(0, TITTLE_FRONT_LENGTH) + "..." + tittle.substring(tittle.length() - TITTLE_BACK_LENGTH, tittle.length());
            mTvTitle.setText(mShortTvTitle);
        }
    }

    private void initTitleView() {
        setMarginForStatusBar(mRlTitle, TITLE_BAR_HEIGHT_DP);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                mPictureControlPresenter.showNextPicture();
            } else if (e2.getX() - e1.getX() > SLIDING_DISTANCE) {
                mPictureControlPresenter.showPrePicture();
            }
            return super.onFling(e1, e2, velocityX, velocityY);
        }
    }
}


