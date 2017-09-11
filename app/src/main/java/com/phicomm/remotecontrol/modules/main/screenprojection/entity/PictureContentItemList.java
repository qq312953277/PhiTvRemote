package com.phicomm.remotecontrol.modules.main.screenprojection.entity;

import org.fourthline.cling.support.model.item.Item;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kang.sun on 2017/8/28.
 */
public class PictureContentItemList {
    private static PictureContentItemList INSTANCE = null;
    private List<Item> mPictureContentItemList;

    private PictureContentItemList() {
        mPictureContentItemList = new ArrayList<>();
    }

    public static PictureContentItemList getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PictureContentItemList();
        }
        return INSTANCE;
    }

    public void addPictureContentItem(Item mContentItem) {
        mPictureContentItemList.add(mContentItem);
    }

    public List<Item> getPictureContentItemList() {
        return mPictureContentItemList;
    }

    public void destroy() {
        mPictureContentItemList = null;
        INSTANCE = null;
    }
}

