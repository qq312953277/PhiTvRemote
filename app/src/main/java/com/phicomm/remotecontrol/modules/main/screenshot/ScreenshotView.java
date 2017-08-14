package com.phicomm.remotecontrol.modules.main.screenshot;

import android.graphics.drawable.Drawable;

import com.phicomm.remotecontrol.base.BasicView;

/**
 * Created by hao04.wu on 2017/8/9.
 */

public interface ScreenshotView extends BasicView {
    void showPicture(Drawable drawable);
}
