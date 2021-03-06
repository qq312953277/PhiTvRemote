package com.phicomm.remotecontrol.modules.main.controlpanel;

import android.content.Context;

import com.phicomm.remotecontrol.TaskQuene;
import com.phicomm.remotecontrol.httpclient.PhiCallBack;
import com.phicomm.remotecontrol.util.DevicesUtil;
import com.phicomm.remotecontrol.util.LogUtil;

/**
 * Created by xufeng02.zhou on 2017/7/13.
 */

public class PanelPresenter implements PanelContract.Presenter {
    PanelContract.View mView;
    private Context mContext;

    public PanelPresenter() {
    }

    public PanelPresenter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void start() {
    }

    @Override
    public void stop() {
        TaskQuene.getInstance().unubscribe();
    }

    @Override
    public void setView(PanelContract.View view) {
        mView = view;
    }

    @Override
    public void sendKeyEvent(final int keyCode) {
        if (DevicesUtil.getTarget() != null) {
            TaskQuene.getInstance().sendKeyEvent(keyCode, new PhiCallBack() {
                @Override
                public void onSuccess(Object model) {
                    LogUtil.d("keyevent " + keyCode + " : SUCCESS");
                }

                @Override
                public void onFailure(String msg) {
                    LogUtil.d("keyevent " + keyCode + " : fail");
                    if (mView != null) {
                        mView.connectFail();
                    }
                }

                @Override
                public void onFinish() {
                }
            });
        }
    }

    @Override
    public void sendKeyLongClickEvent(final int keyCode) {
        TaskQuene.getInstance().sendKeyLonClickEvent(keyCode, new PhiCallBack() {
            @Override
            public void onSuccess(Object model) {
                LogUtil.d("keyevent " + keyCode + " : SUCCESS");
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
    public void sendCommand(final String cmd) {
        if (DevicesUtil.getTarget() != null) {
            TaskQuene.getInstance().sendCommand(cmd, new PhiCallBack() {
                @Override
                public void onSuccess(Object model) {
                    LogUtil.d("sendCommand " + cmd + " : SUCCESS");
                }

                @Override
                public void onFailure(String msg) {
                    LogUtil.d("sendCommand " + cmd + " : fail");
                    mView.connectFail();
                }

                @Override
                public void onFinish() {
                }
            });
        }
    }
}
