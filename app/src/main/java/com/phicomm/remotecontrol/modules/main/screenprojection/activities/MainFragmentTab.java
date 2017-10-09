package com.phicomm.remotecontrol.modules.main.screenprojection.activities;

import com.phicomm.remotecontrol.base.BaseFragment;
import com.phicomm.remotecontrol.modules.main.screenprojection.fragments.PictureFragment;
import com.phicomm.remotecontrol.modules.main.screenprojection.fragments.VideoFragment;

/**
 * Created by kang.sun on 2017/10/8.
 */

public enum MainFragmentTab {
    PICTURE(0, PictureFragment.class),
    VIDEO(1, VideoFragment.class);
    public final int tabIndex;
    public final Class<? extends BaseFragment> clazz;

    MainFragmentTab(int index, Class<? extends BaseFragment> clazz) {
        this.tabIndex = index;
        this.clazz = clazz;

    }

    public static final MainFragmentTab fromTabIndex(int position) {
        for (MainFragmentTab tab : MainFragmentTab.values()) {
            if (tab.tabIndex == position) {
                return tab;
            }
        }
        return null;
    }
}
