package com.phicomm.remotecontrol.modules.main.screenprojection.presenter;

import android.content.Context;
import android.os.Handler;

import com.phicomm.remotecontrol.base.BaseFragment;

/**
 * Created by kang.sun on 2017/8/21.
 */
public interface LocalMediaItemPresenter {
    void init(Context mContext, Handler mHandle, int type);

    void showItems(int type);

    void getItems(int type);

    void browserItems(int position, BaseFragment mLocalMediaItemActivity);

    void destory();

    void restore();
}
