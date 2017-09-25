package com.phicomm.remotecontrol.modules.main.controlpanel;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.phicomm.remotecontrol.BuildConfig;
import com.phicomm.remotecontrol.R;
import com.phicomm.remotecontrol.RemoteBoxDevice;
import com.phicomm.remotecontrol.base.BaseActivity;
import com.phicomm.remotecontrol.base.BaseApplication;
import com.phicomm.remotecontrol.constant.Commands;
import com.phicomm.remotecontrol.constant.KeyCode;
import com.phicomm.remotecontrol.constant.PhiConstants;
import com.phicomm.remotecontrol.modules.main.screenprojection.activities.LocalMediaItemActivity;
import com.phicomm.remotecontrol.modules.main.screenprojection.constants.DeviceDisplayListOperation;
import com.phicomm.remotecontrol.modules.main.screenprojection.entity.DeviceDisplay;
import com.phicomm.remotecontrol.modules.main.screenprojection.entity.DisplayDeviceList;
import com.phicomm.remotecontrol.modules.main.screenprojection.presenter.LocalMediaItemPresenter;
import com.phicomm.remotecontrol.modules.main.screenprojection.presenter.LocalMediaItemPresenterImpl;
import com.phicomm.remotecontrol.modules.main.screenshot.ScreenshotActivity;
import com.phicomm.remotecontrol.modules.main.spinnerlist.SpinnerListFragment;
import com.phicomm.remotecontrol.modules.personal.upgrade.UpdateInfoResponseBean;
import com.phicomm.remotecontrol.modules.personal.upgrade.UpdatePresenter;
import com.phicomm.remotecontrol.modules.personal.upgrade.UpdatePresenterImpl;
import com.phicomm.remotecontrol.modules.personal.upgrade.UpdateView;
import com.phicomm.remotecontrol.preference.PreferenceDef;
import com.phicomm.remotecontrol.preference.PreferenceRepository;
import com.phicomm.remotecontrol.util.ActivityUtils;
import com.phicomm.remotecontrol.util.CommonUtils;
import com.phicomm.remotecontrol.util.DevicesUtil;
import com.phicomm.remotecontrol.util.LogUtil;
import com.phicomm.remotecontrol.util.SettingUtil;
import com.phicomm.widgets.alertdialog.PhiGuideDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.OnClick;

public class CoreControlActivity extends BaseActivity implements UpdateView {
    private final static String TAG = "CoreControlActivity";
    static final int REQUEST_CODE = 101;
    private RemoteBoxDevice mRemoteBoxDevice;
    private DisplayDeviceList mDisplayDeviceList;
    private ProgressDialog mProgressDialog;
    private Context mContext;
    private DLNAHandler mDLNAHandler;
    private KeyPanelFragment mKeypanelFragment;
    private TouchPanelFragment mTouchPanelFragment;
    private ArrayList<Fragment> mFragmentList;
    private PanelContract.Presenter mPresenter;
    private LocalMediaItemPresenter mLocalMediaItemPresenter;
    private UpdatePresenter mUpdatePresenter;
    private PreferenceRepository mPreference;

