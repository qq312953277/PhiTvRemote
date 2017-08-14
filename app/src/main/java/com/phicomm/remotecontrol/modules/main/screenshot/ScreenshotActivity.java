package com.phicomm.remotecontrol.modules.main.screenshot;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.phicomm.remotecontrol.R;
import com.phicomm.remotecontrol.base.BaseActivity;
import com.phicomm.remotecontrol.util.CommonUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by hao04.wu on 2017/8/9.
 */

public class ScreenshotActivity extends BaseActivity implements ScreenshotView {

    private ScreenshotPresenter mScreenshotPresenter;
    private final static String TAG = "ScreenshotActivity";
    @BindView(R.id.iv_show)
    ImageView mImageShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_screenshot);
        init();
    }

    private void init() {
        mScreenshotPresenter = new ScreenshotPresenterImp(this, this);
    }

    @OnClick({R.id.btn_screenshot, R.id.btn_save})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_screenshot:
                mScreenshotPresenter.doScreenshot();
                break;
            case R.id.btn_save:
                mScreenshotPresenter.savePicture();
                break;
        }
    }


    @Override
    public void showPicture(Drawable drawable) {
        mImageShow.setImageDrawable(drawable);
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
        CommonUtils.showToastBottom((String) message);
    }

}
