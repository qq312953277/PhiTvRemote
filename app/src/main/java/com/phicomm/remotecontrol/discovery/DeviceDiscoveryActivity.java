package com.phicomm.remotecontrol.discovery;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.WindowCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.phicomm.remotecontrol.constant.PhiConstants;
import com.phicomm.remotecontrol.R;
import com.phicomm.remotecontrol.util.ActivityUtils;

/**
 * Created by chunya02.li on 2017/7/11.
 */

public class DeviceDiscoveryActivity extends AppCompatActivity {
    private DeviceDiscoveryPresenter mDiscoveryDevicePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String title = bundle.getString(PhiConstants.ACTION_BAR_NAME);

        supportRequestWindowFeature(WindowCompat.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_discovery_device);
        DeviceDiscoveryFragment deviceDiscoveryFragment = (DeviceDiscoveryFragment)
                getSupportFragmentManager().findFragmentById(R.id.layout_single_container);
        // new fragment
        if (deviceDiscoveryFragment == null) {
            deviceDiscoveryFragment = DeviceDiscoveryFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    deviceDiscoveryFragment, R.id.layout_single_container);
        }
        //action Bar set
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setTitle(title);
        mDiscoveryDevicePresenter = new DeviceDiscoveryPresenter(deviceDiscoveryFragment, this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

