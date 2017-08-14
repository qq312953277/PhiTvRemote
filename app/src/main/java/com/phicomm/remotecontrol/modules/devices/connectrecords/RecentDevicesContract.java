package com.phicomm.remotecontrol.modules.devices.connectrecords;

import java.util.List;
import com.phicomm.remotecontrol.base.BasePresenter;
import com.phicomm.remotecontrol.base.BaseView;
import com.phicomm.remotecontrol.greendao.Entity.RemoteDevice;

/**
 * Created by chunya02.li on 2017/7/10.
 */

public class RecentDevicesContract {
    interface View extends BaseView<Presenter> {
        void showRecentDevices(List<RemoteDevice> devices);

        void removeItems(List<RemoteDevice> list_delete);
    }

    interface Presenter extends BasePresenter {
        List<RemoteDevice> loadRecentDevices();

        void removeSelectedDevice(List<RemoteDevice> removeDevics);
    }
}
