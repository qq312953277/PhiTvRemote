package com.phicomm.remotecontrol.httpclient;

import com.phicomm.remotecontrol.beans.KeyEvent;
import com.phicomm.remotecontrol.beans.Status;

import rx.Observable;

/**
 * Created by xufeng02.zhou on 2017/7/12.
 */

public interface IRemoterService {
    // GET("v1/status")
    public Observable<Status> getStatus();

    // GET("v1/ping")
    public Observable<String> ping();

    // POST("v1/keyevent")
    public Observable<String> sendKeyEvent(final KeyEvent keyEvent);

    // POST("v1/action")
    public Observable<String> sendCommand(final String action);

    // get("v1/screenshot")
    public Observable<byte[]> doScreenshot();

}
