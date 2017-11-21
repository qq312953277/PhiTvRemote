package com.phicomm.remotecontrol.modules.main.screenprojection.event;

/**
 * Created by kang.sun on 2017/11/21.
 */

public class StopVideoEvent {
    private boolean mIsOver;

    public StopVideoEvent(boolean mIsOver) {
        this.mIsOver = mIsOver;
    }

    public boolean getPlayState() {
        return mIsOver;
    }

}
