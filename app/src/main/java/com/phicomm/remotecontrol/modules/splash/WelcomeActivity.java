package com.phicomm.remotecontrol.modules.splash;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

import com.phicomm.remotecontrol.R;
import com.phicomm.remotecontrol.base.BaseActivity;
import com.phicomm.remotecontrol.base.BaseApplication;
import com.phicomm.remotecontrol.modules.main.controlpanel.CoreControlActivity;
import com.phicomm.remotecontrol.modules.personal.account.local.LocalDataRepository;
import com.phicomm.remotecontrol.modules.personal.account.token.TokenManager;
import com.phicomm.remotecontrol.util.CommonUtils;
import com.phicomm.remotecontrol.util.LogUtil;
import com.phicomm.remotecontrol.util.StatusBarUtils;

/**
 * Created by xufeng02.zhou on 2017/7/26.
 */

public class WelcomeActivity extends BaseActivity {

    Handler mHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcom);
        mHandler = new Handler();
        StatusBarUtils.setFullScreen(WelcomeActivity.this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        LogUtil.d("WelcomeActivity Start");
        mHandler.postDelayed(goNextActivity(), 1000);

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
                CommonUtils.startIntent(WelcomeActivity.this, CoreControlActivity.class);
                finish();
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
}
