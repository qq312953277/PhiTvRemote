package com.phicomm.remotecontrol.modules.main.screenprojection.activities;

import com.phicomm.remotecontrol.modules.main.screenprojection.adapter.GeneralAdapter;
import com.phicomm.remotecontrol.modules.main.screenprojection.entity.ContentItem;
import com.phicomm.remotecontrol.modules.main.screenprojection.presenter.LocalMediaItemPresenter;

/**
 * Created by kang.sun on 2017/9/28.
 */

public class PictureEvent {
    public GeneralAdapter<ContentItem> mItems;
    public LocalMediaItemPresenter mLocalMediaItemPresenter;
    public int mType;

    PictureEvent(int type, GeneralAdapter<ContentItem> items,LocalMediaItemPresenter localMediaItemPresenter) {
        this.mItems = items;
        this.mType = type;
        this.mLocalMediaItemPresenter = localMediaItemPresenter;
    }
}
