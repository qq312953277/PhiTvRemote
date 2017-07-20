package com.phicomm.remotecontrol;

import com.phicomm.remotecontrol.httpclient.IRemoterService;
import com.phicomm.remotecontrol.httpclient.RemoteServiceImpl;

import java.util.ArrayList;

/**
 * Created by xufeng02.zhou on 2017/7/19.
 */

public class ConnectManager {
    private RemoteBoxDevice mConnectingDevice;
    private ArrayList<ConnectListener> mListeners;

    private static class SingletonHolder {
        private static ConnectManager instance = new ConnectManager();
    }

    interface ConnectListener {
        void onConnectChanged();
    }

    static public ConnectManager getInstance() {
        return SingletonHolder.instance;
    }

    private ConnectManager() {
        if (mListeners == null) {
            mListeners = new ArrayList<ConnectListener>();
        }
    }

    public boolean connect(RemoteBoxDevice dev) {
        if (mConnectingDevice != null && mConnectingDevice.equals(dev)) {
            return true;
        }

//        notifyConnectChange();
        mConnectingDevice = dev;
        initConnect(mConnectingDevice.getAddress(), mConnectingDevice.getPort());
        return true;
    }

    public void unConnect( ) {
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
