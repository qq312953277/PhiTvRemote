package com.phicomm.remotecontrol.modules.devices.searchdevices;


import android.content.Context;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.phicomm.remotecontrol.ConnectManager;
import com.phicomm.remotecontrol.R;
import com.phicomm.remotecontrol.RemoteBoxDevice;
import com.phicomm.remotecontrol.modules.devices.searchdevices.DeviceDiscoveryContract.View;
import com.phicomm.remotecontrol.modules.devices.searchdevices.JmdnsDiscoveryClient.IDiscoverResultListener;
import com.phicomm.remotecontrol.util.DevicesUtil;
import com.phicomm.remotecontrol.util.LogUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.WIFI_SERVICE;

/**
 * Created by chunya02.li on 2017/7/11.
 */

public class DeviceDiscoveryPresenter implements DeviceDiscoveryContract.Presenter {
    private static String TAG = "DiscoveryPresenter";
    private View mView;
    private List<RemoteBoxDevice> mDiscoveryDeviceList;
    private Map<String, RemoteBoxDevice> mCachedRemoteAddress;
    private WifiManager mWifiManager;
    private WifiManager.MulticastLock mMulticastLock = null;
    private JmdnsDiscoveryClient mJmdnsDiscoveryClient;
    private List<RemoteBoxDevice> currentDeviceList;
    private Thread mJmdnsClientThread;

    public DeviceDiscoveryPresenter(View view, Context context) {
        mView = view;
        mView.setPresenter(this);
        mDiscoveryDeviceList = new ArrayList<>(0);
        mCachedRemoteAddress = new HashMap<>(0);
        currentDeviceList = new ArrayList<>(0);
        mWifiManager = (WifiManager) context.getSystemService(WIFI_SERVICE);
    }

    @Override
    public void start() {
        allowMulticast();
        clearListArray();
        startDiscoveryService();
    }

    @Override
    public void stop() {
        stopDiscoveryService();
    }

    private void startDiscoveryService() {
        mJmdnsDiscoveryClient = new JmdnsDiscoveryClient(mDiscoverResultListener);
        mJmdnsClientThread = new Thread(mJmdnsDiscoveryClient);
        mJmdnsClientThread.start();
    }

    public void ipConnect(String ip) {
        ConnectManager.getInstance().connect(ip, 8080, connetResultCallback);
    }

    public void ipConnectAgain(String ip) {
        ConnectManager.getInstance().connect(ip, 8080, connetAgainResultCallback);
    }

    private ConnectManager.ConnetResultCallback connetAgainResultCallback = new ConnectManager.ConnetResultCallback() {
        @Override
        public void onSuccess(RemoteBoxDevice device) {
            Log.d(TAG, "onSuccess");
            mCachedRemoteAddress.put(device.getBssid(), device);
            mDiscoveryDeviceList.add(device);
            DevicesUtil.setTarget(device);
            mView.setTittle(device.getName());
            setCurrentDeviceList(mDiscoveryDeviceList);
            mView.refreshListView(mDiscoveryDeviceList);
        }

        @Override
        public void onFail(String msg) {
            LogUtil.d(TAG, "onFail");
            DevicesUtil.setTarget(null);
            mView.setTittle(R.string.unable_to_connect_device);
            mView.showToast(msg);
        }
    };
    private ConnectManager.ConnetResultCallback connetResultCallback = new ConnectManager.ConnetResultCallback() {
        @Override
        public void onSuccess(RemoteBoxDevice device) {
            Log.d(TAG, "onSuccess");
            clearListArray();
            mCachedRemoteAddress.put(device.getBssid(), device);
            mDiscoveryDeviceList.add(device);
            DevicesUtil.setTarget(device);
            mView.setTittle(device.getName());
            setCurrentDeviceList(mDiscoveryDeviceList);
            mView.refreshListView(mDiscoveryDeviceList);
            DevicesUtil.insertOrUpdateRecentDevices(device);
            mView.stopIPConnectProgressBar();
        }

        @Override
        public void onFail(String msg) {
            LogUtil.d(TAG, "onFail");
            mView.stopIPConnectProgressBar();
            mView.showConnectFailDialog();
        }
    };

