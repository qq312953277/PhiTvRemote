package com.phicomm.remotecontrol;

import com.phicomm.remotecontrol.beans.Status;
import com.phicomm.remotecontrol.httpclient.IRemoterService;
import com.phicomm.remotecontrol.httpclient.PhiCallBack;
import com.phicomm.remotecontrol.httpclient.RemoteServiceImpl;
import com.phicomm.remotecontrol.util.LogUtil;

import java.util.ArrayList;

/**
 * Created by xufeng02.zhou on 2017/7/19.
 */

public class ConnectManager {
    public static final int PORT = 8080;
    private RemoteBoxDevice mConnectingDevice;
    private ArrayList<ConnectListener> mListeners;

    private static class SingletonHolder {
        private static ConnectManager instance = new ConnectManager();
    }

    public interface ConnectListener {
        void onConnectChanged();
    }

    public interface ConnetResultCallback {
        void onSuccess(RemoteBoxDevice device);

        void onFail(String msg);
    }

    static public ConnectManager getInstance() {
        return SingletonHolder.instance;
    }

    private ConnectManager() {
        if (mListeners == null) {
            mListeners = new ArrayList<ConnectListener>();
        }
    }

    @Deprecated
    public boolean connect(RemoteBoxDevice dev) {
        if (mConnectingDevice != null && mConnectingDevice.equals(dev)) {
            return true;
        }

        mConnectingDevice = dev;
        initConnect(mConnectingDevice.getAddress(), mConnectingDevice.getPort());
        return true;
    }

    public void getStatus(String ip, int port, final ConnetResultCallback result) {
        connect(ip, PORT, result);
    }

    public void connect(final String ipAddress, final int port, final ConnetResultCallback result) {

        LogUtil.d("connect:" + ipAddress + "/" + port);
        final IRemoterService service = new RemoteServiceImpl(ipAddress, port);

        TaskQuene.getInstance().addSubscription(
                service.getStatus(), new PhiCallBack<Status>() {
                    @Override
                    public void onSuccess(Status status) {
                        LogUtil.d("connect onSuccess");
                        TaskQuene.getInstance().setRemoterService(service);
                        mConnectingDevice = new RemoteBoxDevice(status.getName(), ipAddress, port, status.getSn());
                        result.onSuccess(mConnectingDevice);
                    }

                    @Override
                    public void onFailure(String msg) {
                        result.onFail(msg);
                    }

                    @Override
                    public void onFinish() {

                    }
                }
        );
    }

    public void connect(final RemoteBoxDevice dev, final ConnetResultCallback result) {
        if (mConnectingDevice != null && mConnectingDevice.equals(dev)) {
            result.onSuccess(mConnectingDevice);
        }

        this.connect(dev.getAddress(), dev.getPort(), result);
    }

    public void unConnect() {
        notifyConnectChange();
    }

    public RemoteBoxDevice getConnectingDevice() {
        return mConnectingDevice;
    }

    private void initConnect(String ip, int port) {
        IRemoterService service = new RemoteServiceImpl(ip, port);
        TaskQuene.getInstance().setRemoterService(service);
    }

    public void addListener(ConnectListener listener) {

        for (ConnectListener l : mListeners) {
            if (l == listener) {
                return;
            }
        }
        mListeners.add(listener);
    }

    public void removeListener(ConnectListener listener) {
        mListeners.remove(listener);
    }

    private void notifyConnectChange() {
        for (ConnectListener l : mListeners) {
            l.onConnectChanged();
        }
    }
}
