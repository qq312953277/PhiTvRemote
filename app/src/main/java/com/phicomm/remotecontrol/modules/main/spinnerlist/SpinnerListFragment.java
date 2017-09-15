package com.phicomm.remotecontrol.modules.main.spinnerlist;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.phicomm.remotecontrol.ConnectManager;
import com.phicomm.remotecontrol.R;
import com.phicomm.remotecontrol.RemoteBoxDevice;
import com.phicomm.remotecontrol.base.BaseFragment;
import com.phicomm.remotecontrol.constant.PhiConstants;
import com.phicomm.remotecontrol.greendao.Entity.RemoteDevice;
import com.phicomm.remotecontrol.greendao.GreenDaoUserUtil;
import com.phicomm.remotecontrol.modules.devices.searchdevices.DeviceDiscoveryActivity;
import com.phicomm.remotecontrol.modules.personal.personaldetail.PersonalActivity;
import com.phicomm.remotecontrol.util.CommonUtils;
import com.phicomm.remotecontrol.util.DevicesUtil;
import com.phicomm.remotecontrol.util.LogUtil;
import com.phicomm.remotecontrol.util.SettingUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by chunya02.li on 2017/7/13.
 */
public class SpinnerListFragment extends BaseFragment {
    private static String TAG = "SpinnerListFragment";
    private SpinnerWindowView mSpinerPopWindow;
    private GreenDaoUserUtil mGreenDaoUserUtil;
    private List<RemoteBoxDevice> mCurrentDevicesList = new ArrayList<>(0);
    private boolean mIsSuccess = false;
    private DisconnectSpinnerWindowView mDisconnectSpinnerWindowView;
    @BindView(R.id.connected_device)
    public TextView mDeviceTv;

    @BindView(R.id.scan)
    public Button mDiscoveryBtn;

    @BindView(R.id.login)
    public ImageButton mLoginIBtn;

    public SpinnerListFragment() {

    }

    public static SpinnerListFragment newInstance() {
        SpinnerListFragment fragment = new SpinnerListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_spinner_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        initAdapter();
    }

    @Override
    public void onResume() {
        DevicesUtil.loadRecentList();
        //获取连接记录并显示
        List<RemoteDevice> deviceList = mGreenDaoUserUtil.loadAllRecentByTimeOrder();
        List<RemoteBoxDevice> remoteBoxDeviceList = parseToRemoteBoxDevice(deviceList);
        RemoteBoxDevice target = DevicesUtil.getTarget();
        if (target != null) {
            mDeviceTv.setText(target.getName());
        } else {
            mDeviceTv.setText(getString(R.string.unable_to_connect_device));
        }
        refreshSpinnerListView(remoteBoxDeviceList);
        super.onResume();
    }

    private List<RemoteBoxDevice> parseToRemoteBoxDevice(List<RemoteDevice> deviceList) {
        List<RemoteBoxDevice> remoteBoxDeviceList = new ArrayList<>();
        for (int i = 0; i < deviceList.size(); i++) {
            RemoteDevice mRemoteDevice = deviceList.get(i);
            remoteBoxDeviceList.add(new RemoteBoxDevice(mRemoteDevice.getName(), mRemoteDevice.getAddress(),
                    mRemoteDevice.getPort(), mRemoteDevice.getBssid()));
        }
        return remoteBoxDeviceList;
    }


    @Override
    public void onDestroy() {
        DevicesUtil.setTarget(null);
        super.onDestroy();
    }

    private void refreshSpinnerListView(List<RemoteBoxDevice> deviceList) {
        if (deviceList != null) {
            mSpinerPopWindow.notifyDataChange(deviceList);
        }
    }

    private void initAdapter() {
        mSpinerPopWindow = new SpinnerWindowView(getContext(), itemClickListener);
        mSpinerPopWindow.setOnDismissListener(dismissListener);
        mDisconnectSpinnerWindowView = new DisconnectSpinnerWindowView(getContext());
        mDisconnectSpinnerWindowView.setOnDismissListener(dismissListener);
        mGreenDaoUserUtil = new GreenDaoUserUtil();
        new Thread(mLoadConnectedTask).start();
        DevicesUtil.setGreenDaoUserUtil(mGreenDaoUserUtil);
    }

    Runnable mLoadConnectedTask = new Runnable() {
        @Override
        public void run() {
            loadLastestConnectionDevice();
        }
    };

    private void loadLastestConnectionDevice() {
        List<RemoteDevice> deviceList = mGreenDaoUserUtil.loadAllRecentByTimeOrder();
        List<RemoteBoxDevice> recentDevices = parseToRemoteBoxDevice(deviceList);
        Log.d(TAG, "loadLastestConnectionDevice mCurrentDevicesList=" + mCurrentDevicesList);
        mCurrentDevicesList.clear();
        int i = 0;
        while (recentDevices.size() > i && !mIsSuccess) {
            RemoteBoxDevice device = recentDevices.get(i);
            i++;
            if (device != null) {
                ConnectManager.getInstance().connect(device, new ConnectManager
                        .ConnetResultCallback() {
                    @Override
                    public void onSuccess(RemoteBoxDevice device) {
                        Log.d(TAG, "onSuccess device=" + device);
                        DevicesUtil.setTarget(device);
                        mCurrentDevicesList.add(device);
                        mIsSuccess = true;
                        sendMessage();
                        DevicesUtil.setTarget(device);
                        DevicesUtil.setCurrentListResult(mCurrentDevicesList);
                        //更新连接记录中
                        DevicesUtil.insertOrUpdateRecentDevices(device);
                    }

                    @Override
                    public void onFail(String msg) {
                        Log.d(TAG, "onFail msg=" + msg);
                        mIsSuccess = false;
                        DevicesUtil.setTarget(null);
                    }
                });
            }
        }
    }

