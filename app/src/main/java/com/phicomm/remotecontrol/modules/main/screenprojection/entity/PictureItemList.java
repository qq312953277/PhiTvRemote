package com.phicomm.remotecontrol.modules.main.screenprojection.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kang.sun on 2017/10/9.
 */

public class PictureItemList {

    private List<MItem> mPictureItemList;

    public PictureItemList() {
        mPictureItemList = new ArrayList<>();
    }

    public void addPictureItem(MItem mContentItem) {
        mPictureItemList.add(mContentItem);
    }

    public List<MItem> getPictureItemList() {
        return mPictureItemList;
    }

    public void destroy() {
        mPictureItemList = null;
    }
}
