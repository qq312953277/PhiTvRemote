package com.phicomm.remotecontrol.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.phicomm.remotecontrol.R;
import com.phicomm.remotecontrol.fragments.recentdevices.RecentDevicesFragment;
import com.phicomm.remotecontrol.fragments.recentdevices.RecentDevicesPresenter;
import com.phicomm.remotecontrol.util.ActivityUtils;

/**
 * Created by chunya02.li on 2017/7/10.
 */

public class RecentDevicesActivity extends AppCompatActivity {
    private RecentDevicesPresenter mRecentPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_device);
        RecentDevicesFragment recentDevicesFragment = (RecentDevicesFragment)
                getSupportFragmentManager().findFragmentById(R.id.layout_single_container);
        if (recentDevicesFragment == null) {
            recentDevicesFragment = RecentDevicesFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    recentDevicesFragment, R.id.layout_single_container);
        }
        mRecentPresenter = new RecentDevicesPresenter(recentDevicesFragment);
    }
}
