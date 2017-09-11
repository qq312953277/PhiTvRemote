package com.phicomm.remotecontrol.modules.main.screenprojection.service;

import org.fourthline.cling.UpnpServiceConfiguration;
import org.fourthline.cling.android.AndroidUpnpServiceConfiguration;
import org.fourthline.cling.android.AndroidUpnpServiceImpl;

/**
 * Created by kang.sun on 2017/8/21.
 */
public class UpnpDeviceService extends AndroidUpnpServiceImpl {
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected UpnpServiceConfiguration createConfiguration() {
        return new AndroidUpnpServiceConfiguration() {
            @Override
            public int getRegistryMaintenanceIntervalMillis() {

                return 7000;
            }
//			@Override
//			public ServiceType[] getExclusiveServiceTypes() {
//				// 过滤要搜索的服务类型
//				return new ServiceType[] { new UDAServiceType("RenderingControl", 1) };
//			}
        };
    }
}

