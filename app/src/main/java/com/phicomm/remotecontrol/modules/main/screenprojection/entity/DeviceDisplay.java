package com.phicomm.remotecontrol.modules.main.screenprojection.entity;

import com.phicomm.remotecontrol.R;
import com.phicomm.remotecontrol.base.BaseApplication;

import org.fourthline.cling.model.meta.Device;
import org.fourthline.cling.model.meta.Service;

/**
 * Created by kang.sun on 2017/8/21.
 */
public class DeviceDisplay {
    private Device device;

    public DeviceDisplay(Device device) {
        this.device = device;
    }

    public Device getDevice() {
        return this.device;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DeviceDisplay that = (DeviceDisplay) o;
        return device.equals(that.device);
    }

    @Override
    public int hashCode() {
        return device.hashCode();
    }

    @Override
    public String toString() {
        String name = device.getDetails() != null && device.getDetails().getFriendlyName() != null
                ? device.getDetails().getFriendlyName() : device.getDisplayString();
        return device.isFullyHydrated() ? name : name + " *";
    }

    public String getDetailsMsg() {
        StringBuilder sb = new StringBuilder();
        if (device.isFullyHydrated()) {
            sb.append(device.getDisplayString());
            sb.append("\n\n");
            for (Service srv : device.getServices()) {
                sb.append(srv.getServiceType()).append("\n");
            }
        } else {
            sb.append(BaseApplication.getContext().getString(R.string.device_search));
        }
        return sb.toString();
    }
}
