package com.phicomm.remotecontrol.modules.main.screenprojection.model;

import android.os.Handler;
import android.os.Message;

import com.phicomm.remotecontrol.modules.main.screenprojection.callback.AVTransportEvent;
import com.phicomm.remotecontrol.modules.main.screenprojection.callback.RenderingControlEvent;
import com.phicomm.remotecontrol.modules.main.screenprojection.constants.MediaEventType;
import com.phicomm.remotecontrol.modules.main.screenprojection.entity.AVTransportInfo;
import com.phicomm.remotecontrol.modules.main.screenprojection.entity.RenderingControlInfo;
import com.phicomm.remotecontrol.util.LogUtil;

import org.fourthline.cling.model.gena.CancelReason;
import org.fourthline.cling.model.gena.GENASubscription;
import org.fourthline.cling.model.message.UpnpResponse;
import org.fourthline.cling.model.meta.Device;
import org.fourthline.cling.model.meta.Service;
import org.fourthline.cling.model.types.UDAServiceType;

/**
 * Created by kang.sun on 2017/8/23.
 */
public class MediaEventBiz {
    private static String TAG = MediaControlBiz.class.getSimpleName();
    private Device device;
    private UpnpServiceBiz upnpBiz;
    protected Handler handler;
    private RenderingControlEvent serviceRCEvent;
    private AVTransportEvent transportEvent;

    public MediaEventBiz(Device device, Handler handler) {
        this.device = device;
        this.handler = handler;
        upnpBiz = UpnpServiceBiz.newInstance();
    }

    public void addRenderingEvent() {
        Service serviceRC = device.findService(new UDAServiceType("RenderingControl", 1));
        serviceRCEvent = new RenderingControlEvent(serviceRC) {
            @Override
            protected void failed(GENASubscription subscription,
                                  UpnpResponse responseStatus, Exception exception, String defaultMsg) {
                LogUtil.d(TAG, "Rendering failed:" + defaultMsg);
            }

            @Override
            protected void eventsMissed(GENASubscription subscription,
                                        int numberOfMissedEvents) {
                LogUtil.d(TAG, "Rendering eventsMissed:" + numberOfMissedEvents);
            }

            @Override
            protected void established(GENASubscription subscription) {
                LogUtil.d(TAG, "Rendering established:" + subscription.toString());
            }

            @Override
            protected void ended(GENASubscription subscription, CancelReason reason,
                                 UpnpResponse responseStatus) {
                LogUtil.d(TAG, "Rendering ended");
            }

            @Override
            public void received(RenderingControlInfo controlInfo) {
                LogUtil.d(TAG, "Rendering received:" + controlInfo.toString());
                Message msg = Message.obtain(handler);
                msg.what = MediaEventType.RENDERING_CONTROL;
                msg.obj = controlInfo;
                msg.sendToTarget();
            }
        };
        upnpBiz.execute(serviceRCEvent);
    }

    public void removeRenderingEvent() {
        if (serviceRCEvent != null) {
            serviceRCEvent.end();
            LogUtil.d(TAG, "Remove rendering event");
        }
    }

    public void addAVTransportEvent() {
        Service serviceAVT = device.findService(new UDAServiceType("AVTransport", 1));
        transportEvent = new AVTransportEvent(serviceAVT) {
            @Override
            protected void failed(GENASubscription subscription,
                                  UpnpResponse responseStatus, Exception exception, String defaultMsg) {
                LogUtil.d(TAG, "AVTransport failed:" + defaultMsg);
            }

            @Override
            protected void eventsMissed(GENASubscription subscription,
                                        int numberOfMissedEvents) {
                LogUtil.d(TAG, "AVTransport eventsMissed:" + numberOfMissedEvents);
            }

            @Override
            protected void established(GENASubscription subscription) {
                LogUtil.d(TAG, "AVTransport established:" + subscription.toString());
            }

            @Override
            protected void ended(GENASubscription subscription, CancelReason reason,
                                 UpnpResponse responseStatus) {
                LogUtil.d(TAG, "AVTransport ended");
            }

            @Override
            public void received(AVTransportInfo avTransportInfo) {
                LogUtil.d(TAG, "AVTransport received:" + avTransportInfo.toString());
                Message msg = Message.obtain(handler);
                msg.what = MediaEventType.AV_TRANSPORT;
                msg.obj = avTransportInfo;
                msg.sendToTarget();
            }
        };
        upnpBiz.execute(transportEvent);
    }

    public void removeAVTransportEvent() {
        if (transportEvent != null) {
            transportEvent.end();
            LogUtil.d(TAG, "Remove AVTransport event");
        }
    }
}


