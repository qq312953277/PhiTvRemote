package com.phicomm.remotecontrol.modules.main.screenprojection.listener;

import android.os.Handler;
import android.os.Message;

import com.phicomm.remotecontrol.modules.main.screenprojection.constants.DeviceDisplayListOperation;
import com.phicomm.remotecontrol.modules.main.screenprojection.entity.DeviceDisplay;

import org.fourthline.cling.model.meta.Device;
import org.fourthline.cling.registry.Registry;

/**
 * Created by kang.sun on 2017/8/21.
 */
public class TestRegistryListener extends UpnpRegistryListener {
    private Handler handler;

    public TestRegistryListener(Handler handler) {
        this.handler = handler;
    }

    /**
     * 添加设备
     */
    @Override
    public void deviceAdded(Device device) {
        if (handler == null) {
            return;
        }
        DeviceDisplay d = new DeviceDisplay(device);
        Message msg = Message.obtain(handler);
        msg.what = DeviceDisplayListOperation.ADD;
        msg.obj = d;
        //msg.sendToTarget();
        handler.sendMessage(msg);
    }

    /**
     * 删除设备
     */
    @Override
    public void deviceRemoved(Device device) {
        if (handler == null) {
            return;
        }
        Message msg = Message.obtain(handler);
        msg.what = DeviceDisplayListOperation.REMOVE;
        msg.obj = new DeviceDisplay(device);
        //msg.sendToTarget();
        handler.sendMessage(msg);
    }

    @Override
    public void beforeShutdown(Registry registry) {
    }

    @Override
    public void afterShutdown() {
    }
}
