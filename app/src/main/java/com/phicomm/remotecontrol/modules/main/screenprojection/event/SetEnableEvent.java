package com.phicomm.remotecontrol.modules.main.screenprojection.event;

/**
 * Created by kang.sun on 2017/10/25.
 */

public class SetEnableEvent {
    private boolean isEnable;

    public SetEnableEvent(boolean isEnable) {
        this.isEnable = isEnable;
    }

    public boolean getEnable() {
        return isEnable;
    }
}
