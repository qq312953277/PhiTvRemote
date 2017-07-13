package com.phicomm.remotecontrol;

import android.app.Application;

import com.phicomm.remotecontrol.greendao.GreenDaoManager;
import com.phicomm.remotecontrol.greendao.gen.RemoteDeviceDao;

/**
 * Created by chunya02.li on 2017/6/30.
 */

public class MyApplication extends Application {
    private static MyApplication mInstance;

    private GreenDaoManager mGreenDaoManager;
    private RemoteDeviceDao mRemoteDevcieDao;

    public static MyApplication getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        mGreenDaoManager = GreenDaoManager.getInstance();
        mRemoteDevcieDao = mGreenDaoManager.getSession().getRemoteDeviceDao();
    }

    public RemoteDeviceDao getRemoteDeviceDao() {
        return mRemoteDevcieDao;
    }

}
