package com.phicomm.remotecontrol.httpclient;

import com.phicomm.remotecontrol.constant.ErrorCode;

import rx.Subscriber;

public abstract class PhiCallBack<M> extends Subscriber<M> {

    public abstract void onSuccess(M model);

    public abstract void onFailure(String msg);

    public abstract void onFinish();


    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        if(e instanceof PhiConnectException){
            PhiConnectException exception =  (PhiConnectException)e;
            if(exception.getErrorCode() == ErrorCode.NO_DEVICE) {
                onFailure("no device assigned");
            }
            onFailure("fail");
        }else{
            onFailure("fail");
        }

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