package com.phicomm.remotecontrol.base;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.phicomm.remotecontrol.greendao.GreenDaoManager;
import com.phicomm.remotecontrol.greendao.gen.RemoteDeviceDao;
import com.phicomm.remotecontrol.modules.main.screenprojection.entity.DeviceDisplay;
import com.phicomm.remotecontrol.modules.main.screenprojection.model.UpnpServiceBiz;
import com.phicomm.remotecontrol.preference.PreferenceRepository;
import com.squareup.leakcanary.LeakCanary;

import org.fourthline.cling.android.FixedAndroidLogHandler;
import org.fourthline.cling.support.model.item.Item;
import org.seamless.util.logging.LoggingUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by hao04.wu on 2017/8/1.
 */

public class BaseApplication extends Application {
    private static Context mContext;
    private static BaseApplication manager = null;
    private List<Activity> mActiviyList = new ArrayList<Activity>();
    private Map<String, Activity> mDestoryMap = new HashMap<>();

    private GreenDaoManager mGreenDaoManager;
    private RemoteDeviceDao mRemoteDevcieDao;

    private PreferenceRepository mPreferenceRepository;
    private DeviceDisplay device;
    private Item item;
    private static Object lock = new Object();

    public boolean isLogined = false;

    @Override
    public void onCreate() {
        super.onCreate();
        //CrashHandler.getInstance().init(this);
        analyseLeak();
        mContext = this;
        manager = this;
        mGreenDaoManager = GreenDaoManager.getInstance();
        mRemoteDevcieDao = mGreenDaoManager.getSession().getRemoteDeviceDao();
        LoggingUtil.resetRootHandler(new FixedAndroidLogHandler());

        //应用打开时获得震动、音量的设置状态
        mPreferenceRepository = new PreferenceRepository(this);

    }

    public void setDeviceDisplay(DeviceDisplay device) {
        this.device = device;
    }

    public DeviceDisplay getDeviceDisplay() {
        return device;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Item getItem() {
        return item;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        UpnpServiceBiz.newInstance().closeUpnpService(this);
    }

    private void analyseLeak() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
    }

    public static Context getContext() {
        return mContext;
    }

    public static BaseApplication getApplication() {
        if (manager == null) {
            manager = new BaseApplication();
        }
        return manager;
    }

    public RemoteDeviceDao getRemoteDeviceDao() {

        return mRemoteDevcieDao;
    }

    /**
     * 添加到销毁队列
     *
     * @param activity
     */
    public void addDestoryActivity(Activity activity, String key) {
        if (!mDestoryMap.containsKey(key))
            mDestoryMap.put(key, activity);
    }

    /**
     * 销毁所有Activity
     */
    public void destoryActivity() {
        Set<String> keySet = mDestoryMap.keySet();
        for (String key : keySet) {
            if (!mDestoryMap.get(key).isFinishing())
                mDestoryMap.get(key).finish();
        }
    }

    /**
     * 销毁所有Activity
     */
    public void destoryAllActivity() {
        if (null != mActiviyList) {
            for (Activity activity : mActiviyList) {
                if (!activity.isFinishing()) {
                    activity.finish();
                }
            }
        }
    }

    /**
     * 销毁指定Activity
     */
    public void destorySpecifyActivity(String key) {
        if (mDestoryMap.containsKey(key)) {
            if (!mDestoryMap.get(key).isFinishing())
                mDestoryMap.get(key).finish();
        }
    }

    public List<Activity> getActiviyList() {
        if (mActiviyList == null) {
            mActiviyList = new ArrayList<>();
        }
        return mActiviyList;
    }

    public void add(Activity activity) {
        if (mActiviyList == null) {
            mActiviyList = new ArrayList<>();
        }
        if (!mActiviyList.contains(activity)) {
            mActiviyList.add(activity);
        }
    }

    public void remove(Activity activity) {
        if (mActiviyList == null) {
            mActiviyList = new ArrayList<>();
        }
        if (mActiviyList.contains(activity)) {
            mActiviyList.remove(activity);
        }
    }

    public void setActiviyList(List<Activity> activiyList) {
        mActiviyList = activiyList;
    }

    public PreferenceRepository getmPreferenceRepository() {
        return mPreferenceRepository;
    }

}
