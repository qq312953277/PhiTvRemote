package com.phicomm.remotecontrol.modules.main.screenprojection.presenter;

import com.phicomm.remotecontrol.ConnectManager;
import com.phicomm.remotecontrol.modules.main.screenprojection.contract.VideoContentContract;
import com.phicomm.remotecontrol.modules.main.screenprojection.event.CheckTargetEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by kang.sun on 2017/10/27.
 */

public class VideoContentPresenterImpl implements VideoContentContract.VideoContentPresenter {
    private VideoContentContract.VideoContentView mView;

    public VideoContentPresenterImpl(VideoContentContract.VideoContentView mView) {
        this.mView = mView;
    }

    @Override
    public void checkTargetState(String ipAddress) {
        mView.showCheckDialog();
        ConnectManager.getInstance().checkTarget(ipAddress, 8080, mCheckResultCallback);
    }

    private ConnectManager.CheckResultCallback mCheckResultCallback = new ConnectManager.CheckResultCallback() {
        @Override
        public void onSuccess(boolean result) {
            mView.dimissCheckDialog();
            EventBus.getDefault().post(new CheckTargetEvent(true));
        }

        @Override
        public void onFail(boolean result) {
            mView.dimissCheckDialog();
            EventBus.getDefault().post(new CheckTargetEvent(false));
        }
    };

    @Override
    public void start() {
    }

    @Override
    public void stop() {
    }
}
