package com.phicomm.remotecontrol.httpclient;

import rx.Subscriber;

public abstract class PhiCallBack<M> extends Subscriber<M> {

    public abstract void onSuccess(M model);

    public abstract void onFailure(String msg);

    public abstract void onFinish();


    @Override
    public void onError(Throwable e) {
        e.printStackTrace();

        onFinish();
    }

    @Override
    public void onNext(M model) {
        onSuccess(model);

    }

    @Override
    public void onCompleted() {
        onFinish();
    }
}