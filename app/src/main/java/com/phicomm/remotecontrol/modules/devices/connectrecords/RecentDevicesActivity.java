package com.phicomm.remotecontrol.modules.devices.connectrecords;

import android.os.Bundle;

import com.phicomm.remotecontrol.R;
import com.phicomm.remotecontrol.base.BaseActivity;

import static com.phicomm.remotecontrol.util.ActivityUtils.addFragmentToActivity;

/**
 * Created by chunya02.li on 2017/7/10.
 */

public class RecentDevicesActivity extends BaseActivity {
    public RecentDevicesPresenter mRecentPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_device);
        RecentDevicesFragment recentDevicesFragment = (RecentDevicesFragment)
                getSupportFragmentManager().findFragmentById(R.id.layout_single_container);
        if (recentDevicesFragment == null) {
            recentDevicesFragment = RecentDevicesFragment.newInstance();
            addFragmentToActivity(getSupportFragmentManager(),
                    recentDevicesFragment, R.id.layout_single_container);
        }
        mRecentPresenter = new RecentDevicesPresenter(recentDevicesFragment);
    }
}
