package com.phicomm.remotecontrol.modules.main.screenprojection.event;

/**
 * Created by kang.sun on 2017/11/21.
 */

public class SetSeekBarStateEvent {
    private boolean mIsSlide;

    public SetSeekBarStateEvent(boolean mIsSlide) {
        this.mIsSlide = mIsSlide;
    }

    public boolean getSeekBarState() {
        return mIsSlide;
    }
}
