package com.phicomm.remotecontrol.modules.devices.searchdevices;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.phicomm.remotecontrol.ConnectManager;
import com.phicomm.remotecontrol.R;
import com.phicomm.remotecontrol.RemoteBoxDevice;
import com.phicomm.remotecontrol.base.BaseFragment;
import com.phicomm.remotecontrol.constant.PhiConstants;
import com.phicomm.remotecontrol.modules.devices.connectrecords.RecentDevicesActivity;
import com.phicomm.remotecontrol.modules.devices.searchdevices.DeviceDiscoveryContract.Presenter;
import com.phicomm.remotecontrol.util.DevicesUtil;
import com.phicomm.remotecontrol.util.LogUtil;
import com.phicomm.remotecontrol.util.SettingUtil;
import com.tandong.bottomview.view.BottomView;

import java.util.Formatter;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.content.Context.WIFI_SERVICE;
import static com.phicomm.remotecontrol.R.id.bt_cancelinput;
import static com.phicomm.remotecontrol.R.id.bt_confirminput;
import static com.phicomm.remotecontrol.R.id.ipConnectProgressBar;
import static com.phicomm.remotecontrol.constant.PhiConstants.TITLE_BAR_HEIGHT_DP;


/**
 * Created by chunya02.li on 2017/7/11.
 */

