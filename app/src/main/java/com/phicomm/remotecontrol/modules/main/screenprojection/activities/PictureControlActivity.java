package com.phicomm.remotecontrol.modules.main.screenprojection.activities;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.phicomm.remotecontrol.R;
import com.phicomm.remotecontrol.base.BaseActivity;
import com.phicomm.remotecontrol.base.BaseApplication;
import com.phicomm.remotecontrol.modules.main.screenprojection.presenter.PictureControlPresenter;
import com.phicomm.remotecontrol.modules.main.screenprojection.presenter.PictureControlPresenterImpl;
import com.phicomm.remotecontrol.util.CommonUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by kang.sun on 2017/8/28.
 */
public class PictureControlActivity extends BaseActivity implements PictureControlView {
    private PictureControlPresenter mPictureControlPresenter;
    @BindView(R.id.iv_showpicture)
    ImageView mImageView;
    @BindView(R.id.progressbar)
    ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showpicture);
        ButterKnife.bind(this);
        init();
        mPictureControlPresenter.showPicture();
    }

    private void init() {
        mPictureControlPresenter = new PictureControlPresenterImpl(this, (BaseApplication) getApplication());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @OnClick({R.id.bt_pre, R.id.bt_next})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_pre:
                mPictureControlPresenter.showPrePicture();
                break;
            case R.id.bt_next:
                mPictureControlPresenter.showNextPicture();
                break;
        }
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
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void dismissDialog() {
        mProgressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showPicture(Bitmap bitmap) {
        mImageView.setImageBitmap(bitmap);
    }
}


