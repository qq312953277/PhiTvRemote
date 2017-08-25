package com.phicomm.remotecontrol;

import com.phicomm.remotecontrol.beans.KeyEvent;
import com.phicomm.remotecontrol.beans.Status;
import com.phicomm.remotecontrol.httpclient.IRemoterService;
import com.phicomm.remotecontrol.httpclient.IdleRemoterService;
import com.phicomm.remotecontrol.httpclient.PhiCallBack;
import com.phicomm.remotecontrol.util.LogUtil;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by xufeng02.zhou on 2017/7/17.
 */

public class TaskQuene {
    private CompositeSubscription mCompositeSubscription;
    private IRemoterService mService;

    private static class SingletonHolder {
        private static TaskQuene instance = new TaskQuene();
    }

    private TaskQuene() {
        LogUtil.d("TaskQuene instance");
        if (mCompositeSubscription == null) {
            mCompositeSubscription = new CompositeSubscription();
        }

        if (mService == null) {
            mService = new IdleRemoterService();
        }
    }

    public static TaskQuene getInstance() {
        return SingletonHolder.instance;
    }

    public void setRemoterService(IRemoterService service) {
        LogUtil.d("TaskQuene setRemoterService");
        if (mCompositeSubscription.hasSubscriptions()) {
            mCompositeSubscription.clear();
        }
        mService = service;
    }

    public void addSubscription(Observable observable, Subscriber subscriber) {
        mCompositeSubscription.add(observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber));
    }

    public void getStatus(PhiCallBack<Status> callback) {
        LogUtil.d("TaskQuene sendKeyEvent");

        addSubscription(
                mService.getStatus(), callback
        );
    }

    public void doScreenshot(PhiCallBack<byte[]> callback) {
        LogUtil.d("TaskQuene sendKeyEvent");

        addSubscription(
                mService.doScreenshot(), callback
        );
    }


    public void sendKeyEvent(int keycode, PhiCallBack callback) {
        LogUtil.d("TaskQuene sendKeyEvent");
        KeyEvent event = new KeyEvent(keycode, false);
        addSubscription(
                mService.sendKeyEvent(event), callback
        );
//
//        Observable observable = mService.sendKeyEvent(event);
//        observable.subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(callback);
    }

    public void sendKeyLonClickEvent(int keycode, PhiCallBack callback) {
        LogUtil.d("TaskQuene sendKeyLonClickEvent");
        KeyEvent event = new KeyEvent(keycode, true);
        addSubscription(
                mService.sendKeyEvent(event), callback
        );
    }

    public void sendCommand(String commad, PhiCallBack callback) {
        LogUtil.d("TaskQuene sendCommand");
        addSubscription(
                mService.sendCommand(commad), callback
        );
    }

    public void unubscribe() {
        if (mCompositeSubscription.hasSubscriptions()) {
            mCompositeSubscription.clear();
        }
    }
}
