package com.phicomm.remotecontrol.modules.main.screenprojection.fragments;

import android.support.v4.app.Fragment;

import com.phicomm.remotecontrol.R;
import com.phicomm.remotecontrol.base.BaseApplication;

/**
 * Created by yong04.zhou on 2017/9/19.
 */

public enum MainFragmentTab {
    PICTURE(0, PictureFragment.class, BaseApplication.getContext().getString(R.string.photo)),
    VIDEO(1, VideoFragment.class, BaseApplication.getContext().getString(R.string.video));

    public final int mTabIndex;
    public final Class<? extends Fragment> mClazz;
    public final String mRes;

    MainFragmentTab(int index, Class<? extends Fragment> clazz, String res) {
        this.mTabIndex = index;
        this.mClazz = clazz;
        this.mRes = res;
    }

}