    public class DLNAHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            DeviceDisplay mDeviceDisplay = (DeviceDisplay) msg.obj;
            switch (msg.what) {
                case DeviceDisplayListOperation.ADD:
                    LogUtil.d(TAG, "开始添加数据：" + mDeviceDisplay.getDevice().toString());
                    if (!mDisplayDeviceList.getDeviceDisplayList().contains(mDeviceDisplay)) {
                        mDisplayDeviceList.addDevice(mDeviceDisplay);
                    }
                    break;
                case DeviceDisplayListOperation.REMOVE:
                    if (mDisplayDeviceList.getDeviceDisplayList().contains(mDeviceDisplay)) {
                        mDisplayDeviceList.removeDevice(mDeviceDisplay);
                    }
                    break;
                case PhiConstants.BROADCAST_TIMEOUT:
                    mProgressDialog.dismiss();
                    LogUtil.d(TAG, "进入MyDialogHandler");
                    mLocalMediaItemPresenter.destory();
                    LogUtil.d(TAG, "后台已销毁dlna搜索：");
                    LogUtil.d(TAG, "目标设备是：" + DevicesUtil.getTarget());
                    LogUtil.d(TAG, "搜索到的设备是：" + mDisplayDeviceList);
                    if (DevicesUtil.getTarget() == null || mDisplayDeviceList == null || isSelectedDeviceNotOnline(DevicesUtil.getTarget(), mDisplayDeviceList.getDeviceDisplayList())) {
                        //if (mDisplayDeviceList == null || isSelectedDeviceNotOnline(DevicesUtil.getTarget(), mDisplayDeviceList.getDeviceDisplayList())) {
                        Toast.makeText(CoreControlActivity.this, "请先连接设备", Toast.LENGTH_SHORT).show();
                    } else {
                        startIntentToScreenProjection();
                    }
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        clearRestoreFragment(savedInstanceState);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_core_controler);
        mDisplayDeviceList = DisplayDeviceList.getInstance();
        mContext = this;
        mDLNAHandler = new DLNAHandler();
//        transStatusbar();
        initSpinner();
        initPanel();

        HeaderButtons heads = new HeaderButtons(findViewById(R.id.header_view));
        PanelContract.Presenter presenter = new PanelPresenter();
        heads.setPresenter(presenter);
        mLocalMediaItemPresenter = new LocalMediaItemPresenterImpl();

