package com.phicomm.remotecontrol.modules.main.screenprojection.model;

import android.content.Context;

import com.phicomm.remotecontrol.modules.main.screenprojection.dao.MediaContentDao;
import com.phicomm.remotecontrol.modules.main.screenprojection.entity.ContentNode;
import com.phicomm.remotecontrol.modules.main.screenprojection.entity.ContentTree;
import com.phicomm.remotecontrol.modules.main.screenprojection.entity.MItem;

import org.fourthline.cling.support.model.DIDLObject;
import org.fourthline.cling.support.model.WriteStatus;
import org.fourthline.cling.support.model.container.Container;

import java.util.ArrayList;

/**
 * Created by kang.sun on 2017/8/21.
 */
public class MediaContentBiz {
    private boolean mServerPrepared = false;
    String[] mAlbumNameList = {"Camera", "WeiXin", "Pictures", "image", "Download", "Downloads", "Screenshots", "media", "tmp", "clip"};

    public void prepareMediaServer(Context ctx, String serverAdd, int type) {
        if (mServerPrepared) {
            return;
        }
        MediaContentDao contentDao = new MediaContentDao(ctx, serverAdd);
        ContentNode rootNode = ContentTree.getRootNode();
        Container rootContainer = rootNode.getContainer();
        rootContainer.getContainers().clear();
        if (type == 0) {
            // 创建添加image容器,节点
            ArrayList<MItem> mAllImageItems = contentDao.getImageItems();
            creatContainer(rootNode, rootContainer, "All", "All", mAllImageItems);
            for (int i = 0; i < mAlbumNameList.length; i++) {
                ArrayList<MItem> imageItems = contentDao.getImageItems(mAlbumNameList[i]);
                if (imageItems.size() > 0) {
                    creatContainer(rootNode, rootContainer, mAlbumNameList[i], mAlbumNameList[i], imageItems);
                }
            }
        } else {
            // 创建添加Video容器,节点
            ArrayList<MItem> videoItems = contentDao.getVideoItems();
            creatContainer(rootNode, rootContainer, ContentTree.VIDEO_ID, "Videos", videoItems);
        }
        mServerPrepared = true;
    }

    private void creatContainer(ContentNode rootNode, Container rootContainer, String id, String title, ArrayList<MItem> mItems) {
        // 创建容器
        Container container = new Container();
        container.setClazz(new DIDLObject.Class("object.container"));
        container.setId(id);
        container.setParentID(ContentTree.ROOT_ID);
        container.setTitle(title);
        container.setCreator("GNaP MediaServer");
        container.setRestricted(true);
        container.setWriteStatus(WriteStatus.NOT_WRITABLE);
        container.setChildCount(0);
        // 添加video节点
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
