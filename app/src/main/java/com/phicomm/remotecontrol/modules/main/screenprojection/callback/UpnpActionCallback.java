package com.phicomm.remotecontrol.modules.main.screenprojection.callback;

import org.fourthline.cling.controlpoint.ActionCallback;
import org.fourthline.cling.model.action.ActionArgumentValue;
import org.fourthline.cling.model.action.ActionInvocation;
import org.fourthline.cling.model.types.InvalidValueException;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kang.sun on 2017/8/21.
 */
public abstract class UpnpActionCallback extends ActionCallback {
    public UpnpActionCallback(ActionInvocation actionInvocation) {
        super(actionInvocation);
    }

    public void setInput(String argumentName, Object value) throws InvalidValueException {
        getActionInvocation().setInput(argumentName, value);
    }

    public void setInput(ActionArgumentValue value) {
        getActionInvocation().setInput(value);
    }

    public void setInput(ActionArgumentValue[] input) {
        getActionInvocation().setInput(input);
    }

    @Override
    public void success(ActionInvocation invocation) {
        ActionArgumentValue[] aavs = invocation.getOutput();
        Map<String, Object> result = new HashMap<String, Object>();
        for (ActionArgumentValue aav : aavs) {
            result.put(aav.getArgument().getName(), aav.getValue());
        }
        received(invocation, result);
    }

    public abstract void received(ActionInvocation invocation, Map<String, Object> result);
}
