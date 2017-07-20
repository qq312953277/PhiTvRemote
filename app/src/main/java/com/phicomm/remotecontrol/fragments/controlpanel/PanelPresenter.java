package com.phicomm.remotecontrol.fragments.controlpanel;

import com.phicomm.remotecontrol.TaskQuene;
import com.phicomm.remotecontrol.httpclient.PhiCallBack;

/**
 * Created by xufeng02.zhou on 2017/7/13.
 */

public class PanelPresenter implements PanelContract.Presenter {

    PanelContract.View mView;

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void setView(PanelContract.View view) {
        mView = view;
    }

    @Override
    public void sendKeyEvent(int keyCode) {
        TaskQuene.getInstance().sendKeyEvent(keyCode, new PhiCallBack() {
            @Override
            public void onSuccess(Object model) {

            }

            @Override
            public void onFailure(String msg) {

            }

            @Override
            public void onFinish() {

            }
        });
    }

    @Override
    public void sendKeyLongClickEvent(int keyCode) {
        TaskQuene.getInstance().sendKeyLonClickEvent(keyCode, new PhiCallBack() {
            @Override
            public void onSuccess(Object model) {

            }

            @Override
            public void onFailure(String msg) {

            }

            @Override
            public void onFinish() {

            }
        });
    }

    @Override
    public void sendCommand(String cmd) {
        TaskQuene.getInstance().sendCommand(cmd, new PhiCallBack() {
            @Override
            public void onSuccess(Object model) {

            }

            @Override
            public void onFailure(String msg) {

            }

            @Override
            public void onFinish() {

            }
        });
    }
}
