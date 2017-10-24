package com.phicomm.remotecontrol.modules.main.screenprojection.model;

import android.content.Context;

import com.phicomm.remotecontrol.modules.main.screenprojection.dao.MediaContentDao;
import com.phicomm.remotecontrol.modules.main.screenprojection.entity.ContentNode;
import com.phicomm.remotecontrol.modules.main.screenprojection.entity.ContentTree;
import com.phicomm.remotecontrol.modules.main.screenprojection.entity.MItem;
import com.phicomm.remotecontrol.modules.main.screenprojection.entity.PictureItemList;

import org.fourthline.cling.support.model.DIDLObject;
import org.fourthline.cling.support.model.WriteStatus;
import org.fourthline.cling.support.model.container.Container;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kang.sun on 2017/8/21.
 */
public class MediaContentBiz {

    public static List<PictureItemList> mPictureItemArrayList = new ArrayList<>();
    public static List<MItem> mVideoItemArrayList = new ArrayList<>();
    public static Map<String, String> mPictureMapList = new HashMap<>();
    public static Map<String, String> mVideoMapList = new HashMap<>();
    private boolean mServerPrepared = false;
    public String[] mAlbumNameList = {"Camera", "WeiXin", "Pictures", "image", "Download", "Downloads",
            "Screenshots", "media", "tmp", "clip", "PhiboxScreenshot", "bluetooth"};
    public static List<String> mPicAlbumNameList = new ArrayList<>();

    public void prepareMediaServer(Context ctx, String serverAdd) {
        if (mServerPrepared) {
            return;
        }
        mPictureItemArrayList.clear();
        mVideoItemArrayList.clear();
        MediaContentDao contentDao = new MediaContentDao(ctx, serverAdd);
        ContentNode rootNode = ContentTree.getRootNode();
        Container rootContainer = rootNode.getContainer();
        rootContainer.getContainers().clear();
        ArrayList<MItem> mAllImageItems = contentDao.getImageItems();
        if (mAllImageItems.size() > 0) {
            mPicAlbumNameList.add("All");
            creatContainer(rootNode, rootContainer, "All", "All", mAllImageItems);
            PictureItemList mAllPictureItemList = new PictureItemList();
            for (MItem mMItem : mAllImageItems) {
                mAllPictureItemList.addPictureItem(mMItem);
                mPictureMapList.put(mMItem.getId(), mMItem.getFilePath());
            }
            mPictureItemArrayList.add(mAllPictureItemList);
        }
        for (int i = 0; i < mAlbumNameList.length; i++) {
            ArrayList<MItem> imageItems = contentDao.getImageItems(mAlbumNameList[i]);
            if (imageItems.size() > 0) {
                mPicAlbumNameList.add(mAlbumNameList[i]);
                creatContainer(rootNode, rootContainer, mAlbumNameList[i], mAlbumNameList[i], imageItems);
                PictureItemList mPictureItemList = new PictureItemList();
                for (MItem mMItem : imageItems) {
                    mPictureItemList.addPictureItem(mMItem);
                }
                mPictureItemArrayList.add(mPictureItemList);
            }
        }
        ArrayList<MItem> videoItems = contentDao.getVideoItems();
        creatContainer(rootNode, rootContainer, ContentTree.VIDEO_ID, "Videos", videoItems);
        for (MItem mMItem : videoItems) {
            mVideoMapList.put(mMItem.getId(), mMItem.getFilePath());
            mVideoItemArrayList.add(mMItem);
        }
        mServerPrepared = true;
    }

    private void creatContainer(ContentNode rootNode, Container rootContainer, String id, String title, ArrayList<MItem> mItems) {
        Container container = new Container();
        container.setClazz(new DIDLObject.Class("object.container"));
        container.setId(id);
        container.setParentID(ContentTree.ROOT_ID);
        container.setTitle(title);
        container.setCreator("GNaP MediaServer");
        container.setRestricted(true);
        container.setWriteStatus(WriteStatus.NOT_WRITABLE);
        container.setChildCount(0);
        rootContainer.addContainer(container);
        rootContainer.setChildCount(rootContainer.getChildCount() + 1);
        ContentTree.addNode(id, new ContentNode(id, container));
        for (MItem mItem : mItems) {
            container.addItem(mItem);
            container.setChildCount(container.getChildCount() + 1);
            ContentTree.addNode(mItem.getId(), new ContentNode(mItem.getId(), mItem, mItem.getFilePath()));
        }
    }
}
