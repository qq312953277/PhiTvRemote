package com.phicomm.remotecontrol.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.phicomm.remotecontrol.BuildConfig;
import com.phicomm.remotecontrol.R;
import com.phicomm.remotecontrol.base.BaseActivity;
import com.phicomm.remotecontrol.constant.Configs;
import com.phicomm.remotecontrol.httpclient.PhiCallBack;
import com.phicomm.remotecontrol.util.CommonUtils;
import com.phicomm.remotecontrol.util.LogUtil;
import com.phicomm.remotecontrol.util.StatusBarUtils;

import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

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