        mPreference = new PreferenceRepository(this);
        checkNewVersion();
    }

    @Override
    @OnClick({R.id.ib_screenshot, R.id.ib_screenprojection, R.id.ib_voice, R.id.ib_childrenlock, R.id.ib_clear})
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.ib_screenshot:
                CommonUtils.startIntent(this, null, ScreenshotActivity.class);
                break;
            case R.id.ib_screenprojection:
                if (DevicesUtil.getTarget() == null) {
                    Toast.makeText(mContext, "请先连接设备", Toast.LENGTH_SHORT).show();
                } else {
                    mLocalMediaItemPresenter.init(mContext, mDLNAHandler);
                    showProgressDialog();
                }
                break;
            case R.id.ib_voice:
                break;
            case R.id.ib_childrenlock:
                mPresenter.sendCommand(Commands.OPEN_LOCK);
                break;
            case R.id.ib_clear:
                mPresenter.sendCommand(Commands.OPEN_CLEAR);
                break;

        }
    }

    private void startIntentToScreenProjection() {
        LogUtil.d(TAG, "mDisplayDeviceList的个数是：" + mDisplayDeviceList.getDeviceDisplayList().size());
        DeviceDisplay mDisplayDevice = mDisplayDeviceList.getDeviceDisplayList().get(0);
        ((BaseApplication) getApplication()).setDeviceDisplay(mDisplayDevice);
        if (mDisplayDevice.getDevice().isFullyHydrated()) {
            CommonUtils.startIntent(this, null, LocalMediaItemActivity.class);
        }
    }

    private void showProgressDialog() {
        mProgressDialog = ProgressDialog.show(this, "提示", "正在初始化投屏设备，请等待");
        mDLNAHandler.sendEmptyMessageDelayed(PhiConstants.BROADCAST_TIMEOUT,
                PhiConstants.DISCOVERY_TIMEOUT);
    }

    private void clearRestoreFragment(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            String FRAGMENTS_TAG = "android:support:fragments";
            savedInstanceState.remove(FRAGMENTS_TAG);
        }
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
        mKeypanelFragment = KeyPanelFragment.newInstance();
        mKeypanelFragment.setPresenter(keyPresenter);

        PanelPresenter touchPresenter = new PanelPresenter();
        mTouchPanelFragment = TouchPanelFragment.newInstance();
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
        //PanelContract.Presenter mPresenter;
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
                    if (SettingUtil.isVibrateOn()) {
                        SettingUtil.doVibrate();
                    }

                    mPresenter.sendKeyEvent(KeyCode.POWER);
                }
            });
        }

        void initMenuButton() {
            ImageButton menuBtn = (ImageButton) mRootView.findViewById(R.id.btn_menu);
            menuBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (SettingUtil.isVibrateOn()) {
                        SettingUtil.doVibrate();
                    }

                    mPresenter.sendKeyEvent(KeyCode.MENU);
                }
            });
        }

        void initSettingButton() {
            ImageButton settingBtn = (ImageButton) mRootView.findViewById(R.id.btn_setting);
            settingBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (SettingUtil.isVibrateOn()) {
                        SettingUtil.doVibrate();
                    }

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

    private boolean isSelectedDeviceNotOnline(RemoteBoxDevice mRemoteBoxDevice, List<DeviceDisplay> mDeviceDisplayList) {
        String ip = mRemoteBoxDevice.getAddress();
        LogUtil.d(TAG, "搜到的设备有：" + mDeviceDisplayList.toString());
        //String ip = "172.20.10.9";//测试需要
        LogUtil.d(TAG, "当前选中设备的IP是:" + ip);
        for (int i = 0; i < mDeviceDisplayList.size(); i++) {
            LogUtil.d(TAG, "当前设备是：" + mDeviceDisplayList.get(i).getDevice().toString());
            if (mDeviceDisplayList.get(i).getDevice().toString().indexOf(ip) != -1) {
                LogUtil.d(TAG, "被选中的设备是：" + mDeviceDisplayList.get(i).getDevice().toString());
                return false;
            }
        }
        return true;
    }

    private void checkNewVersion() {
        mUpdatePresenter = new UpdatePresenterImpl(this, this);
        Map<String, String> options = new HashMap<>();
        options.put("appid", PhiConstants.APP_ID);
        options.put("channel", CommonUtils.getAppChannel());
        options.put("vercode", BuildConfig.VERSION_CODE + "");
        mUpdatePresenter.checkVersion(options);
    }

    @Override
    public void checkVersion(UpdateInfoResponseBean bean) {
        if (bean == null) {
            return;
        }
        String ret = bean.getRet();
        String updateType = bean.getVerType();
        boolean isForceUpdate = false;
        if (!TextUtils.isEmpty(updateType) && updateType.equals("1")) {
            isForceUpdate = true;
        }

        if (!TextUtils.isEmpty(ret)) {
            if (ret.equals("0")) {
                mPreference.put(PreferenceDef.APP_VERSION, PreferenceDef.IS_HAVE_NEW_VERSIOM, true);
                showUpdateInfoDialog(isForceUpdate, bean.getVerName(), bean.getVerInfos(), bean.getVerDown());

            } else {
                mPreference.put(PreferenceDef.APP_VERSION, PreferenceDef.IS_HAVE_NEW_VERSIOM, false);
                // CommonUtils.showShortToast(getString(R.string.current_version_newest));
            }
        }
    }

    private void showUpdateInfoDialog(final boolean isForceUpdate, final String versionName, String versionInfo, final String url) {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_content_for_update_info, null);
        TextView title = (TextView) view.findViewById(R.id.update_title);
        TextView message = (TextView) view.findViewById(R.id.update_message);
        title.setText(String.format(getString(R.string.version_update_title), versionName));
        message.setText(versionInfo);
        final PhiGuideDialog dialog = new PhiGuideDialog(this);
        dialog.setContentPanel(view);
        dialog.setLeftGuideOnclickListener(getResources().getString(R.string.update_later), R.color.syn_text_color, new PhiGuideDialog.onLeftGuideOnclickListener() {
            @Override
            public void onLeftGuideClick() {
                if (!isForceUpdate) {
                    dialog.dismiss();
                }
            }

        });
        dialog.setRightGuideOnclickListener(getResources().getString(R.string.update_now), R.color.weight_line_color, new PhiGuideDialog.onRightGuideOnclickListener() {
            @Override
            public void onRightGuideClick() {
                if (!isForceUpdate) {
                    dialog.dismiss();
                }
                mUpdatePresenter.downloadFile(url, versionName);
            }
        });
        if (isForceUpdate) {
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
        }
        dialog.show();
    }

    @Override
    public void showMessage(Object message) {

    }

    @Override
    public void onSuccess(Object message) {

    }

    @Override
    public void onFailure(Object message) {

    }
}
