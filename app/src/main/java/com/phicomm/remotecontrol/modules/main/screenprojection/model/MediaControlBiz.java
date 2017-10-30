package com.phicomm.remotecontrol.modules.main.screenprojection.model;

import android.os.Handler;
import android.os.Message;

import com.phicomm.remotecontrol.modules.main.screenprojection.callback.GetMediaInfo;
import com.phicomm.remotecontrol.modules.main.screenprojection.callback.GetPositionInfo;
import com.phicomm.remotecontrol.modules.main.screenprojection.callback.VideoPause;
import com.phicomm.remotecontrol.modules.main.screenprojection.callback.VideoPlay;
import com.phicomm.remotecontrol.modules.main.screenprojection.callback.VideoSeek;
import com.phicomm.remotecontrol.modules.main.screenprojection.callback.SetAVTransportURI;
import com.phicomm.remotecontrol.modules.main.screenprojection.callback.VideoStop;
import com.phicomm.remotecontrol.modules.main.screenprojection.constants.MediaControlOperation;
import com.phicomm.remotecontrol.modules.main.screenprojection.entity.MediaInfo;
import com.phicomm.remotecontrol.modules.main.screenprojection.entity.PositionInfo;
import com.phicomm.remotecontrol.util.LogUtil;

import org.fourthline.cling.model.ModelUtil;
import org.fourthline.cling.model.action.ActionInvocation;
import org.fourthline.cling.model.message.UpnpResponse;
import org.fourthline.cling.model.meta.Device;
import org.fourthline.cling.model.meta.Service;
import org.fourthline.cling.model.types.UDAServiceType;
import org.fourthline.cling.model.types.UnsignedIntegerFourBytes;
import org.fourthline.cling.support.contentdirectory.DIDLParser;
import org.fourthline.cling.support.model.DIDLContent;
import org.fourthline.cling.support.model.item.Item;

/**
 * Created by kang.sun on 2017/8/22.
 */
public class MediaControlBiz {
    public static final int SUCCESS = 0;
    public static final int FAILURE = 1;
    private static String TAG = MediaControlBiz.class.getSimpleName();
    private Service serviceAVT;
    private Service serviceRC;
    private UpnpServiceBiz upnpBiz;
    protected Handler handler;
    private UnsignedIntegerFourBytes instanceId;

    public MediaControlBiz(Device device, Handler handler, long mId) {
        serviceAVT = device.findService(new UDAServiceType("AVTransport", 1));
        serviceRC = device
                .findService(new UDAServiceType("RenderingControl", 1));
        upnpBiz = UpnpServiceBiz.newInstance();
        this.handler = handler;
        instanceId = new UnsignedIntegerFourBytes(mId);
    }

    public MediaControlBiz(Device device, long mId) {
        serviceAVT = device.findService(new UDAServiceType("AVTransport", 1));
        serviceRC = device
                .findService(new UDAServiceType("RenderingControl", 1));
        upnpBiz = UpnpServiceBiz.newInstance();
        instanceId = new UnsignedIntegerFourBytes(mId);
    }

    /**
     * 设备要播放的媒体的uri
     */
    public void setPlayUri(Item item) {
        String uri = item.getFirstResource().getValue();
        DIDLContent didlContent = new DIDLContent();
        didlContent.addItem(item);
        DIDLParser parser = new DIDLParser();
        String metadata = "";
        try {
            metadata = parser.generate(didlContent);
        } catch (Exception e) {
            e.printStackTrace();
        }
        upnpBiz.execute(new SetAVTransportURI(instanceId, serviceAVT, uri,
                metadata) {
            @Override
            public void failure(ActionInvocation invocation,
                                UpnpResponse operation, String defaultMsg) {
                LogUtil.d(TAG, "setPlayUri failure:" + defaultMsg);
            }

            @Override
            public void onSuccess(String defaultMsg) {
                LogUtil.d(TAG, "SetAVTransportURI successed:" + defaultMsg);
            }
        });
    }

