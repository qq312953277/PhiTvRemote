package com.phicomm.remotecontrol.discovery;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.phicomm.remotecontrol.discovery.DeviceDiscoveryContract.View;
import com.phicomm.remotecontrol.RemoteBoxDevice;
import com.phicomm.remotecontrol.util.DevicesUtil;
import com.phicomm.remotecontrol.discovery.JmdnsDiscoveryService.IDiscoverResultListener;

import static android.content.Context.BIND_AUTO_CREATE;

/**
 * Created by chunya02.li on 2017/7/11.
 */

public class DeviceDiscoveryPresenter implements DeviceDiscoveryContract.Presenter {
    private static String TAG = "DiscoveryPresenter";

    private View mView;
    private List<RemoteBoxDevice> mDiscoveryDeviceList;
    private Map<String, RemoteBoxDevice> mCachedRemoteAddress;
    public Context mContext;

    private JmdnsDiscoveryService.DiscoveryBinder mDiscoveryBinder;
    private boolean mIsBind;

    public DeviceDiscoveryPresenter(View view, Context context) {
        mView = view;
        mContext = context;
        mView.setPresenter(this);
        mDiscoveryDeviceList = new ArrayList<>(0);
        mCachedRemoteAddress = new HashMap<>(0);
    }

    @Override
    public void start() {
        mDiscoveryDeviceList.clear();
        mCachedRemoteAddress.clear();
        startDiscoveryService();
    }

    @Override
    public void stop() {
        stopDiscoveryService();
    }

    @Override
    public List<RemoteBoxDevice> getCurrentDeviceList() {
        List<RemoteBoxDevice> currentDeviceList = DevicesUtil.getCurrentDevicesListResult();
        mView.refreshListView(currentDeviceList);
        return currentDeviceList;
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

    private ServiceConnection mServiceConnect = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "onServiceConnected and setLister");
            mDiscoveryBinder = (JmdnsDiscoveryService.DiscoveryBinder) service;
            JmdnsDiscoveryService mService = mDiscoveryBinder.getService();
            mService.setDiscoverResultListener(mDiscoverResultListener);
        }
    };

    private void startDiscoveryService() {
        Intent bindIntent = new Intent(mContext, JmdnsDiscoveryService.class);
        mIsBind = mContext.bindService(bindIntent, mServiceConnect, BIND_AUTO_CREATE);
    }

    private void stopDiscoveryService() {
        if (mIsBind) {
            mContext.unbindService(mServiceConnect);
            mIsBind = false;
        }
    }


    private IDiscoverResultListener mDiscoverResultListener = new IDiscoverResultListener() {
        @Override
        public void onDeviceAdd(RemoteBoxDevice device) {
            String bssid = device.getBssid();
            Log.d(TAG, "dev.mBssid=" + bssid + "mSearchDeviceList.size()=" +
                    mDiscoveryDeviceList.size());
            if (!mCachedRemoteAddress.containsKey(bssid)) {
                mCachedRemoteAddress.put(bssid, device);
                mDiscoveryDeviceList.add(device);
                setCurrentDeviceList(mDiscoveryDeviceList);
                mView.refreshListView(mDiscoveryDeviceList);
            }
        }
    };

    @Override
    public RemoteBoxDevice getTarget() {
        return DevicesUtil.getTarget();
    }

}
