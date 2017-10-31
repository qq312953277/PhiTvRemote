package com.phicomm.remotecontrol.modules.main.controlpanel;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.phicomm.remotecontrol.BuildConfig;
import com.phicomm.remotecontrol.ConnectManager;
import com.phicomm.remotecontrol.R;
import com.phicomm.remotecontrol.base.BaseActivity;
import com.phicomm.remotecontrol.constant.Commands;
import com.phicomm.remotecontrol.constant.PhiConstants;
import com.phicomm.remotecontrol.modules.main.screenprojection.activities.LocalMediaItemActivity;
import com.phicomm.remotecontrol.modules.main.screenprojection.entity.DisplayDeviceList;
import com.phicomm.remotecontrol.modules.main.screenprojection.presenter.LocalMediaItemPresenter;
import com.phicomm.remotecontrol.modules.main.screenprojection.presenter.LocalMediaItemPresenterImpl;
import com.phicomm.remotecontrol.modules.main.screenshot.ScreenshotActivity;
import com.phicomm.remotecontrol.modules.main.spinnerlist.SpinnerListFragment;
import com.phicomm.remotecontrol.modules.personal.upgrade.UpdateInfoResponseBean;
import com.phicomm.remotecontrol.modules.personal.upgrade.UpdatePresenter;
import com.phicomm.remotecontrol.modules.personal.upgrade.UpdatePresenterImpl;
import com.phicomm.remotecontrol.modules.personal.upgrade.UpdateService;
import com.phicomm.remotecontrol.modules.personal.upgrade.UpdateView;
import com.phicomm.remotecontrol.preference.PreferenceDef;
import com.phicomm.remotecontrol.preference.PreferenceRepository;
import com.phicomm.remotecontrol.util.ActivityUtils;
import com.phicomm.remotecontrol.util.CommonUtils;
import com.phicomm.remotecontrol.util.DevicesUtil;
import com.phicomm.remotecontrol.util.FileUtil;
import com.phicomm.widgets.alertdialog.PhiGuideDialog;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class CoreControlActivity extends BaseActivity implements UpdateView, SpinnerListFragment.ShowGrayLayout {

    @BindView(R.id.tab_first)
    ImageView mTabFirst;

    @BindView(R.id.tab_second)
    ImageView mTabSecond;

    @BindView(R.id.spinner_container)
    FrameLayout frameLayout;

    @BindView(R.id.gray_layout)
    View mGrayLayout;

    private final static String TAG = "CoreControlActivity";
    static final int REQUEST_CODE = 101;
    private final int POPWINDOW_FLAG = 0;
    private final int POPWINDOW_DELAY = 1000;
    private DisplayDeviceList mDisplayDeviceList;
    private Context mContext;
    private KeyPanelFragment mKeypanelFragment;
    private TouchPanelFragment mTouchPanelFragment;
    private ArrayList<Fragment> mFragmentList;
    private PanelContract.Presenter mPresenter;
    private LocalMediaItemPresenter mLocalMediaItemPresenter;
    private UpdatePresenter mUpdatePresenter;
    private PreferenceRepository mPreference;
    private long mFirstTime;
    private PopupWindow wifyNotAvailablePopWindow;
    private WifiAvailableReceiver wifiAvailableReceiver;
    private Handler popWindowShowDelayed = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == POPWINDOW_FLAG) {
                wifyNotAvailablePopWindow.showAsDropDown(frameLayout); //进入APP无wifi时需要延时等待父窗口加载完才能展示PopWindow，否则报错
            }
        }
    };

    @Override
    public void callback(boolean isVisible) {
        //控制阴影逻辑
        if (isVisible) {
            mGrayLayout.setVisibility(View.VISIBLE);
        } else {
            mGrayLayout.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        clearRestoreFragment(savedInstanceState);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_core_controler);
        //transStatusbar();
        mDisplayDeviceList = DisplayDeviceList.getInstance();
        mContext = this;
        initSpinner();
        initPanel();
        HeaderButtons heads = new HeaderButtons(findViewById(R.id.header_view));
        PanelContract.Presenter presenter = new PanelPresenter();
        heads.setPresenter(presenter);
        mLocalMediaItemPresenter = new LocalMediaItemPresenterImpl();
        mPreference = new PreferenceRepository(this);
        checkNewVersion();
    }

    private void transStatusbar() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//状态栏
    }

    @Override
    @OnClick({R.id.ib_screenshot, R.id.ib_screenprojection, R.id.ib_childrenlock, R.id.ib_clear})
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
                    Intent intent = new Intent();
                    intent.setClass(this, LocalMediaItemActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.ib_childrenlock:
                mPresenter.sendCommand(Commands.OPEN_LOCK);
                break;
            case R.id.ib_clear:
                mPresenter.sendCommand(Commands.OPEN_CLEAR);
                break;
        }
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
        //监听popwindow阴影效果
        spinnerListFragment.setGrayLayoutListener(this);
        View view = LayoutInflater.from(this).inflate(R.layout.ppw_wify_not_available, null);
        wifyNotAvailablePopWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        wifyNotAvailablePopWindow.setAnimationStyle(R.style.popwin_anim_style);//设置pop动画
        RelativeLayout mRlWifyNotAvailable = (RelativeLayout) view.findViewById(R.id.rl_wifi_not_available);
        mRlWifyNotAvailable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
            }
        });
    }

    private void initPanel() {
        PanelPresenter keyPresenter = new PanelPresenter();
        mKeypanelFragment = KeyPanelFragment.newInstance();
        mKeypanelFragment.setPresenter(keyPresenter);
        PanelPresenter touchPresenter = new PanelPresenter();
        mTouchPanelFragment = TouchPanelFragment.newInstance();
        mTouchPanelFragment.setPresenter(touchPresenter);
        mFragmentList = new ArrayList<>();
        mFragmentList.add(mKeypanelFragment);
        mFragmentList.add(mTouchPanelFragment);
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPageMainContent);
        viewPager.setAdapter(new ViewPageAdapter(getSupportFragmentManager(), mFragmentList));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        mTabFirst.setImageDrawable(getResources().getDrawable(R.drawable.tab_on));
                        mTabSecond.setImageDrawable(getResources().getDrawable(R.drawable.tab_off));
                        break;
                    case 1:
                        mTabFirst.setImageDrawable(getResources().getDrawable(R.drawable.tab_off));
                        mTabSecond.setImageDrawable(getResources().getDrawable(R.drawable.tab_on));
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
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
        //注册广播
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        wifiAvailableReceiver = new WifiAvailableReceiver();
        registerReceiver(wifiAvailableReceiver, filter);
        //检测设备是否在线
        ConnectManager.getInstance().deviceDetect();
    }

    @Override
    public void onPause() {
        //注销广播
        unregisterReceiver(wifiAvailableReceiver);
        super.onPause();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    class HeaderButtons implements PanelContract.View {
        View mRootView;
        //PanelContract.Presenter mPresenter;
        private Toast mToast;

        HeaderButtons(View view) {
            mRootView = view;
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

        @Override
        public void connectFail() {
            EventBus.getDefault().post(new LogoffNoticeEvent(true));
            DevicesUtil.setTarget(null);
            CommonUtils.showShortToast(getString(R.string.unable_to_connect_device));
        }
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
    public void onEventMainThread(DeviceDetectEvent event) {
        if (!event.getTargetState()) {
            DevicesUtil.setTarget(null);
            EventBus.getDefault().post(new LogoffNoticeEvent(true));
        }
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
                String path = Environment.getExternalStoragePublicDirectory(UpdateService.DirName) + File.separator;
                FileUtil.deleteFolder(path);
            }
        }
    }

    private void showUpdateInfoDialog(final boolean isForceUpdate, final String versionName, String versionInfo, final String url) {
        String hint;
        if (isForceUpdate) {
            hint = null;
        } else {
            hint = getResources().getString(R.string.update_later);
        }
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_content_for_update_info, null);
        TextView title = (TextView) view.findViewById(R.id.update_title);
        TextView message = (TextView) view.findViewById(R.id.update_message);
        title.setText(String.format(getString(R.string.version_update_title), versionName));
        message.setText(versionInfo);
        final PhiGuideDialog dialog = new PhiGuideDialog(this);
        dialog.setContentPanel(view);
        dialog.setLeftGuideOnclickListener(hint, R.color.syn_text_color, new PhiGuideDialog.onLeftGuideOnclickListener() {
            @Override
            public void onLeftGuideClick() {
                dialog.dismiss();
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
        dialog.show();
        if (isForceUpdate) {
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
        }
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("LocalMediaItemActivity", "CoreControlActivity onDestroy");
    }

    @Override
    public void onBackPressed() {
        long secondTime = System.currentTimeMillis();
        if (secondTime - mFirstTime > 2000) {
            CommonUtils.showShortToast(getString(R.string.exit_application_hint));
            mFirstTime = secondTime;
        } else {
            finish();
            System.exit(0);
        }
    }

    class WifiAvailableReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(action)) {
                int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, -1);
                if (wifiState == WifiManager.WIFI_STATE_DISABLED) {
                    popWindowShowDelayed.sendEmptyMessageDelayed(POPWINDOW_FLAG, POPWINDOW_DELAY);//延时1s显示popwindow，解决首次进入APP无wifi时报错问题
                }
            }
            if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(action)) {
                Parcelable parcelableExtra = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                if (null != parcelableExtra) {
                    NetworkInfo networkInfo = (NetworkInfo) parcelableExtra;
                    NetworkInfo.State state = networkInfo.getState();
                    if (state == NetworkInfo.State.CONNECTED && wifyNotAvailablePopWindow != null) {
                        wifyNotAvailablePopWindow.dismiss();
                    }
                }
            }
        }
    }
}
