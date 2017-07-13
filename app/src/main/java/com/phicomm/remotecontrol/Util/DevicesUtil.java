package com.phicomm.remotecontrol.Util;

import android.util.Log;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import com.phicomm.remotecontrol.R;
import com.phicomm.remotecontrol.RemoteBoxDevice;
import com.phicomm.remotecontrol.greendao.Entity.RemoteDevice;
import com.phicomm.remotecontrol.greendao.GreenDaoUserUtil;


/**
 * Created by chunya02.li on 2017/7/7.
 */

public class DevicesUtil {

    private static String TAG = "DevicesUtil";

    public static List<RemoteDevice> mRecentedConnectedList = new ArrayList<>(0);

    private static Map<Long, String> mRecentedConnectedMap = new HashMap<>(0);

    public static List<RemoteBoxDevice> mCurrentDevicesListResult = new ArrayList<>(0);

    public static GreenDaoUserUtil mGreenDaoUserUtil;

    public static RemoteBoxDevice mTarget;


    public static boolean connectToEntry(String ipAddress) {
        return true;
    }

    public static List<RemoteBoxDevice> getCurrentDevicesListResult() {
        if (mCurrentDevicesListResult != null) {
            Log.d(TAG, "get currentListResult.size=" + mCurrentDevicesListResult.size());
        }
        return mCurrentDevicesListResult;
    }

    public static void setCurrentListResult(List<RemoteBoxDevice> currentListResult) {
        Log.d(TAG, "set currentListResult.size=" + currentListResult.size());
        mCurrentDevicesListResult = currentListResult;
    }

    public static void insertOrUpdateRecentDevices(RemoteBoxDevice remoteDevice) {
        RemoteDevice device = new RemoteDevice(null, remoteDevice.getName(), remoteDevice.getAddress(), remoteDevice.getBssid(), System.currentTimeMillis());
        Log.d(TAG, "insertOrUpdateRecentDevices remoteDevice=" + remoteDevice + "mRecentedConnectedMap.size()=" + mRecentedConnectedMap.size());
        Set<Long> ids = mRecentedConnectedMap.keySet();
        Long findId = null;
        for (Long id : ids) {
            String tempBssid = mRecentedConnectedMap.get(id);
            Log.d(TAG, "tempBssid=" + tempBssid + " remoteDevice.getBssid()=" + remoteDevice.getBssid());
            if (tempBssid.equals(remoteDevice.getBssid())) {
                findId = id;
                break;
            }
        }
        Log.d(TAG, "findId=" + findId);
        if (findId != null) {
            device.setId(findId);
            mGreenDaoUserUtil.upateData(device);
        } else {
            mGreenDaoUserUtil.insertdata(device);
        }
        setTarget(remoteDevice);
    }

    public static void loadRecentList() {
        mRecentedConnectedMap.clear();
        mRecentedConnectedList.clear();
        mRecentedConnectedList = mGreenDaoUserUtil.loadAllRecentByTimeOrder();
        for (int i = 0; i < mRecentedConnectedList.size(); i++) {
            mRecentedConnectedMap.put(mRecentedConnectedList.get(i).getId(), mRecentedConnectedList.get(i).getBssid());
        }
        Log.d(TAG, "mRecentedConnectedMap.size()=" + mRecentedConnectedMap);
    }

    public static void setGreenDaoUserUtil(GreenDaoUserUtil GreenDaoUserUtil) {
        mGreenDaoUserUtil = GreenDaoUserUtil;
    }

    public static void setTarget(RemoteBoxDevice target) {
        Log.d(TAG, "setTarget target=" + target);
        mTarget = target;
    }

    public static RemoteBoxDevice getTarget() {
        Log.d(TAG, "getTarget mTarget=" + mTarget);
        return mTarget;
    }
}

