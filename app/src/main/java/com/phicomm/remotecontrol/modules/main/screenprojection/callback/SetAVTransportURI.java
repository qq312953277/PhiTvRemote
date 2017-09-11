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
public abstract class SetAVTransportURI extends UpnpActionCallback {
    private static String TAG = SetAVTransportURI.class.getSimpleName();

    public SetAVTransportURI(UnsignedIntegerFourBytes instanceId,
                             Service service, String uri, String metadata) {
        super(new ActionInvocation(service.getAction("SetAVTransportURI")));
        try {
            setInput("InstanceID", instanceId);
            setInput("CurrentURI", uri);
            setInput("CurrentURIMetaData", metadata);
        } catch (InvalidValueException e) {
            LogUtil.e(TAG, e.toString());
        }
    }

    @Override
    public void received(ActionInvocation invocation, Map<String, Object> result) {
        onSuccess("Set AVTransport URI successful");
    }

    public abstract void onSuccess(String msg);
}
