package com.phicomm.remotecontrol.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.phicomm.remotecontrol.R;
import com.phicomm.remotecontrol.fragments.controlpanel.KeyPanelFregment;
import com.phicomm.remotecontrol.fragments.controlpanel.PanelPresenter;
import com.phicomm.remotecontrol.fragments.controlpanel.TouchPanelFregment;
import com.phicomm.remotecontrol.fragments.controlpanel.ViewPageAdapter;
import com.phicomm.remotecontrol.fragments.spinnerlist.SpinnerListFragment;
import com.phicomm.remotecontrol.util.ActivityUtils;
import com.phicomm.remotecontrol.util.LogUtil;

import java.util.ArrayList;

public class CoreControlActivity extends AppCompatActivity {

    static final int REQUEST_CODE = 101;

    private KeyPanelFregment mKeypanelFragment;
    private TouchPanelFregment mTouchPanelFragment;
    private ArrayList<Fragment> mFragmentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.core_controler);

        PanelPresenter keyPresenter = new PanelPresenter();
        mKeypanelFragment = KeyPanelFregment.newInstance();
        mKeypanelFragment.setPresenter(keyPresenter);

        PanelPresenter touchPresenter = new PanelPresenter();
        mTouchPanelFragment = TouchPanelFregment.newInstance();
        mTouchPanelFragment.setPresenter(touchPresenter);

        mFragmentList = new ArrayList<Fragment>();
        mFragmentList.add(mKeypanelFragment);
        mFragmentList.add(mTouchPanelFragment);

        SpinnerListFragment spinnerListFragment = (SpinnerListFragment)
                getSupportFragmentManager().findFragmentById(R.id.spinner_container);

        if (spinnerListFragment == null) {
            spinnerListFragment = SpinnerListFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    spinnerListFragment, R.id.spinner_container);
        }

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPageMainContent);
        viewPager.setAdapter(new ViewPageAdapter(getSupportFragmentManager(), mFragmentList));
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtil.d("activity onresume");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}
