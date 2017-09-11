package com.phicomm.remotecontrol.modules.main.screenprojection.callback;

import com.phicomm.remotecontrol.modules.main.screenprojection.entity.RenderingControlInfo;

import org.fourthline.cling.model.meta.Service;
import org.fourthline.cling.support.lastchange.EventedValue;
import org.fourthline.cling.support.lastchange.LastChangeParser;
import org.fourthline.cling.support.model.Channel;
import org.fourthline.cling.support.renderingcontrol.lastchange.ChannelMute;
import org.fourthline.cling.support.renderingcontrol.lastchange.ChannelVolume;
import org.fourthline.cling.support.renderingcontrol.lastchange.RenderingControlLastChangeParser;

import java.util.List;

/**
 * Created by kang.sun on 2017/8/23.
 */
public abstract class RenderingControlEvent extends UpnpSubscriptionCallback {
    public RenderingControlEvent(Service service) {
        super(service);
    }

    public RenderingControlEvent(Service service, int requestedDurationSeconds) {
        super(service, requestedDurationSeconds);
    }

    @Override
    public void onReceive(List<EventedValue> values) {
        RenderingControlInfo controlInfo = new RenderingControlInfo();
        for (EventedValue entry : values) {
            if (RenderingControlInfo.MUTE.equals(entry.getName())) {
                Object v = entry.getValue();
                if (v instanceof ChannelMute) {
                    ChannelMute cm = (ChannelMute) v;
                    if (Channel.Master.equals(cm.getChannel())) {
                        controlInfo.setMute(cm.getMute());
                    }
                }
            }
            if (RenderingControlInfo.VOLUME.equals(entry.getName())) {
                Object v = entry.getValue();
                if (v instanceof ChannelVolume) {
                    ChannelVolume cv = (ChannelVolume) v;
                    if (Channel.Master.equals(cv.getChannel())) {
                        controlInfo.setVolume(cv.getVolume());
                    }
                }
            }
            if (RenderingControlInfo.PRESET_NAME_LIST.equals(entry.getName())) {
                Object v = entry.getValue();
                controlInfo.setPresetNameList(v.toString());
            }
        }
        received(controlInfo);
    }

    @Override
    public LastChangeParser getLastChangeParser() {
        return new RenderingControlLastChangeParser();
    }

    public abstract void received(RenderingControlInfo controlInfo);
}
