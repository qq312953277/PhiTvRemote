package com.phicomm.remotecontrol.modules.main.screenprojection.presenter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.phicomm.remotecontrol.base.BaseApplication;
import com.phicomm.remotecontrol.modules.main.screenprojection.activities.LocalMediaItemView;
import com.phicomm.remotecontrol.modules.main.screenprojection.constants.DeviceDisplayListOperation;
import com.phicomm.remotecontrol.modules.main.screenprojection.entity.ContentItem;
import com.phicomm.remotecontrol.modules.main.screenprojection.entity.DeviceDisplay;
import com.phicomm.remotecontrol.modules.main.screenprojection.listener.TestRegistryListener;
import com.phicomm.remotecontrol.modules.main.screenprojection.model.ContentBrowseBiz;
import com.phicomm.remotecontrol.modules.main.screenprojection.model.MediaContentBiz;
import com.phicomm.remotecontrol.modules.main.screenprojection.model.UpnpServiceBiz;
import com.phicomm.remotecontrol.modules.main.screenprojection.server.MediaServer;
import com.phicomm.remotecontrol.modules.main.screenprojection.utils.IPUtil;

import org.fourthline.cling.support.model.item.Item;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kang.sun on 2017/8/21.
 */
public class LocalMediaItemPresenterImpl implements LocalMediaItemPresenter {
    public static Map<String, Item> mDlnaPictureMapList = new HashMap<>();
    public static Map<String, Item> mDlnaVideoMapList = new HashMap<>();
    private List<String> mDlnaPictureIdList = new ArrayList<>();
    private List<String> mDlnaVideoIdList = new ArrayList<>();
    private Context mContext;
    private LocalMediaItemView mView;
    private BaseApplication mBaseApplication;
    private DeviceDisplay mDeviceDisplay;
    private ContentBrowseBiz mContentBrowseBiz;
    private TestRegistryListener rListener;
    private UpnpServiceBiz upnpServiceBiz;
    private MediaContentBiz mediaContentBiz;
    private MediaServer mediaServer;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DeviceDisplayListOperation.ADD:
                    ContentItem mContentItem = (ContentItem) msg.obj;
                    if (!mContentItem.isContainer()) {
                        if (mContentItem.getFiletype() == 1) {
                            if (!mDlnaPictureIdList.contains(mContentItem.getItem().getId())) {
                                mDlnaPictureIdList.add(mContentItem.getItem().getId());
                                mDlnaPictureMapList.put(mContentItem.getItem().getId(), mContentItem.getItem());
                            }
                        }
                        else if (mContentItem.getFiletype() == 2) {
                            if (!mDlnaVideoIdList.contains(mContentItem.getItem().getId())) {
                                mDlnaVideoIdList.add(mContentItem.getItem().getId());
                                mDlnaVideoMapList.put(mContentItem.getItem().getId(), mContentItem.getItem());
                            }
                        }
                    } else {
                        mContentBrowseBiz.getContent(mContentItem);
                    }
                    break;
                case DeviceDisplayListOperation.CLEAR_ALL:
                    break;
            }
        }
    };

    public LocalMediaItemPresenterImpl() {
    }

    public LocalMediaItemPresenterImpl(LocalMediaItemView mView, Context mContext, BaseApplication mBaseApplication) {
        this.mView = mView;
        this.mContext = mContext;
        this.mBaseApplication = mBaseApplication;
    }

    public void init(Context mContext, Handler mHandle) {
        upnpServiceBiz = UpnpServiceBiz.newInstance();
        rListener = new TestRegistryListener(mHandle);
        upnpServiceBiz.addListener(rListener);
        try {
            InetAddress localAddress = IPUtil.getLocalIpAddress(mContext);
            if (mediaServer == null) {
                try {
                    mediaServer = new MediaServer(localAddress);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            upnpServiceBiz.addDevice(mediaServer.getDevice());
            mediaContentBiz = new MediaContentBiz();
            mediaContentBiz.prepareMediaServer(mContext, mediaServer.getAddress());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getItems() {
        mDeviceDisplay = mBaseApplication.getDeviceDisplay();
        mBaseApplication.setDeviceDisplay(null);
        mContentBrowseBiz = new ContentBrowseBiz(mHandler);
        if (mDeviceDisplay != null) {
            mContentBrowseBiz.getRootContent(mDeviceDisplay.getDevice());
        }
    }

    public void destory() {
        if (upnpServiceBiz != null) {
            upnpServiceBiz.removeListener(rListener);
            upnpServiceBiz = null;
        }
        if (mediaContentBiz != null) {
            mediaContentBiz = null;
        }
    }

    public void restore() {
        if (null != mediaServer) {
            mediaServer.restore();
        }
    }
}
