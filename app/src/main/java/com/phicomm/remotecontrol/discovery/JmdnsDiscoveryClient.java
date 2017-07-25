package com.phicomm.remotecontrol.discovery;

import android.os.Handler;
import android.util.Log;

import com.phicomm.remotecontrol.RemoteBoxDevice;
import com.phicomm.remotecontrol.constant.PhiConstants;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceInfo;
import javax.jmdns.ServiceListener;


/**
 * Created by chunya02.li on 2017/7/21.
 */

public class JmdnsDiscoveryClient implements Runnable {
    private static String TAG = "JmdnsDiscoveryClient";

    private List<RemoteBoxDevice> mBoxDeviceList;
    private HashMap<String, RemoteBoxDevice> mBoxDeviceListMap;
    private DiscoveryDevicesListener mDiscoveryListener = null;
    private IDiscoverResultListener mDiscoverResultListener = null;
    private JmDNS mMdnsService;
    private ServiceInfo mServiceInfo;
    private Handler mHandler;


    public JmdnsDiscoveryClient(IDiscoverResultListener discoverResultListener) {
        mBoxDeviceList = new ArrayList<>(0);
        mDiscoverResultListener = discoverResultListener;
        mHandler = new Handler();
        mBoxDeviceListMap = new HashMap<>(0);
    }

    @Override
    public void run() {
        mBoxDeviceList.clear();
        try {
            discoverJmdnsDevices();
        } catch (IOException e) {
            e.printStackTrace();
            stopDiscovery();
        }
    }

    private void discoverJmdnsDevices() throws IOException {
        Log.i(TAG, "begin searching...");
        mMdnsService = JmDNS.create();
        mDiscoveryListener = new DiscoveryDevicesListener();
        mMdnsService.addServiceListener(PhiConstants.REMOTE_SERVICE_TYPE_JMDNS,
                mDiscoveryListener);
    }

    public void stopDiscovery() {
        mHandler.removeCallbacksAndMessages(null);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (mMdnsService != null) {
                        Log.i(TAG, "Stopping probe....");
                        mMdnsService.unregisterAllServices();
                        mMdnsService.removeServiceListener(PhiConstants
                                .REMOTE_SERVICE_TYPE_JMDNS, mDiscoveryListener);
                        mMdnsService.close();
                        mMdnsService = null;
                    }
                } catch (Exception ex) {
                    Log.e(TAG, ex.getMessage(), ex);
                }
            }
        }).start();

    }

    private class DiscoveryDevicesListener implements ServiceListener {
        public void serviceAdded(ServiceEvent event) {
            mServiceInfo = mMdnsService.getServiceInfo(PhiConstants.REMOTE_SERVICE_TYPE_JMDNS,
                    event.getName());
            Log.d(TAG, "serviceAdded service type mServiceInfo=" + mServiceInfo);
            mMdnsService.requestServiceInfo(event.getType(), event.getName(), 1);
        }

        public void serviceRemoved(ServiceEvent event) {
            ServiceInfo info = event.getInfo();
            Log.d(TAG, "Remove service type = " + event.getType() + ", name = "
                    + event.getName() + "info=" + info);
            if (info.getInet4Addresses().length <= 0) {
                return;
            }
            final RemoteBoxDevice findDevice = parseSeviceInfo2RemoteDevice(info);
            if ((findDevice != null) && mBoxDeviceListMap.containsKey(findDevice.getBssid())) {
                mBoxDeviceList.remove(findDevice);
                mBoxDeviceListMap.remove(findDevice.getBssid());
                if (mDiscoverResultListener != null) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mDiscoverResultListener.onDeviceRemove(findDevice);
                        }
                    });
                }
            }
        }

        public void serviceResolved(ServiceEvent event) {
            ServiceInfo info = event.getInfo();
            Log.d(TAG, "serviceResolved info=" + info + "mServiceInfo.getInet4Addresses().length=" +
                    info.getInet4Addresses().length);
            if (info.getInet4Addresses().length <= 0) {
                return;
            }
            final RemoteBoxDevice findDevice = parseSeviceInfo2RemoteDevice(info);
            if ((findDevice != null) && !(mBoxDeviceListMap.containsKey(findDevice.getBssid()))) {
                mBoxDeviceList.add(findDevice);
                mBoxDeviceListMap.put(findDevice.getBssid(), findDevice);
                if (mDiscoverResultListener != null) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mDiscoverResultListener.onDeviceAdd(findDevice);
                        }
                    });
                }
            }
        }
    }


    private RemoteBoxDevice parseSeviceInfo2RemoteDevice(javax.jmdns.ServiceInfo serviceInfo) {
        byte[] textBytes = serviceInfo.getTextBytes();
        Log.d(TAG, "parseSeviceInfo2RemoteDevice(): textBytes toString: " + new String(textBytes));
        int index = 0;
        InetAddress inetAddress = serviceInfo.getInet4Addresses()[0];
        Map<String, String> keyValue = new HashMap<>();
        String part;
        String[] subParts;
        while (index < textBytes.length) {
            part = new String(textBytes, index + 1, textBytes[index]);
            subParts = part.split("=");
            if (subParts.length == 2) {
                keyValue.put(subParts[0], subParts[1]);
            }
            index += (1 + textBytes[index]);
        }
        String bssid = keyValue.get(PhiConstants.KEY_BSSID);
        String name = keyValue.get(PhiConstants.KEY_NAME);
        if (bssid == null || name == null) {
            Log.d(TAG, "parseSeviceInfo2RemoteDevice(): bssid = null or type = null, return null");
            return null;
        }
        Log.d(TAG, "bssid=" + bssid + " name=" + name + "inetAddress=" + inetAddress.getHostAddress());
        RemoteBoxDevice mRemoteBoxDevice;
        mRemoteBoxDevice = new RemoteBoxDevice(name, inetAddress.getHostAddress(),
                serviceInfo.getPort(), bssid);
        return mRemoteBoxDevice;
    }

    public interface IDiscoverResultListener {
        void onDeviceAdd(RemoteBoxDevice device);

        void onDeviceRemove(RemoteBoxDevice device);
    }
}
