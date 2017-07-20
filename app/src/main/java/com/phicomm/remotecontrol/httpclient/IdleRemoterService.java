package com.phicomm.remotecontrol.httpclient;


import com.phicomm.remotecontrol.beans.KeyEvent;
import com.phicomm.remotecontrol.beans.Status;
import com.phicomm.remotecontrol.constant.ErrorCode;

import rx.Observable;
import rx.Subscriber;

/**
 * Author: allen.z
 * Date  : 2017-07-17
 * last modified: 2017-07-17
 */
public class IdleRemoterService implements IRemoterService {
    Observable mObservable;

    public IdleRemoterService() {
        mObservable = Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {
                subscriber.onError(new PhiConnectException(ErrorCode.NO_DEVICE));
            }
        });
    }

    @Override
    public Observable<Status> getStatus() {
        return mObservable;
    }

    @Override
    public Observable<String> ping() {
        return mObservable;
    }

    @Override
    public Observable<String> sendKeyEvent(KeyEvent keyEvent) {
        return mObservable;
    }

    @Override
    public Observable<String> sendCommand(String action) {
        return mObservable;
    }

    @Override
    public Observable<byte[]> doScreenshot() {
        return mObservable;
    }
}
