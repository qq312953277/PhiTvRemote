package com.phicomm.remotecontrol.DeviceDiscovery;

import java.util.List;

import com.phicomm.remotecontrol.RemoteBoxDevice;
import com.phicomm.remotecontrol.Util.BasePresenter;
import com.phicomm.remotecontrol.Util.BaseView;

/**
 * Created by chunya02.li on 2017/7/11.
 */

public class DeviceDiscoveryContract {
    interface View extends BaseView<Presenter> {
        void refreshListView(List<RemoteBoxDevice> current_list);
    }

    interface Presenter extends BasePresenter {
        List<RemoteBoxDevice> getCurrentDeviceList();

        void setCurrentDeviceList(List<RemoteBoxDevice> current_List);

        RemoteBoxDevice getTarget();

        void loadRecentList();

        void insertOrUpdateRecentDevices(RemoteBoxDevice device);
    }
}
