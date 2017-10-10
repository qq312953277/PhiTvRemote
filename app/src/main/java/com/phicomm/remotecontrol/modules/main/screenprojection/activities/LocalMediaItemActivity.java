package com.phicomm.remotecontrol.modules.main.screenprojection.activities;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.phicomm.remotecontrol.R;
import com.phicomm.remotecontrol.RemoteBoxDevice;
import com.phicomm.remotecontrol.base.BaseActivity;
import com.phicomm.remotecontrol.base.BaseApplication;
import com.phicomm.remotecontrol.base.BaseFragment;
import com.phicomm.remotecontrol.constant.PhiConstants;
import com.phicomm.remotecontrol.modules.main.screenprojection.adapter.GeneralAdapter;
import com.phicomm.remotecontrol.modules.main.screenprojection.constants.DeviceDisplayListOperation;
import com.phicomm.remotecontrol.modules.main.screenprojection.entity.ContentItem;
import com.phicomm.remotecontrol.modules.main.screenprojection.entity.DeviceDisplay;
import com.phicomm.remotecontrol.modules.main.screenprojection.entity.DisplayDeviceList;
import com.phicomm.remotecontrol.modules.main.screenprojection.fragments.PictureFragment;
import com.phicomm.remotecontrol.modules.main.screenprojection.fragments.VideoFragment;
import com.phicomm.remotecontrol.modules.main.screenprojection.presenter.LocalMediaItemPresenter;
import com.phicomm.remotecontrol.modules.main.screenprojection.presenter.LocalMediaItemPresenterImpl;
import com.phicomm.remotecontrol.util.CommonUtils;
import com.phicomm.remotecontrol.util.DevicesUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.phicomm.remotecontrol.constant.PhiConstants.TITLE_BAR_HEIGHT_DP;


/**
 * Created by kang.sun on 2017/9/27.
 */

