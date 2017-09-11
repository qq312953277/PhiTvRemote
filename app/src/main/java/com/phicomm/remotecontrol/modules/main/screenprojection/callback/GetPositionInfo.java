package com.phicomm.remotecontrol.modules.main.screenprojection.callback;

import com.phicomm.remotecontrol.modules.main.screenprojection.entity.PositionInfo;
import com.phicomm.remotecontrol.util.LogUtil;

import org.fourthline.cling.model.action.ActionInvocation;
import org.fourthline.cling.model.meta.Service;
import org.fourthline.cling.model.types.InvalidValueException;
import org.fourthline.cling.model.types.UnsignedIntegerFourBytes;

import java.util.Map;

/**
 * Created by kang.sun on 2017/8/22.
 */
public abstract class GetPositionInfo extends UpnpActionCallback {
    private static String TAG = GetPositionInfo.class.getSimpleName();

    public GetPositionInfo(UnsignedIntegerFourBytes instanceId, Service service) {
        super(new ActionInvocation(service.getAction("GetPositionInfo")));
        try {
            setInput("InstanceID", instanceId);
        } catch (InvalidValueException e) {
            LogUtil.e(TAG, e.toString());
        }
    }

    @Override
    public void received(ActionInvocation invocation, Map<String, Object> result) {
        PositionInfo positionInfo = new PositionInfo(result);
        onSuccess(positionInfo);
    }

    public abstract void onSuccess(PositionInfo positionInfo);
}
