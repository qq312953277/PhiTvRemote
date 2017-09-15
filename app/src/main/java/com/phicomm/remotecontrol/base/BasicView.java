package com.phicomm.remotecontrol.base;

/**
 * Created by hao04.wu on 2017/8/7.
 */

public interface BasicView<T> {
    void showMessage(T message);

    void onSuccess(T message);

    void onFailure(T message);
}
