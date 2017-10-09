package com.phicomm.remotecontrol.modules.main.screenprojection.activities;

import android.graphics.Bitmap;

import com.phicomm.remotecontrol.base.BasicView;

/**
 * Created by kang.sun on 2017/8/31.
 */
public interface PictureControlView extends BasicView {
    void showDialog();

    void dismissDialog();

    void showPicture(Bitmap bitmap);

    void setTittle(String tittle);
}
