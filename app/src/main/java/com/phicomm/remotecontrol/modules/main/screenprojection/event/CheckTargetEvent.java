package com.phicomm.remotecontrol.modules.main.screenprojection.event;

/**
 * Created by kang.sun on 2017/10/28.
 */

public class CheckTargetEvent {
    private boolean mIsOnline;

    public CheckTargetEvent(boolean isOnline) {
        this.mIsOnline = isOnline;
    }

    public boolean getTargetState() {
        return mIsOnline;
    }
}
