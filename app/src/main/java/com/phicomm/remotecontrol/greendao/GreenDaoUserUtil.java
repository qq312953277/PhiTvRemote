package com.phicomm.remotecontrol.greendao;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import com.phicomm.remotecontrol.MyApplication;
import com.phicomm.remotecontrol.RemoteBoxDevice;
import com.phicomm.remotecontrol.base.BaseApplication;
import com.phicomm.remotecontrol.greendao.Entity.RemoteDevice;
import com.phicomm.remotecontrol.greendao.gen.RemoteDeviceDao;

/**
 * Created by chunya02.li on 2017/6/30.
 */

public class GreenDaoUserUtil {
    private static String TAG = "GreenDaoUserUtil";
    private static RemoteDeviceDao mRemoteDeviceDao;

    public GreenDaoUserUtil() {
        mRemoteDeviceDao = BaseApplication.getApplication().getRemoteDeviceDao();
    }

    //insert data
    public void insertdata(RemoteDevice device) {
        Log.d(TAG, "insertdata device=" + device);
        mRemoteDeviceDao.insert(device);
    }

    //delete id data
    public void deleteData(long id) {
        Log.d(TAG, "deletData id=" + id);
        mRemoteDeviceDao.deleteByKey(id);
    }

    //delete list data
    public void deleteBatchByKey(List<Long> pkIds) {
        Log.d(TAG, "deleteBatchByKey pkIds=" + pkIds);
        mRemoteDeviceDao.deleteByKeyInTx(pkIds);
    }

    //delete all
    public void deleteAll() throws Exception {
        Log.d(TAG, "deleteAll");
        mRemoteDeviceDao.deleteAll();

    }

    //update data
    public void upateData(RemoteDevice device) {
        Log.d(TAG, "upateData device=" + device);
        mRemoteDeviceDao.update(device);
    }

    //querydata
    public List<RemoteBoxDevice> querydata() {
        List<RemoteDevice> devicelist = loadAllRecentByTimeOrder();
        List<RemoteBoxDevice> mRecentConnecedList = new ArrayList<>(0);
        for (int i = 0; i < devicelist.size(); i++) {
            RemoteDevice recentdevice = devicelist.get(0);
            RemoteBoxDevice temp = new RemoteBoxDevice(recentdevice.getName(), recentdevice.getAddress(), recentdevice.getPort(), recentdevice.getBssid());
            mRecentConnecedList.add(temp);
        }
        return mRecentConnecedList;
    }

    public List<RemoteDevice> loadAllRecentByTimeOrder() {
        return mRemoteDeviceDao.queryBuilder().orderDesc(RemoteDeviceDao.Properties.Time).list();
    }
}
