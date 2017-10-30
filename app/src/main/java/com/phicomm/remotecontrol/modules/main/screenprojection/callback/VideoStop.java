package com.phicomm.remotecontrol.modules.main.screenprojection.callback;

import com.phicomm.remotecontrol.util.LogUtil;

import org.fourthline.cling.model.action.ActionInvocation;
import org.fourthline.cling.model.meta.Service;
import org.fourthline.cling.model.types.InvalidValueException;
import org.fourthline.cling.model.types.UnsignedIntegerFourBytes;

import java.util.Map;

/**
 * Created by kang.sun on 2017/10/27.
 */

public abstract class VideoStop extends UpnpActionCallback {
    private static String TAG = VideoStop.class.getSimpleName();

    public VideoStop(UnsignedIntegerFourBytes instanceId, Service service) {
        super(new ActionInvocation(service.getAction("Stop")));
        try {
            setInput("InstanceID", instanceId);
        } catch (InvalidValueException exception) {
            LogUtil.d(TAG, exception.toString());
        }
    }

    @Override
    public void received(ActionInvocation invocation, Map<String, Object> result) {
        onSuccess("VideoStop play successful");
    }

    public abstract void onSuccess(String msg);
}