public class LocalMediaItemActivity extends BaseActivity implements MyFragmentAdapter.GetFragmentCallback, LocalMediaItemView {
    public LocalMediaItemPresenter mLocalMediaItemPresenter;
    private int mFragmentFlag = 0;//照片页面
    private MyFragmentAdapter pageAdapter;
    Handler mDLNAHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            DeviceDisplay mDeviceDisplay = (DeviceDisplay) msg.obj;
            switch (msg.what) {
                case DeviceDisplayListOperation.ADD:
                    if (!mDisplayDeviceList.getDeviceDisplayList().contains(mDeviceDisplay)) {
                        mDisplayDeviceList.addDevice(mDeviceDisplay);
                        Log.d(TAG, "搜索到的DLNA设备是：" + mDeviceDisplay);
                    }
                    break;
                case DeviceDisplayListOperation.REMOVE:
                    if (mDisplayDeviceList.getDeviceDisplayList().contains(mDeviceDisplay)) {
                        mDisplayDeviceList.removeDevice(mDeviceDisplay);
                    }
                    break;
            }
        }
    };

    @BindView(R.id.viewPager)
    CustomViewPager mViewPage;

    @BindView(R.id.radio_group)
    RadioGroup mRadioGroup;

    @BindView(R.id.picture)
    RadioButton mPic;

    @BindView(R.id.video)
    RadioButton mVid;

    @BindView(R.id.rl_title)
    RelativeLayout mRlTitle;

    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @BindView(R.id.iv_back)
    ImageView mBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("LocalMediaItemActivity", "LocalMediaItemActivity onCreate");
        setContentView(R.layout.activity_screenprojection);
        ButterKnife.bind(this);
        init();
        getDataLogic(0);
    }

    private void init() {
        setMarginForStatusBar(mRlTitle, TITLE_BAR_HEIGHT_DP);
        mTvTitle.setText(getString(R.string.local_screenprojection));
        setupPager();
        mDisplayDeviceList = DisplayDeviceList.getInstance();
        mLocalMediaItemPresenter = new LocalMediaItemPresenterImpl(this, this, (BaseApplication) getApplication());
    }

    private void getDataLogic(final int type) {
        showProgressDialog();
        mLocalMediaItemPresenter.init(this, mDLNAHandler, type);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finishScan(type);
            }
        }, PhiConstants.DISCOVER_DLNA_TIMEOUT);
    }

    private void finishScan(int type) {
        mProgressDialog.dismiss();
        mLocalMediaItemPresenter.destory();
        Log.d(TAG, "目标设备是：" + DevicesUtil.getTarget());
        if (null == DevicesUtil.getTarget() || mDisplayDeviceList == null || isSelectedDeviceNotOnline(DevicesUtil.getTarget(), mDisplayDeviceList.getDeviceDisplayList())) {
            CommonUtils.showShortToast("初始化投屏失败");
        } else {
            DeviceDisplay mDisplayDevice = mDisplayDeviceList.getDeviceDisplayList().get(0);
            ((BaseApplication) getApplication()).setDeviceDisplay(mDisplayDevice);
            if (mDisplayDevice.getDevice().isFullyHydrated()) {
                mLocalMediaItemPresenter.getItems(type);
            }
        }
    }

    @Override
    @OnClick({R.id.picture, R.id.video, R.id.iv_back})
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.picture:
                mFragmentFlag = 0;
                mViewPage.setCurrentItem(0, false);
                getDataLogic(0);
                break;
            case R.id.video:
                mFragmentFlag = 1;
                mViewPage.setCurrentItem(1, false);
                getDataLogic(1);
                break;
            case R.id.iv_back:
                if ((mFragmentFlag == 0) && (PictureFragment.mLayer == 1)) {//处于图片界面次层
                    mFragmentFlag = 0;
                    PictureFragment.mLayer = 0;
                    mViewPage.setCurrentItem(0, false);
                    getDataLogic(0);
                } else if ((mFragmentFlag == 1) && (VideoFragment.mLayer == 1)) {//处于视频界面次层
                    mFragmentFlag = 1;
                    VideoFragment.mLayer = 0;
                    mViewPage.setCurrentItem(1, false);
                    getDataLogic(1);
                } else {
                    finish();
                }
                break;
        }
    }

    public void setupPager() {
        pageAdapter = new MyFragmentAdapter(getSupportFragmentManager(), this);
        mViewPage.setOffscreenPageLimit(pageAdapter.getCount());
        mViewPage.setAdapter(pageAdapter);
        mViewPage.setNoScroll(true);
    }

    @Override
    public void initFragmentList(android.support.v4.app.FragmentManager fm, List<android.support.v4.app.Fragment> fragmentList) {
        for (MainFragmentTab tab : MainFragmentTab.values()) {
            try {
                BaseFragment fragment = null;
                List<Fragment> fs = fm.getFragments();
                if (fs != null) {
                    for (Fragment f : fs) {
                        if (f.getClass() == tab.clazz) {
                            fragment = (BaseFragment) f;
                            break;
                        }
                    }
                }
                if (fragment == null) {
                    fragment = tab.clazz.newInstance();
                }
                fragmentList.add(fragment);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return null;
    }

    private final static String TAG = "LocalMediaItemActivity";
    private ProgressDialog mProgressDialog;
    private DisplayDeviceList mDisplayDeviceList;

    @Override
    public void showMessage(Object message) {
    }

    @Override
    public void onSuccess(Object message) {
    }

    @Override
    public void onFailure(Object message) {
    }

    @Override
    public void showItems(int type, GeneralAdapter<ContentItem> mContentAdapter) {
        EventBus.getDefault().post(new PictureEvent(type, mContentAdapter, mLocalMediaItemPresenter));
    }

    private void showProgressDialog() {
        mProgressDialog = ProgressDialog.show(this, getString(R.string.tips), getString(R.string.tips_content));
    }

    private boolean isSelectedDeviceNotOnline(RemoteBoxDevice mRemoteBoxDevice, List<DeviceDisplay> mDeviceDisplayList) {
        String ip = mRemoteBoxDevice.getAddress();
        for (int i = 0; i < mDeviceDisplayList.size(); i++) {
            if (mDeviceDisplayList.get(i).getDevice().toString().indexOf(ip) != -1) {
                return false;
            }
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocalMediaItemPresenter.restore();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((mFragmentFlag == 0) && (PictureFragment.mLayer == 1)) {
                mFragmentFlag = 0;
                PictureFragment.mLayer = 0;
                mViewPage.setCurrentItem(0, false);
                getDataLogic(0);
            } else if ((mFragmentFlag == 1) && (VideoFragment.mLayer == 1)) {
                mFragmentFlag = 1;
                VideoFragment.mLayer = 0;
                mViewPage.setCurrentItem(1, false);
                getDataLogic(1);
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}

