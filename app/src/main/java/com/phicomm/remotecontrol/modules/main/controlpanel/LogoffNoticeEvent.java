package com.phicomm.remotecontrol.modules.main.controlpanel;

/**
 * Created by kang.sun on 2017/10/30.
 */

public class LogoffNoticeEvent {
    private boolean mIsOffline;

    public LogoffNoticeEvent(boolean isOffline) {
        this.mIsOffline = isOffline;
    }

    public boolean getDeviceState() {
        return mIsOffline;
    }
}
