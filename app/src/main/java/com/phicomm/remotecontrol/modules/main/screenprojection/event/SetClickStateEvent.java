package com.phicomm.remotecontrol.modules.main.screenprojection.event;

/**
 * Created by kang.sun on 2017/10/25.
 */

public class SetClickStateEvent {
    private boolean mIsClickable;

    public SetClickStateEvent(boolean isClickable) {
        this.mIsClickable = isClickable;
    }

    public boolean getClickState() {
        return mIsClickable;
    }
}
