package com.phicomm.remotecontrol.modules.main.screenshot;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import com.phicomm.widgets.alertdialog.PhiAlertDialog;

import butterknife.BindView;
import butterknife.OnClick;

import static com.phicomm.remotecontrol.constant.PhiConstants.TITLE_BAR_HEIGHT_DP;

/**
 * Created by hao04.wu on 2017/8/9.
 */

public class ScreenshotActivity extends BaseActivity implements ScreenshotView {

    public static final int DELAY_HIDEN_TIME = 5000;
    public static final int DELAY_MSG = 100;


    private final static String TAG = "ScreenshotActivity";
    @BindView(R.id.iv_show)
    ImageView mImageShow;
    @BindView(R.id.btn_screenshot)
    ImageButton mScreenshot;

    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @BindView(R.id.iv_right)
    ImageView mHelper;

    @BindView(R.id.rl_title)
    RelativeLayout mRlTitle;

    private RemoteBoxDevice mTarget;
    private ScreenshotPresenter mScreenshotPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_screenshot);
        init();

    }

    private void init() {
        setMarginForStatusBar(mRlTitle, TITLE_BAR_HEIGHT_DP);
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

    private Handler defaultPicHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DELAY_MSG:
                    mImageShow.setImageResource(R.drawable.tv_connected);
                    mScreenshot.setImageResource(R.drawable.takephoto_on);
                    mScreenshot.setEnabled(true);//可点击
                    break;
                default:
                    break;
            }
        }
    };

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
                    mScreenshot.setEnabled(false);//不可点击
                    mScreenshotPresenter.doScreenshot();
                }
                break;
            case R.id.iv_show:
                if (mTarget == null || !NetworkManagerUtils.isCurrWifiAvailable(BaseApplication.getContext())) {
                    toSearchActivity();
                }
                break;
            case R.id.iv_right:
                showHelpDialog();
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

    private void showHelpDialog() {
        PhiAlertDialog.Builder builder = new PhiAlertDialog.Builder(this);
        builder.setMessage(R.string.photo_help_tips);
        builder.setCancelable(false);//点击框外不取消
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    @Override
    public void showPicture(Drawable drawable) {
        mImageShow.setImageDrawable(drawable);
        mScreenshot.setEnabled(true);//可点击
        mScreenshot.setImageResource(R.drawable.takephoto_on);
        defaultPicHandler.sendEmptyMessageDelayed(DELAY_MSG, DELAY_HIDEN_TIME);//5s后截图图片替换成默认的
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