    private void sendMessage() {
        Message msg = new Message();
        Bundle data = new Bundle();
        data.putParcelableArrayList(PhiConstants.SPINNER_DEVICES_LIST, (ArrayList<? extends
                Parcelable>) mCurrentDevicesList);
        msg.setData(data);
        mLoadTargetDevice.sendMessage(msg);
    }

    private AdapterView.OnItemClickListener itemClickListener = new AdapterView
            .OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            SettingUtil.isVibrate();

            final RemoteBoxDevice remoteDevice = (RemoteBoxDevice) parent.getAdapter().getItem
                    (position);
            Log.d(TAG, "onItemSelected remoteDevice=" + remoteDevice);
            if (remoteDevice != null) {
                ConnectManager.getInstance().connect(remoteDevice, new ConnectManager
                        .ConnetResultCallback() {

                    @Override
                    public void onSuccess(RemoteBoxDevice device) {
                        RemoteBoxDevice target = DevicesUtil.getTarget();
                        if (target != null && !(target.getBssid().equals(device.getBssid()))) {
                            DevicesUtil.insertOrUpdateRecentDevices(device);
                        }
                        mDeviceTv.setText(remoteDevice.getName());
                        //刷新下拉列表
                        List<RemoteDevice> deviceList = mGreenDaoUserUtil.loadAllRecentByTimeOrder();
                        List<RemoteBoxDevice> remoteBoxDeviceList = parseToRemoteBoxDevice(deviceList);
                        refreshSpinnerListView(remoteBoxDeviceList);
                        Toast.makeText(getContext(), "connect SUCCESS", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFail(String msg) {
                        LogUtil.d("Connect fail:" + remoteDevice.toString());
                        mCurrentDevicesList.remove(remoteDevice);
                        DevicesUtil.setCurrentListResult(mCurrentDevicesList);
                        Toast.makeText(getContext(), "connect fail", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            mSpinerPopWindow.dismiss();
        }
    };

    private PopupWindow.OnDismissListener dismissListener = new PopupWindow.OnDismissListener() {
        @Override
        public void onDismiss() {
            setTextImage(R.drawable.icon_down);
        }
    };

    private void setTextImage(int resId) {
        Drawable drawable = getResources().getDrawable(resId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        mDeviceTv.setCompoundDrawables(null, null, drawable, null);
    }

    Handler mLoadTargetDevice = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            List<RemoteBoxDevice> deviceList = bundle.getParcelableArrayList(PhiConstants
                    .SPINNER_DEVICES_LIST);
            Log.d(TAG, " handleMessage deviceList.size()=" + deviceList.size());
            //手机后台自动connect连接记录的device，然后更新下拉展开栏
            List<RemoteDevice> recentdDeviceList = mGreenDaoUserUtil.loadAllRecentByTimeOrder();
            List<RemoteBoxDevice> remoteBoxDeviceList = parseToRemoteBoxDevice(recentdDeviceList);
            mSpinerPopWindow.notifyDataChange(remoteBoxDeviceList);
            if (deviceList.size() > 0) {
                mDeviceTv.setText(deviceList.get(0).getName());
            } else {
                mDeviceTv.setText(R.string.unable_to_connect_device);
            }
        }
    };

    @Override
    @OnClick({R.id.login, R.id.scan, R.id.connected_device})
    public void onClick(View view) { //继承BaseFragment震动事件
        super.onClick(view);

        switch (view.getId()) {
            case R.id.login:
                CommonUtils.startIntent(getActivity(), PersonalActivity.class);
                break;
            case R.id.scan:
                Intent intent = new Intent(getContext(), DeviceDiscoveryActivity.class);
                intent.putExtra(PhiConstants.ACTION_BAR_NAME, mDeviceTv.getText());
                startActivity(intent);
                break;
            case R.id.connected_device:
                WindowManager wm = (WindowManager) getContext()
                        .getSystemService(Context.WINDOW_SERVICE);
                if (DevicesUtil.getTarget() != null) {
                    mSpinerPopWindow.setWidth(wm.getDefaultDisplay().getWidth());
                    mSpinerPopWindow.showAsDropDown(mDeviceTv);
                } else {
                    mDisconnectSpinnerWindowView.setWidth(wm.getDefaultDisplay().getWidth());
                    mDisconnectSpinnerWindowView.showAsDropDown(mDeviceTv);
                }
                setTextImage(R.drawable.icon_up);
                break;
        }
    }
}
