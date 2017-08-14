package com.phicomm.remotecontrol.modules.devices.searchdevices;

import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;

import com.phicomm.remotecontrol.R;

import static com.phicomm.remotecontrol.util.ActivityUtils.*;

/**
 * Created by chunya02.li on 2017/7/11.
 */

public class DeviceDiscoveryActivity extends AppCompatActivity {
    private DeviceDiscoveryPresenter mDiscoveryDevicePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discovery_device);
        DeviceDiscoveryFragment deviceDiscoveryFragment = (DeviceDiscoveryFragment)
                getSupportFragmentManager().findFragmentById(R.id.layout_single_container);
        // new fragment
        if (deviceDiscoveryFragment == null) {
            deviceDiscoveryFragment = DeviceDiscoveryFragment.newInstance();
            addFragmentToActivity(getSupportFragmentManager(),
                    deviceDiscoveryFragment, R.id.layout_single_container);
        }
        mDiscoveryDevicePresenter = new DeviceDiscoveryPresenter(deviceDiscoveryFragment, this);
    }
}

