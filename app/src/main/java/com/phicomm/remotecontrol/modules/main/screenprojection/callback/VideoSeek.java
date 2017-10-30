package com.phicomm.remotecontrol.modules.main.screenprojection.callback;

import com.phicomm.remotecontrol.util.LogUtil;

import org.fourthline.cling.model.action.ActionInvocation;
import org.fourthline.cling.model.meta.Service;
import org.fourthline.cling.model.types.InvalidValueException;
import org.fourthline.cling.model.types.UnsignedIntegerFourBytes;

import java.util.Map;

/**
 * Created by kang.sun on 2017/8/22.
 */
public abstract class VideoSeek extends UpnpActionCallback {
    private static String TAG = VideoSeek.class.getSimpleName();

    public VideoSeek(UnsignedIntegerFourBytes instanceId, Service service,
                     String seekTo) {
        super(new ActionInvocation(service.getAction("Seek")));
        try {
            setInput("InstanceID", instanceId);
            setInput("Unit", "REL_TIME");
            setInput("Target", seekTo);
        } catch (InvalidValueException e) {
            LogUtil.e(TAG, e.toString());
        }
    }

    @Override
    public void received(ActionInvocation invocation, Map<String, Object> result) {
        onSuccess("VideoSeek successful");
    }

    public abstract void onSuccess(String msg);
}
