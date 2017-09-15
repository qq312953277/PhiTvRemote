package com.phicomm.remotecontrol.modules.main.screenshot;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.phicomm.remotecontrol.R;
import com.phicomm.remotecontrol.RemoteBoxDevice;
import com.phicomm.remotecontrol.base.BaseActivity;
import com.phicomm.remotecontrol.base.BaseApplication;
import com.phicomm.remotecontrol.constant.PhiConstants;
import com.phicomm.remotecontrol.modules.devices.searchdevices.DeviceDiscoveryActivity;
import com.phicomm.remotecontrol.util.CommonUtils;
import com.phicomm.remotecontrol.util.DevicesUtil;
import com.phicomm.remotecontrol.util.NetworkManagerUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by hao04.wu on 2017/8/9.
 */

public class ScreenshotActivity extends BaseActivity implements ScreenshotView {


    private final static String TAG = "ScreenshotActivity";
    @BindView(R.id.iv_show)
    ImageView mImageShow;
    @BindView(R.id.btn_screenshot)
    ImageButton mScreenshot;

    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @BindView(R.id.iv_right)
    ImageView mHelper;

    private RemoteBoxDevice mTarget;
    private ScreenshotPresenter mScreenshotPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_screenshot);
        init();
    }

    private void init() {
        mScreenshotPresenter = new ScreenshotPresenterImp(this, this);
        mHelper.setVisibility(View.VISIBLE);
        mHelper.setImageResource(R.drawable.help_icon);
        mHelper.setClickable(true);

        mTarget = DevicesUtil.getTarget();

        if (mTarget == null || !NetworkManagerUtils.isCurrWifiAvailable(BaseApplication.getContext())) {
            mImageShow.setImageResource(R.drawable.tv_not_connected);
            mTvTitle.setText(getString(R.string.screenshot_title_not_connnect));
        } else {
            mTvTitle.setText(mTarget.getName());
            mImageShow.setImageResource(R.drawable.tv_connected);
        }
    }

    @Override
    @OnClick({R.id.btn_screenshot, R.id.iv_show, R.id.iv_right, R.id.iv_back})
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.btn_screenshot:
                if (mTarget == null || !NetworkManagerUtils.isCurrWifiAvailable(BaseApplication.getContext())) {
                    toSearchActivity();
                } else {
                    mScreenshot.setImageResource(R.drawable.takephoto_off);
                    mScreenshotPresenter.doScreenshot();
                }
                break;
            case R.id.iv_show:
                if (mTarget == null || !NetworkManagerUtils.isCurrWifiAvailable(BaseApplication.getContext())) {
                    toSearchActivity();
                }
                break;
            case R.id.iv_right:
                showHelpDialog().show();
                break;
            case R.id.iv_back:
                finish();
                break;
        }
    }

    private void toSearchActivity() {
        Intent intent = new Intent(this, DeviceDiscoveryActivity.class);
        intent.putExtra(PhiConstants.ACTION_BAR_NAME, getString(R.string.unable_to_connect_device));
        startActivity(intent);
        finish();
    }

    private AlertDialog showHelpDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.dialog);
        builder.setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setMessage(R.string.photo_help_tips)
                .setCancelable(false); //点击框外不取消
        return builder.create();
    }

    @Override
    public void showPicture(Drawable drawable) {
        mImageShow.setImageDrawable(drawable);
        mScreenshot.setImageResource(R.drawable.takephoto_on);
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
