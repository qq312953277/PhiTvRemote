package com.phicomm.remotecontrol.modules.devices.searchdevices;

import android.app.Service;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

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

import com.phicomm.remotecontrol.constant.PhiConstants;
import com.phicomm.remotecontrol.RemoteBoxDevice;

/**
 * Created by chunya02.li on 2017/7/11.
 */

public class JmdnsDiscoveryService extends Service {
    private static final String TAG = "JmdnsDiscoveryService";
    /**
     * Allows an application to receive Wifi Multicast packets.
     */
    private WifiManager mWifiManager;
    private WifiManager.MulticastLock mMulticastLock = null;
    private List<RemoteBoxDevice> mBoxDeviceList = new ArrayList<>(0);
    private HashMap<String, RemoteBoxDevice> mBoxDeviceListMap = new HashMap<>(0);
    private JmdnsDiscoveryService.DiscoveryDevicesListener mDiscoveryListener = null;
    private JmdnsDiscoveryService.IDiscoverResultListener mDiscoverResultListener = null;
    private JmDNS mMdnsService;
    private ServiceInfo mServiceInfo;
    private Thread mThread;
    private DiscoveryBinder mBinder = new DiscoveryBinder();
    private Handler mHandler;

    @Override
    public void onCreate() {
        super.onCreate();
        mWifiManager = (WifiManager)getApplicationContext(). getSystemService(WIFI_SERVICE);
        mHandler = new Handler();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startDiscovery();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        stopDiscovery();
        super.onDestroy();
    }

    public class DiscoveryBinder extends Binder {
        public JmdnsDiscoveryService getService() {
            return JmdnsDiscoveryService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind");
        allowMulticast();
        startDiscovery();
        return mBinder;
    }

    public void setDiscoverResultListener(JmdnsDiscoveryService.IDiscoverResultListener listener) {
        this.mDiscoverResultListener = listener;
    }

    private Handler mJmdnsHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case PhiConstants.DISCOVERY_DERVICE_REMOVE:
                    Log.d(TAG, "stopDiscovery from timeout");
                    if (mMdnsService == null)
                        return;
                    stopDiscovery();
                    break;
            }
        }
    };

    /**
     * startDiscovery
     */
    public void startDiscovery() {
        mThread = null;
        mBoxDeviceList.clear();
        mBoxDeviceListMap.clear();
        Log.d(TAG, "startDiscovery()");
        mThread = new Thread() {
            @Override
            public void run() {
                try {
                    Log.i(TAG, "begin searching...");
                    mMdnsService = JmDNS.create();
                    mDiscoveryListener = new JmdnsDiscoveryService.DiscoveryDevicesListener();
                    mMdnsService.addServiceListener(PhiConstants.REMOTE_SERVICE_TYPE_JMDNS,
                            mDiscoveryListener);
                } catch (IOException ex) {
                    Log.e(TAG, "begin searching...fail");
                }
            }
        };
        mThread.start();
        mJmdnsHandler.removeMessages(PhiConstants.DISCOVERY_DERVICE_REMOVE);
        mJmdnsHandler.sendEmptyMessageDelayed(PhiConstants.DISCOVERY_DERVICE_REMOVE, PhiConstants
                .DISCOVERY_TIMEOUT);
    }

    /**
     * stopDiscovery
     */
    public void stopDiscovery() {
        mJmdnsHandler.removeMessages(PhiConstants.DISCOVERY_DERVICE_REMOVE);
        mHandler.removeCallbacksAndMessages(null);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (mMdnsService != null) {
                        Log.i(TAG, "Stopping probe....");
                        mMdnsService.unregisterAllServices();
                        mMdnsService.close();
                        mMdnsService = null;
                    }
                    if (mMulticastLock != null) {
                        Log.i(TAG, "Releasing Mutlicast Lock...");
                        mMulticastLock.release();
                        mMulticastLock = null;
                    }
                } catch (Exception ex) {
                    Log.e(TAG, ex.getMessage(), ex);
                }
            }
        }).start();

    }

    public class DiscoveryDevicesListener implements ServiceListener {
        public void serviceAdded(ServiceEvent event) {
            mServiceInfo = mMdnsService.getServiceInfo(PhiConstants.REMOTE_SERVICE_TYPE_JMDNS,
                    event.getName());
            Log.d(TAG, "serviceAdded service type mServiceInfo=" + mServiceInfo);
            mMdnsService.requestServiceInfo(event.getType(), event.getName(), 1);
        }

        public void serviceRemoved(ServiceEvent event) {
            Log.d(TAG, "Remove service type = " + event.getType() + ", name = "
                    + event.getName());
        }

        public void serviceResolved(ServiceEvent event) {
            ServiceInfo info = event.getInfo();
            Log.d(TAG, "serviceResolved info=" + info + "mServiceInfo.getInet4Addresses().length=" +
                    info.getInet4Addresses().length);
            if (info.getInet4Addresses().length <= 0) {
                return;
            }
            final RemoteBoxDevice findDevice = parseSeviceInfo2RemoteDevice(info);
            if (findDevice != null && !mBoxDeviceListMap.containsKey(findDevice.getBssid())) {
                mBoxDeviceList.add(findDevice);
                mBoxDeviceListMap.put(findDevice.getBssid(), findDevice);
                if (mBoxDeviceList != null && mDiscoverResultListener != null) {
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
        Log.d(TAG, "bssid=" + bssid + " name=" + name + "inetAddress=" + inetAddress);
        RemoteBoxDevice mRemoteBoxDevice;
        mRemoteBoxDevice = new RemoteBoxDevice(name, inetAddress.toString(), serviceInfo.getPort(), bssid);
        return mRemoteBoxDevice;
    }

    private void allowMulticast() {
        mMulticastLock = mWifiManager.createMulticastLock(getClass().getSimpleName());
        mMulticastLock.setReferenceCounted(false);
        mMulticastLock.acquire();
        Log.d(TAG, "mMulticastLock.acquire()");
    }

    public interface IDiscoverResultListener {
        void onDeviceAdd(RemoteBoxDevice device);
    }
}
