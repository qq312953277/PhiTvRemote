package com.phicomm.remotecontrol.beans;

import com.google.gson.annotations.SerializedName;

/**
 * Created by xufeng02.zhou on 2017/7/18.
 */

public class KeyEvent {
    @SerializedName("keycode")
    private int mKeyCode;
    @SerializedName("longclick")
    private boolean mLongClick;

    public KeyEvent(int code, boolean longclick) {
        mKeyCode = code;
        mLongClick = longclick;
    }
}
