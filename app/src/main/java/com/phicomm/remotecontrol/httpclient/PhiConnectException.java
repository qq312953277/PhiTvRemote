package com.phicomm.remotecontrol.httpclient;

/**
 * Created by xufeng02.zhou on 2017/7/18.
 */

public class PhiConnectException extends Exception {
    private int mErrorCode;

    public PhiConnectException(int code) {
        super();

        mErrorCode = code;
    }

    public int getErrorCode() {
        return mErrorCode;
    }
}
