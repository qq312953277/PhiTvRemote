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
    private boolean serverPrepared = false;

    public void prepareMediaServer(Context ctx, String serverAdd) {
        if (serverPrepared) {
            return;
        }
        // 创建添加Video容器,节点
        MediaContentDao contentDao = new MediaContentDao(ctx, serverAdd);
        ArrayList<MItem> videoItems = contentDao.getVideoItems();
        creatContainer(ContentTree.VIDEO_ID, "Videos", videoItems);
        // 创建添加image容器,节点
        ArrayList<MItem> imageItems = contentDao.getImageItems();
        creatContainer(ContentTree.IMAGE_ID, "Images", imageItems);
        serverPrepared = true;
    }

    private void creatContainer(String id, String title, ArrayList<MItem> mItems) {
        ContentNode rootNode = ContentTree.getRootNode();
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
        Container rootContainer = rootNode.getContainer();
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