    public interface GetPositionInfoListerner {
        void failure(ActionInvocation invocation, UpnpResponse operation,
                     String defaultMsg);

        void onSuccess(PositionInfo positionInfo);
    }

    protected GetPositionInfoListerner gpiListener;

    /**
     * 获取当前播放的媒体位置信息
     */
    public void getPositionInfo(GetPositionInfoListerner listener) {
        this.gpiListener = listener;
        upnpBiz.execute(new GetPositionInfo(instanceId, serviceAVT) {
            @Override
            public void failure(ActionInvocation invocation, UpnpResponse operation,
                                String defaultMsg) {
                LogUtil.d(TAG, "Get position info failure:" + defaultMsg);
                if (gpiListener != null) {
                    gpiListener.failure(invocation, operation, defaultMsg);
                }
            }

            @Override
            public void onSuccess(PositionInfo positionInfo) {
                if (gpiListener != null) {
                    gpiListener.onSuccess(positionInfo);
                }
            }
        });
    }

    /**
     * 获取当前播放的媒体信息
     */
    public void getMediaInfo() {
        upnpBiz.execute(new GetMediaInfo(instanceId, serviceAVT) {
            @Override
            public void failure(ActionInvocation invocation,
                                UpnpResponse operation, String defaultMsg) {
                LogUtil.d(TAG, "GetMediaInfo failure:" + defaultMsg);
            }

            @Override
            public void onSuccess(MediaInfo mediaInfo) {
            }
        });
    }

    /**
     * 播放
     */
    public void play() {
        upnpBiz.execute(new VideoPlay(instanceId, serviceAVT) {
            @Override
            public void failure(ActionInvocation invocation,
                                UpnpResponse operation, String defaultMsg) {
            }

            @Override
            public void onSuccess(String defaultMsg) {
                LogUtil.d(TAG, "VideoPlay successed:" + defaultMsg);
                Message msg = Message.obtain(handler);
                msg.what = MediaControlOperation.PLAY;
                msg.arg1 = SUCCESS;
                msg.sendToTarget();
            }
        });
    }

    /**
     * 暂停
     */
    public void pause() {
        upnpBiz.execute(new VideoPause(instanceId, serviceAVT) {
            @Override
            public void failure(ActionInvocation invocation,
                                UpnpResponse operation, String defaultMsg) {
            }

            @Override
            public void onSuccess(String defaultMsg) {
                LogUtil.d(TAG, "VideoPause successed:" + defaultMsg);
                Message msg = Message.obtain(handler);
                msg.what = MediaControlOperation.PAUSE;
                msg.arg1 = SUCCESS;
                msg.sendToTarget();
            }
        });
    }

    public void stop() {
        upnpBiz.execute(new VideoStop(instanceId, serviceAVT) {
            @Override
            public void failure(ActionInvocation invocation,
                                UpnpResponse operation, String defaultMsg) {
                LogUtil.d(TAG, "VideoStop failure:" + defaultMsg);
            }

            @Override
            public void onSuccess(String defaultMsg) {
                LogUtil.d(TAG, "VideoStop successed:" + defaultMsg);

            }
        });
    }

    /**
     * 设置跳转时间
     */
    public void seek(String totalTime, int percent) {
        long duration = ModelUtil.fromTimeString(totalTime);
        long seekTime = percent * duration / 100;
        String seekTo = ModelUtil.toTimeString(seekTime);
        upnpBiz.execute(new VideoSeek(instanceId, serviceAVT, seekTo) {
            @Override
            public void failure(ActionInvocation invocation,
                                UpnpResponse operation, String defaultMsg) {
                LogUtil.d(TAG, "VideoSeek failure:" + defaultMsg);
            }

            @Override
            public void onSuccess(String defaultMsg) {
                LogUtil.d(TAG, "VideoSeek successed:" + defaultMsg);
                Message msg = Message.obtain(handler);
                msg.what = MediaControlOperation.SEEK;
                msg.arg1 = SUCCESS;
                msg.sendToTarget();
            }
        });
    }
}
