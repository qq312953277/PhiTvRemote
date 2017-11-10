package com.phicomm.remotecontrol.modules.splash;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.util.Log;

import com.phicomm.remotecontrol.R;
import com.phicomm.remotecontrol.base.BaseActivity;
import com.phicomm.remotecontrol.base.BaseApplication;
import com.phicomm.remotecontrol.modules.main.controlpanel.CoreControlActivity;
import com.phicomm.remotecontrol.modules.personal.account.local.LocalDataRepository;
import com.phicomm.remotecontrol.modules.personal.account.token.TokenManager;
import com.phicomm.remotecontrol.util.CommonUtils;
import com.phicomm.remotecontrol.util.StatusBarUtils;
import com.phicomm.widgets.alertdialog.PhiAlertDialog;
import com.tbruyelle.rxpermissions.RxPermissions;

import rx.functions.Action1;

/**
 * Created by xufeng02.zhou on 2017/7/26.
 */

public class WelcomeActivity extends BaseActivity {
    private RxPermissions rxPermissions;
    Handler mHandler;
    private static final long DELAY_Millis = 1000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcom);
        mHandler = new Handler();
        StatusBarUtils.setFullScreen(WelcomeActivity.this);
        requestAllPermissions();
    }

    private void requestAllPermissions() {
        rxPermissions = new RxPermissions(this);
        rxPermissions.request(
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        if (aBoolean) {
                            delayStartIntentAndFinish();
                        } else {
                            showPermissionAlert();
                        }
                    }
                });
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private Runnable goNextActivity() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                checkLoginStatus();

                if (Build.VERSION.SDK_INT <= 22) { // 22 -> 5.1
                    finish();
                    CommonUtils.startIntent(WelcomeActivity.this, CoreControlActivity.class);
                } else {
                    CommonUtils.startIntent(WelcomeActivity.this, CoreControlActivity.class);
                    finish();
                }

            }
        };

        return runnable;
    }

    private void checkLoginStatus() {
        //是否登录过
        if (LocalDataRepository.getInstance(BaseApplication.getContext()).isCloudLogined()) {
            if (checkAccessToken()) {
                BaseApplication.getApplication().isLogined = true;//登录过设置全局变量true,PersonalActivity判断该状态
            } else {
                BaseApplication.getApplication().isLogined = false;
            }
        } else {
            BaseApplication.getApplication().isLogined = false;
        }
    }

    public boolean checkAccessToken() {
        boolean mLoginOK = false;
        switch (TokenManager.checkAccessTokenAvailable()) {
            case TokenManager.LOGIN:
                mLoginOK = true;
                break;
            case TokenManager.LOGOUT:
                mLoginOK = false;
                break;
            default:
                mLoginOK = false;
                break;
        }
        return mLoginOK;
    }

    private void showPermissionAlert() {
        DialogInterface.OnClickListener okListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent();
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Log.d(TAG, "getPackageName(): " + BaseApplication.getContext().getPackageName());
                Uri uri = Uri.fromParts("package", BaseApplication.getContext().getPackageName(), null);
                intent.setData(uri);
                WelcomeActivity.this.startActivity(intent);
                WelcomeActivity.this.finish();
            }
        };
        new PhiAlertDialog.Builder(this)
                .setMessage(R.string.please_grant_app_permission)
                .setPositiveButton(R.string.ok, okListener)
                .setCancelable(false)
                .create()
                .show();
    }

    private void delayStartIntentAndFinish() {
        mHandler.postDelayed(goNextActivity(), DELAY_Millis);
    }
}
