package com.phicomm.remotecontrol.modules.main.screenprojection.presenter;

import android.content.Context;
import android.os.Handler;

/**
 * Created by kang.sun on 2017/8/21.
 */
public interface LocalMediaItemPresenter {
    void init(Context mContext, Handler mHandle);

    void getItems();

    void destory();

    void restore();
}
