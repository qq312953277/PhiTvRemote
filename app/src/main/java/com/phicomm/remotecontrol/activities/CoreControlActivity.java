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
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;

import com.phicomm.remotecontrol.R;
import com.phicomm.remotecontrol.constant.Commands;
import com.phicomm.remotecontrol.constant.KeyCode;
import com.phicomm.remotecontrol.fragments.controlpanel.KeyPanelFregment;
import com.phicomm.remotecontrol.fragments.controlpanel.PanelContract;
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
        setContentView(R.layout.activity_core_controler);
        transStatusbar();
        initSpinner();
        initPanel();

        HeaderButtons heads = new HeaderButtons(findViewById(R.id.header_view));
        PanelContract.Presenter presenter = new PanelPresenter();
        heads.setPresenter(presenter);

    }

    private void initSpinner() {
        SpinnerListFragment spinnerListFragment = (SpinnerListFragment)
                getSupportFragmentManager().findFragmentById(R.id.spinner_container);

        if (spinnerListFragment == null) {
            spinnerListFragment = SpinnerListFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    spinnerListFragment, R.id.spinner_container);
        }
    }

    private void initPanel() {
        PanelPresenter keyPresenter = new PanelPresenter();
        mKeypanelFragment = KeyPanelFregment.newInstance();
        mKeypanelFragment.setPresenter(keyPresenter);

        PanelPresenter touchPresenter = new PanelPresenter();
        mTouchPanelFragment = TouchPanelFregment.newInstance();
        mTouchPanelFragment.setPresenter(touchPresenter);

        mFragmentList = new ArrayList<Fragment>();
        mFragmentList.add(mKeypanelFragment);
        mFragmentList.add(mTouchPanelFragment);

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

    private void transStatusbar() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }

    class HeaderButtons implements PanelContract.View {
        View mRootView;
        PanelContract.Presenter mPresenter;
        private Toast mToast;

        HeaderButtons(View view) {
            mRootView = view;
            initPowerButton();
            initMenuButton();
            initSettingButton();
        }

        void initPowerButton() {
            ImageButton powerBtn = (ImageButton) mRootView.findViewById(R.id.btn_power);
            powerBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPresenter.sendKeyEvent(KeyCode.POWER);
                }
            });
        }

        void initMenuButton() {
            ImageButton menuBtn = (ImageButton) mRootView.findViewById(R.id.btn_menu);
            menuBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPresenter.sendKeyEvent(KeyCode.MENU);
                }
            });
        }

        void initSettingButton() {
            ImageButton settingBtn = (ImageButton) mRootView.findViewById(R.id.btn_setting);
            settingBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPresenter.sendCommand(Commands.OPEN_SETTING);
                }
            });
        }

        @Override
        public void setPresenter(PanelContract.Presenter presenter) {
            mPresenter = presenter;
            mPresenter.setView(this);
        }

        @Override
        public void toastMessage(String msg) {
            if (mToast == null) {
                mToast = Toast.makeText(CoreControlActivity.this, msg, Toast.LENGTH_SHORT);
            } else {
                mToast.setText(msg);
            }
            mToast.show();
        }
    }
}
