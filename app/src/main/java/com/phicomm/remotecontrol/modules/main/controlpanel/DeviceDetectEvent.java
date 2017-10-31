package com.phicomm.remotecontrol.modules.main.controlpanel;

/**
 * Created by kang.sun on 2017/10/31.
 */

public class DeviceDetectEvent {
    private boolean mIsOnline;

    public DeviceDetectEvent(boolean isOnline) {
        this.mIsOnline = isOnline;
    }

    public boolean getTargetState() {
        return mIsOnline;
    }
}
