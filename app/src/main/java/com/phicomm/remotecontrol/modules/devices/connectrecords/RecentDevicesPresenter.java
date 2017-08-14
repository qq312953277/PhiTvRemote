package com.phicomm.remotecontrol.modules.devices.connectrecords;

import com.phicomm.remotecontrol.modules.devices.connectrecords.RecentDevicesContract.View;
import com.phicomm.remotecontrol.greendao.Entity.RemoteDevice;
import com.phicomm.remotecontrol.greendao.GreenDaoUserUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chunya02.li on 2017/7/10.
 */

public class RecentDevicesPresenter implements RecentDevicesContract.Presenter {
    private static String TAG = "recentPresent";

    private View mView;
    private GreenDaoUserUtil mGreenDaoUserUtil;
    private List<RemoteDevice> mRecentConnectList;

    public RecentDevicesPresenter(View view) {
        mView = view;
        mView.setPresenter(this);
        mGreenDaoUserUtil = new GreenDaoUserUtil();
        mRecentConnectList = new ArrayList<>(0);
    }

    @Override
    public void start() {
        loadRecentDevices();
    }

    @Override
    public void stop() {

    }

    @Override
    public List<RemoteDevice> loadRecentDevices() {
        mRecentConnectList = mGreenDaoUserUtil.loadAllRecentByTimeOrder();
        mView.showRecentDevices(mRecentConnectList);
        return mRecentConnectList;
    }

    @Override
    public void removeSelectedDevice(List<RemoteDevice> listDelete) {
        List<Long> listDeleteId = new ArrayList<>(0);
        for (int i = 0; i < listDelete.size(); i++) {
            listDeleteId.add(listDelete.get(i).getId());
        }
        mGreenDaoUserUtil.deleteBatchByKey(listDeleteId);
        mView.removeItems(listDelete);
    }
}
