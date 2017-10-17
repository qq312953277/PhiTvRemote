package com.phicomm.remotecontrol.modules.main.screenprojection.activities;

import com.phicomm.remotecontrol.base.BasicView;
import com.phicomm.remotecontrol.modules.main.screenprojection.adapter.GeneralAdapter;
import com.phicomm.remotecontrol.modules.main.screenprojection.entity.ContentItem;

/**
 * Created by kang.sun on 2017/8/31.
 */
public interface LocalMediaItemView extends BasicView {
    void showItems(int type, GeneralAdapter<ContentItem> mContentAdapter);

    void setAlbumTittle(String tittle);
}
