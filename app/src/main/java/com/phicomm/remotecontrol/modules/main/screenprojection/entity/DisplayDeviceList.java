package com.phicomm.remotecontrol.modules.main.screenprojection.entity;

import android.support.annotation.Nullable;

import org.fourthline.cling.model.meta.Device;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kang.sun on 2017/8/21.
 */
public class DisplayDeviceList {
    private static DisplayDeviceList INSTANCE = null;
    /**
     * 投屏设备列表 都是引用该 list
     */
    private List<DeviceDisplay> mDeviceDisplayList;

    private DisplayDeviceList() {
        mDeviceDisplayList = new ArrayList<>();
    }

    public static DisplayDeviceList getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DisplayDeviceList();
        }
        return INSTANCE;
    }

    public void removeDevice(DeviceDisplay device) {
        mDeviceDisplayList.remove(device);
    }

    public void addDevice(DeviceDisplay device) {
        mDeviceDisplayList.add(device);
    }

    @Nullable
    public DeviceDisplay getDeviceDisplay(Device device) {
        for (DeviceDisplay DeviceDisplay : mDeviceDisplayList) {
            Device deviceTemp = DeviceDisplay.getDevice();
            if (deviceTemp != null && deviceTemp.equals(device)) {
                return DeviceDisplay;
            }
        }
        return null;
    }

    public boolean contain(Device device) {
        for (DeviceDisplay DeviceDisplay : mDeviceDisplayList) {
            Device deviceTemp = DeviceDisplay.getDevice();
            if (deviceTemp != null && deviceTemp.equals(device)) {
                return true;
            }
        }
        return false;
    }

    @Nullable
    public List<DeviceDisplay> getDeviceDisplayList() {
        return mDeviceDisplayList;
    }

    public void setDeviceDisplayList(List<DeviceDisplay> DeviceDisplayList) {
        mDeviceDisplayList = DeviceDisplayList;
    }

    public void destroy() {
        mDeviceDisplayList = null;
        INSTANCE = null;
    }
}