public class DeviceDiscoveryFragment extends BaseFragment implements DeviceDiscoveryContract.View {
    private static String TAG = "DeviceDiscoveryFragment";
    @BindView(ipConnectProgressBar)
    ProgressBar mConnectProgressBar;
    @BindView(R.id.discovery_devices_list)
    public ListView mDiscoveryListDevices;
    @BindView(R.id.start_discovery)
    public Button mDiscoveryBtn;
    @BindView(R.id.manual_ip)
    public Button mManualIpBtn;
    @BindView(R.id.tv_right)
    public TextView mTvRecords;
    @BindView(R.id.local_networkname)
    public TextView mNetworkNameTv;
    @BindView(R.id.choose_text)
    public TextView mChooseTv;
    @BindView(R.id.device_count)
    public TextView mCountTv;
    @BindView(android.R.id.empty)
    public TextView mEmptyTv;
    @BindView(R.id.discovery_progressbar)
    DiscoveryProgressbar mDiscoveryProgressbar;
    @BindView(R.id.discovery_progress_tv)
    TextView mDiscoveryProgressTv;
    @BindView(R.id.discovery_progress_view)
    FrameLayout mDiscoveryProgressView;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.rl_title)
    RelativeLayout mRlTitle;
    private Presenter mPresenter;
    private DeviceDiscoveryAdapter mDiscoveryAdapter;
    private WifiManager mWifiManager;
    private WifiChangeReceiver mWifiChangeReceiver;
    private BottomView mBottomView;
    private GridView mIPGridView;
    private ArrayAdapter mIPAdapter;
    private EditText mIPInputEditText;
    private Button mIPConfirmBt;
    private Button mIPCancleBt;
    public static final int COMPLETE_PROCESS = 100;
    public static final int SLEEP_TIME = 100;
    private final String[] mIPBtns = new String[]{
            "1", "2", "3",
            "4", "5", "6",
            "7", "8", "9",
            ".", "0", "←",
    };
    private int mFindIndex = -1;
    public static boolean canGoBack = true;
    private boolean mIsRunning;
    private int mProcess;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            mDiscoveryProgressTv.setText(msg.what + "%");
            if (msg.what == COMPLETE_PROCESS) {
                mProcess = 0;
                canGoBack = true;
                mDiscoveryProgressView.setVisibility(View.GONE);
                stopProgressBar();
                stopDiscoveryService();
                if (mFindIndex == -1) {
                    if (DevicesUtil.getTarget() != null) {
                        mDiscoveryAdapter.noClearStates(mFindIndex);
                        String mIp = DevicesUtil.getTarget().getAddress();
                        mPresenter.ipConnectAgain(mIp);
                    }
                }
            }
        }
    };

    public DeviceDiscoveryFragment() {
    }

    public static DeviceDiscoveryFragment newInstance() {
        AtomicReference<DeviceDiscoveryFragment> fragment = new AtomicReference<>(new
                DeviceDiscoveryFragment());
        return fragment.get();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_discovery_device, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        initAdapter();
        initActionBar();
        setOnClickListener();
        setMarginForStatusBar(mRlTitle, TITLE_BAR_HEIGHT_DP);
    }

    private void initActionBar() {
        Intent intent = getActivity().getIntent();
        Bundle bundle = intent.getExtras();
        String title = bundle.getString(PhiConstants.ACTION_BAR_NAME);
        mTvTitle.setText(title);
        mTvRecords.setVisibility(View.VISIBLE);
        mTvRecords.setText(getString(R.string.recent_connect_devices));
    }

    private void initAdapter() {
        mDiscoveryListDevices.setEmptyView(mEmptyTv);
        mDiscoveryListDevices.setDivider(new ColorDrawable(Color.GRAY));
        mDiscoveryListDevices.setDividerHeight(1);
        mDiscoveryAdapter = new DeviceDiscoveryAdapter();
        mDiscoveryListDevices.setAdapter(mDiscoveryAdapter);
    }

    private void setOnClickListener() {
        mDiscoveryListDevices.setOnItemClickListener(selectHandler);
    }

    @Override
    @OnClick({R.id.iv_back, R.id.tv_right, R.id.start_discovery, R.id.manual_ip})
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.start_discovery:
                if (isWifiAvailable()) {
                    if (mFindIndex != -1) {
                        mDiscoveryAdapter.noClearStates(mFindIndex);
                    }
                    startJmdnsDiscoveryDevice();
                } else {
                    Toast.makeText(getContext(), R.string.finder_wifi_not_available, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.manual_ip:
                manualConnectDevice();
                break;
            case R.id.tv_right:
                Intent intent = new Intent(getContext(), RecentDevicesActivity.class);
                startActivity(intent);
                break;
            case R.id.iv_back:
                if (canGoBack) {
                    getActivity().onBackPressed();
                }
                break;
            default:
                break;
        }
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v == mIPCancleBt) {
                mBottomView.dismissBottomView();
            } else if (v == mIPConfirmBt) {
                String inputStr = mIPInputEditText.getText().toString().trim();
                if (isValidIpAddress(inputStr)) {
                    mBottomView.dismissBottomView();
                    startIPConnectProgressBar();
                    mPresenter.ipConnect(inputStr);
                } else {
                    mBottomView.dismissBottomView();
                    illegalIPDialog().show();
                }
            }
        }
    };

    public void startIPConnectProgressBar() {
        mConnectProgressBar.setVisibility(View.VISIBLE);
        mDiscoveryBtn.setVisibility(View.GONE);
        mManualIpBtn.setVisibility(View.GONE);
        mDiscoveryBtn.setEnabled(false);
        mManualIpBtn.setEnabled(false);
        mTvRecords.setEnabled(false);
        mDiscoveryListDevices.setEnabled(false);
    }

    public void stopIPConnectProgressBar() {
        mConnectProgressBar.setVisibility(View.GONE);
        mDiscoveryBtn.setVisibility(View.VISIBLE);
        mManualIpBtn.setVisibility(View.VISIBLE);
        mDiscoveryBtn.setEnabled(true);
        mManualIpBtn.setEnabled(true);
        mTvRecords.setEnabled(true);
        mDiscoveryListDevices.setEnabled(true);
    }

    @Override
    public void showConnectFailDialog() {
        ipConnectFailDialog().show();
    }

    private AdapterView.OnItemClickListener manualConnectDevice = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            String str = mIPInputEditText.getText().toString();
            String text = (String) adapterView.getAdapter().getItem(position);
            if (text.equals("←")) {
                // 执行backspace
                if (str.length() > 0) {
                    str = str.substring(0, str.length() - 1);
                }
                mIPInputEditText.setText(str);
            } else {
                str += text;
                mIPInputEditText.setText(str);
            }
        }
    };

    private void manualConnectDevice() {
        mBottomView = new BottomView(getContext(), R.style.BottomViewTheme_Defalut, R.layout.bottom_view);
        mBottomView.setAnimation(R.style.BottomToTopAnim);
        mBottomView.showBottomView(false);
        mIPGridView = (GridView) mBottomView.getView().findViewById(
                R.id.grid_buttons);
        mIPAdapter = new ManualIPAdapter(getContext(), R.id.ip_button, mIPBtns);
        mIPGridView.setAdapter(mIPAdapter);
        mIPInputEditText = (EditText) mBottomView.getView().findViewById(
                R.id.edittext_ipinput);
        //获取焦点时，禁止弹出软键盘
        mIPInputEditText.setInputType(InputType.TYPE_NULL);
        mIPGridView.setOnItemClickListener(manualConnectDevice);
        mIPCancleBt = (Button) mBottomView.getView().findViewById(
                bt_cancelinput);
        mIPConfirmBt = (Button) mBottomView.getView().findViewById(
                bt_confirminput);
        mIPCancleBt.setOnClickListener(onClickListener);
        mIPConfirmBt.setOnClickListener(onClickListener);
    }

    private AdapterView.OnItemClickListener selectHandler = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent, View v, int position,
                                long id) {
            if (SettingUtil.isVibrateOn()) {
                SettingUtil.doVibrate();
            }
            final int pos = position;
            mFindIndex = position;
            final RemoteBoxDevice remoteDevice = (RemoteBoxDevice) parent.getItemAtPosition(
                    position);
            if (remoteDevice != null) {
                ConnectManager.getInstance().connect(remoteDevice, new ConnectManager.ConnetResultCallback() {
                    @Override
                    public void onSuccess(RemoteBoxDevice device) {
                        RemoteBoxDevice target = mPresenter.getTarget();
                        if (target == null || !(target.getBssid().equals(remoteDevice.getBssid()))) {
                            mPresenter.insertOrUpdateRecentDevices(remoteDevice);
                            mDiscoveryAdapter.clearStates(pos);
                            mDiscoveryAdapter.notifyDataSetInvalidated();
                            mTvTitle.setText(remoteDevice.getName());
                            Toast.makeText(getContext(), "connect SUCCESS", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFail(String msg) {
                        mPresenter.removeItemAndRefreshView(remoteDevice);
                        LogUtil.d(TAG, "remove connect fail device");
                        Toast.makeText(getContext(), "this device is offline", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    };

    @Override
    public void onResume() {
        LogUtil.d(TAG, "onResume() is called");
        LogUtil.d(TAG, mPresenter.getCurrentDeviceList().toString());
        mPresenter.loadRecentList();
        mWifiManager = (WifiManager) getContext().getApplicationContext().getSystemService(WIFI_SERVICE);
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        mWifiChangeReceiver = new WifiChangeReceiver();
        getActivity().registerReceiver(mWifiChangeReceiver, filter);
        //修复首页下拉列表异常
        addSelectedItem();
        super.onResume();
    }

    private void addSelectedItem() {
        List<RemoteBoxDevice> mOnResumeRemoteBoxDevice = mPresenter.getCurrentDeviceList();
        if ((DevicesUtil.getTarget() != null) && (mOnResumeRemoteBoxDevice != null)) {
            if ((!mPresenter.isContains(mOnResumeRemoteBoxDevice, DevicesUtil.getTarget()))) {
                mPresenter.addDeviceItem(mOnResumeRemoteBoxDevice, DevicesUtil.getTarget());
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        getActivity().unregisterReceiver(mWifiChangeReceiver);
        super.onStop();
    }

    @Override
    public void setPresenter(Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showToast(String str) {
        Toast.makeText(getContext(), str, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setTittle(String str) {
        mTvTitle.setText(str);
    }

    @Override
    public void setTittle(int intId) {
        mTvTitle.setText(intId);
    }

    @Override
    public void refreshListView(List<RemoteBoxDevice> currentlist) {
        if (currentlist.size() > 0) {
            mChooseTv.setVisibility(View.VISIBLE);
        } else {
            mChooseTv.setVisibility(View.INVISIBLE);
        }
        mCountTv.setText(makeDeviceCountLabel(currentlist.size()));
        mFindIndex = findTargetPos(currentlist);
        if (mFindIndex != -1) {
            mDiscoveryAdapter.clearStates(mFindIndex);
        }
        mDiscoveryAdapter.notifyDataChange(currentlist);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void startJmdnsDiscoveryDevice() {
        mPresenter.start();
        startProgressBar();
        mDiscoveryProgressView.setVisibility(View.VISIBLE);
        mDiscoveryProgressbar.start();
        mIsRunning = true;
        new Thread() {
            public void run() {
                while (mIsRunning) {
                    mProcess++;
                    Message message = new Message();
                    message.what = mProcess;
                    mHandler.sendMessage(message);
                    try {
                        if (mProcess == COMPLETE_PROCESS) {
                            mIsRunning = false;
                        }
                        Thread.sleep(SLEEP_TIME);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    private void stopDiscoveryService() {
        RemoteBoxDevice target = mPresenter.getTarget();
        if (target == null) {
            mTvTitle.setText(R.string.unable_to_connect_device);
        } else {
            mTvTitle.setText(target.getName());
        }
        mPresenter.stop();
    }

    public String makeDeviceCountLabel(int count) {
        StringBuilder deviceCount = new StringBuilder();
        StringBuilder formatBuilder = new StringBuilder();
        Formatter sFormatter = new Formatter(formatBuilder, Locale.getDefault());
        if (isAdded()) {
            String f = getResources().getQuantityText(R.plurals.NdeviceCount, count)
                    .toString();
            deviceCount.setLength(0);
            sFormatter.format(f, count);
            deviceCount.append(formatBuilder);
            return deviceCount.toString();
        } else {
            return String.valueOf(count);
        }
    }

    private AlertDialog illegalIPDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_illegal_ip, null);
        builder.setPositiveButton(
                R.string.confirm, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        manualConnectDevice();
                    }
                })
                .setCancelable(true)
                .setView(view);
        return builder.create();
    }

    private AlertDialog ipConnectFailDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_ip_connect_fail, null);
        builder.setPositiveButton(
                R.string.confirm, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setCancelable(true)
                .setView(view);
        return builder.create();
    }

    private boolean isValidIpAddress(String inputStr) {
        if (inputStr == null || inputStr.isEmpty()) {
            return false;
        }
        String ips[] = inputStr.split("\\.");
        if (ips.length == 4) {
            try {
                for (String ip : ips) {
                    if (Integer.parseInt(ip) < 0 || Integer.parseInt(ip) > 255) {
                        return false;
                    }
                }
            } catch (Exception e) {
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    private int findTargetPos(List<RemoteBoxDevice> currentList) {
        RemoteBoxDevice target = DevicesUtil.getTarget();
        if (target == null || !(currentList.size() > 0)) {
            return -1;
        }
        for (int i = 0; i < currentList.size(); i++) {
            if (target.getBssid().equals(currentList.get(i).getBssid())) {
                return i;
            }
        }
        return -1;
    }

    private void stopProgressBar() {
        mDiscoveryBtn.setVisibility(View.VISIBLE);
        mManualIpBtn.setVisibility(View.VISIBLE);
        mDiscoveryBtn.setEnabled(true);
        mManualIpBtn.setEnabled(true);
        mTvRecords.setEnabled(true);
        mDiscoveryListDevices.setEnabled(true);
    }

    private void startProgressBar() {
        mDiscoveryBtn.setVisibility(View.GONE);
        mManualIpBtn.setVisibility(View.GONE);
        mDiscoveryBtn.setEnabled(false);
        mManualIpBtn.setEnabled(false);
        mTvRecords.setEnabled(false);
        mDiscoveryListDevices.setEnabled(false);
        canGoBack = false;
    }

    private boolean isWifiAvailable() {
        if (!mWifiManager.isWifiEnabled()) {
            return false;
        }
        WifiInfo info = mWifiManager.getConnectionInfo();
        return info != null && info.getIpAddress() != 0;
    }

    private String getNetworkName() {
        if (!isWifiAvailable()) {
            return null;
        }
        WifiInfo info = mWifiManager.getConnectionInfo();
        String ssid = null;
        if (info != null && isWifiAvailable()) {
            int len = info.getSSID().length();
            if (info.getSSID().startsWith("\"")
                    && info.getSSID().endsWith("\"")) {
                ssid = info.getSSID().substring(1, len - 1);
            } else {
                ssid = info.getSSID();
            }
        }
        return ssid;
    }

    class WifiChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(action)) {
                int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, -1);
                switch (wifiState) {
                    case WifiManager.WIFI_STATE_ENABLING: {
                        mNetworkNameTv.setText(R.string.wifi_connecting);
                        break;
                    }
                    case WifiManager.WIFI_STATE_DISABLING: {
                        mNetworkNameTv.setText(R.string.wifi_disconnecting);
                        break;
                    }
                    case WifiManager.WIFI_STATE_DISABLED: {
                        mNetworkNameTv.setText(R.string.wifi_not_connected);
                        break;
                    }
                    default:
                        break;
                }
            }
            if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(action)) {
                Parcelable parcelableExtra = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                if (null != parcelableExtra) {
                    NetworkInfo networkInfo = (NetworkInfo) parcelableExtra;
                    NetworkInfo.State state = networkInfo.getState();
                    if (state == NetworkInfo.State.CONNECTED) {
                        mNetworkNameTv.setText(getNetworkName());
                    }
                }
            }
        }
    }
}



