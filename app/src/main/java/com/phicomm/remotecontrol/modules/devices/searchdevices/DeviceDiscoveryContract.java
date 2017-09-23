package com.phicomm.remotecontrol.modules.devices.searchdevices;

import com.phicomm.remotecontrol.RemoteBoxDevice;
import com.phicomm.remotecontrol.base.BasePresenter;
import com.phicomm.remotecontrol.base.BaseView;

import java.util.List;

/**
 * Created by chunya02.li on 2017/7/11.
 */

public class DeviceDiscoveryContract {
    interface View extends BaseView<Presenter> {

        void refreshListView(List<RemoteBoxDevice> current_list);

        void setTittle(String str);

        void setTittle(int intId);

        void showToast(String str);

        void stopIPConnectProgressBar();

        void showConnectFailDialog();
    }

    interface Presenter extends BasePresenter {
        List<RemoteBoxDevice> getCurrentDeviceList();

        void setCurrentDeviceList(List<RemoteBoxDevice> current_List);

        RemoteBoxDevice getTarget();

        void loadRecentList();

        void insertOrUpdateRecentDevices(RemoteBoxDevice device);

        void ipConnect(String ip);

        void ipConnectAgain(String ip);

        void removeItemAndRefreshView(RemoteBoxDevice device);

        void addDeviceItem(List<RemoteBoxDevice> mOnResumeRemoteBoxDevice, RemoteBoxDevice mDevice);

        boolean isContains(List<RemoteBoxDevice> mOnResumeRemoteBoxDevice, RemoteBoxDevice mDevice);
    }
}
