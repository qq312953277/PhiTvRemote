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
public abstract class VideoPlay extends UpnpActionCallback {
    private static String TAG = VideoPlay.class.getSimpleName();

    public VideoPlay(UnsignedIntegerFourBytes instanceId, Service service) {
        super(new ActionInvocation(service.getAction("Play")));
        try {
            setInput("InstanceID", instanceId);
            setInput("Speed", "1");
        } catch (InvalidValueException e) {
            LogUtil.e(TAG, e.toString());
        }
    }

    @Override
    public void received(ActionInvocation invocation, Map<String, Object> result) {
        onSuccess("VideoPlay successful");
    }

    public abstract void onSuccess(String msg);
}
