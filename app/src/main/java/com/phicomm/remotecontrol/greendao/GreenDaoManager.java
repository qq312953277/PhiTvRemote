package com.phicomm.remotecontrol.greendao;

import com.phicomm.remotecontrol.MyApplication;
import com.phicomm.remotecontrol.base.BaseApplication;
import com.phicomm.remotecontrol.greendao.gen.DaoMaster;
import com.phicomm.remotecontrol.greendao.gen.DaoSession;
import com.phicomm.remotecontrol.greendao.gen.RemoteDeviceDao;

/**
 * Created by chunya02.li on 2017/6/30.
 */
public class GreenDaoManager {
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;
    private RemoteDeviceDao mRemoteDeviceDao;
    private static GreenDaoManager mInstance;

    private GreenDaoManager() {
        if (mInstance == null) {
            DaoMaster.DevOpenHelper devOpenHelper = new
                    DaoMaster.DevOpenHelper(BaseApplication.getContext(), "remote_devices", null);
            mDaoMaster = new DaoMaster(devOpenHelper.getWritableDatabase());
            mDaoSession = mDaoMaster.newSession();
            mRemoteDeviceDao = mDaoSession.getRemoteDeviceDao();
        }
    }

    public static GreenDaoManager getInstance() {
        if (mInstance == null) {
            synchronized (GreenDaoManager.class) {
                if (mInstance == null) {
                    mInstance = new GreenDaoManager();
                }
            }
        }
        return mInstance;
    }

    public DaoMaster getMaster() {
        return mDaoMaster;
    }

    public DaoSession getSession() {
        return mDaoSession;
    }

    public DaoSession getNewSession() {
        return mDaoMaster.newSession();
    }

    public RemoteDeviceDao getRemoteDeviceDao() {
        return mRemoteDeviceDao;
    }
}

