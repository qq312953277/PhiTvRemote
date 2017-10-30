package com.phicomm.remotecontrol.modules.main.screenprojection.event;

/**
 * Created by kang.sun on 2017/10/25.
 */

public class ClickStateEvent {
    private boolean mIsClickable;

    public ClickStateEvent(boolean isClickable) {
        this.mIsClickable = isClickable;
    }

    public boolean getClickState() {
        return mIsClickable;
    }
}
