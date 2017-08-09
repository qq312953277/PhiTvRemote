package com.phicomm.remotecontrol.activities;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.phicomm.remotecontrol.BuildConfig;
import com.phicomm.remotecontrol.R;
import com.phicomm.remotecontrol.base.BaseActivity;
import com.phicomm.remotecontrol.util.CommonUtils;
import com.phicomm.remotecontrol.util.LogUtil;
import com.phicomm.remotecontrol.util.StatusBarUtils;

/**
 * Created by xufeng02.zhou on 2017/7/26.
 */

public class WelcomeActivity extends BaseActivity {

    Handler mHandler;
    TextView mInfoView;
    TextView mOutlineView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcom);
        mHandler = new Handler();
        mInfoView = (TextView) findViewById(R.id.tv_info);
        mInfoView.setText("Version Name:" + BuildConfig.VERSION_NAME);
        mOutlineView = (TextView) findViewById(R.id.tv_outtime);
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
                CommonUtils.startIntent(WelcomeActivity.this, CoreControlActivity.class);
                finish();
            }
        };

        return runnable;
    }
}