    @Override
    //首页下拉列表异常
    public void addDeviceItem(List<RemoteBoxDevice> mOnResumeRemoteBoxDevice, RemoteBoxDevice device) {
        if (!mCachedRemoteAddress.containsKey(device.getBssid())) {
            mCachedRemoteAddress.put(device.getBssid(), device);
            mDiscoveryDeviceList.add(device);
            mOnResumeRemoteBoxDevice.add(device);
            setCurrentDeviceList(mOnResumeRemoteBoxDevice);
            mView.refreshListView(mOnResumeRemoteBoxDevice);
        }
    }

    private void stopDiscoveryService() {
        releaseMulticast();
        mJmdnsDiscoveryClient.stopDiscovery();
    }

    @Override
    public List<RemoteBoxDevice> getCurrentDeviceList() {
        currentDeviceList = DevicesUtil.getCurrentDevicesListResult();
        mView.refreshListView(currentDeviceList);
        return currentDeviceList;
    }

    @Override
    public void removeItemAndRefreshView(RemoteBoxDevice device, List<RemoteBoxDevice> deviceList) {
        mDiscoveryDeviceList = deviceList;
        String deviceBssid = device.getBssid();
        mCachedRemoteAddress.remove(deviceBssid);
        mDiscoveryDeviceList.remove(device);
        setCurrentDeviceList(mDiscoveryDeviceList);
        mView.refreshListView(mDiscoveryDeviceList);
    }

    @Override
    public void setCurrentDeviceList(List<RemoteBoxDevice> current_list) {
        DevicesUtil.setCurrentListResult(current_list);
    }

    @Override
    public void loadRecentList() {
        DevicesUtil.loadRecentList();
    }

    @Override
    public void insertOrUpdateRecentDevices(RemoteBoxDevice device) {
        DevicesUtil.insertOrUpdateRecentDevices(device);
    }

    private IDiscoverResultListener mDiscoverResultListener = new IDiscoverResultListener() {
        @Override
        public void onDeviceAdd(RemoteBoxDevice device) {
            String deviceBssid = device.getBssid();
            if (!mCachedRemoteAddress.containsKey(deviceBssid)) {
                mCachedRemoteAddress.put(deviceBssid, device);
                mDiscoveryDeviceList.add(device);
                setCurrentDeviceList(mDiscoveryDeviceList);
                mView.refreshListView(mDiscoveryDeviceList);
            }
        }

        @Override
        public void onDeviceRemove(RemoteBoxDevice device) {
            String deviceBssid = device.getBssid();
            if (mCachedRemoteAddress.containsKey(deviceBssid) && mDiscoveryDeviceList.size() > 0) {
                mCachedRemoteAddress.remove(deviceBssid);
                mDiscoveryDeviceList.remove(device);
                setCurrentDeviceList(mDiscoveryDeviceList);
                mView.refreshListView(mDiscoveryDeviceList);
            }
        }
    };

    @Override
    public RemoteBoxDevice getTarget() {
        return DevicesUtil.getTarget();
    }

    private void allowMulticast() {
        mMulticastLock = mWifiManager.createMulticastLock(getClass().getSimpleName());
        mMulticastLock.setReferenceCounted(false);
        mMulticastLock.acquire();
        Log.d(TAG, "mMulticastLock.acquire()");
    }

    private void clearListArray() {
        mDiscoveryDeviceList.clear();
        mCachedRemoteAddress.clear();
        currentDeviceList.clear();
    }

    private void releaseMulticast() {
        if (mMulticastLock != null) {
            Log.i(TAG, "Releasing Mutlicast Lock...");
            mMulticastLock.release();
            mMulticastLock = null;
        }
    }

    @Override
    public boolean isContains(List<RemoteBoxDevice> mOnResumeRemoteBoxDevice, RemoteBoxDevice mTarget) {
        for (int i = 0; i < mOnResumeRemoteBoxDevice.size(); i++) {
            if (mTarget == null || mTarget.getBssid() == null || mOnResumeRemoteBoxDevice.get(i) == null) {
                continue;
            }
            if (mTarget.getBssid().equals(mOnResumeRemoteBoxDevice.get(i).getBssid())) {
                return true;
            }
        }
        return false;
    }
}

