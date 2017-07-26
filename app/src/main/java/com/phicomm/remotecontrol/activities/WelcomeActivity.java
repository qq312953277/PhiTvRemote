package com.phicomm.remotecontrol.activities;

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
import com.phicomm.remotecontrol.constant.Configs;
import com.phicomm.remotecontrol.httpclient.PhiCallBack;
import com.phicomm.remotecontrol.util.LogUtil;

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

public class WelcomeActivity extends AppCompatActivity {

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
    }

    @Override
    protected void onStart() {
        super.onStart();
        LogUtil.d("WelcomeActivity Start");
        if (isNetworkAvailable(this)) {
            checkVersion();
        } else {
            mHandler.postDelayed(goNextActivity(), 3000);
            mOutlineView.setText(R.string.network_invailable_msg);
            mOutlineView.setVisibility(View.VISIBLE);
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
    }

    private Runnable goNextActivity() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent();
                intent.setClass(WelcomeActivity.this, CoreControlActivity.class);
                startActivity(intent);
                finish();
            }
        };

        return runnable;
    }

    void checkVersion() {

        PhiCallBack callBack = new PhiCallBack<Long>() {

            @Override
            public void onSuccess(Long model) {
                if (model.longValue() > getOutLineTime(Configs.APP_OUTLINE)) {
                    mOutlineView.setText(R.string.timeout_msg);
                    mOutlineView.setVisibility(View.VISIBLE);
                } else {
                    mHandler.postDelayed(goNextActivity(), 100);
                }
            }

            @Override
            public void onFailure(String msg) {
                mOutlineView.setText(R.string.network_invailable_msg);
                mOutlineView.setVisibility(View.VISIBLE);
                mHandler.postDelayed(goNextActivity(), 3000);
            }

            @Override
            public void onFinish() {

            }
        };

        Observable observable = getNetTime();
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callBack);
    }

    private boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
        } else {
            NetworkInfo[] info = cm.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public Observable<Long> getNetTime() {
        return Observable.create(new Observable.OnSubscribe<Long>() {

            @Override
            public void call(final Subscriber<? super Long> subscriber) {
                LogUtil.d("call getStatus");
                URL url = null;
                try {
                    url = new URL("http://www.baidu.com");
                    //url = new URL("http://www.ntsc.ac.cn");
                    //url = new URL("http://www.bjtime.cn");
                    URLConnection uc = url.openConnection();
                    uc.setConnectTimeout(5000);
                    uc.setReadTimeout(5000);
                    uc.connect();
                    long ld = uc.getDate();
                    Date date = null;
                    SimpleDateFormat sdr = new SimpleDateFormat(Configs.SCRENSHOT_FORMAT);
                    date = new Date(ld);
                    String time = sdr.format(date);
                    LogUtil.d("Time from net:" + time);
                    subscriber.onNext(ld);
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    private long getOutLineTime(String outtime) {
        Date date = null;
        SimpleDateFormat sdr = new SimpleDateFormat(Configs.SCRENSHOT_FORMAT);
        try {
            date = sdr.parse(outtime);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
