package com.phicomm.remotecontrol.modules.main.spinnerlist;

import android.content.Context;
import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.phicomm.remotecontrol.ConnectManager;
import com.phicomm.remotecontrol.R;
import com.phicomm.remotecontrol.RemoteBoxDevice;
import com.phicomm.remotecontrol.base.BaseFragment;
import com.phicomm.remotecontrol.constant.PhiConstants;
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

    @BindView(R.id.connected_device)
    TextView mDeviceTv;

    @BindView(R.id.ll_spinlist)
    LinearLayout mLlSpinlist;

    @BindView(R.id.iv_up_down)
    ImageView mUpDown;

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
        initAdapter();
    }

    private void initAdapter() {
        mSpinerPopWindow = new SpinnerWindowView(getContext(), itemClickListener);
        mSpinerPopWindow.setOnDismissListener(dismissListener);

        mGreenDaoUserUtil = new GreenDaoUserUtil();
        new Thread(mLoadConnectedTask).start();
        DevicesUtil.setGreenDaoUserUtil(mGreenDaoUserUtil);
    }

    @Override
    public void onResume() {
        DevicesUtil.loadRecentList();

        //获取连接记录并显示
        List<RemoteBoxDevice> remoteBoxDeviceList = mGreenDaoUserUtil.querydata();
        RemoteBoxDevice target = DevicesUtil.getTarget();

        if (target != null) {
            mUpDown.setVisibility(View.VISIBLE);
            mDeviceTv.setText(target.getName());
        } else {
            mUpDown.setVisibility(View.GONE);
            mDeviceTv.setText(getString(R.string.unable_to_connect_device));
        }
        refreshSpinnerListView(remoteBoxDeviceList);
        super.onResume();
    }

    private void refreshSpinnerListView(List<RemoteBoxDevice> deviceList) {
        if (deviceList != null && deviceList.size() > 0) {
            mUpDown.setVisibility(View.VISIBLE);
            mSpinerPopWindow.notifyDataChange(deviceList);
            mUpDown.setImageResource(R.drawable.icon_down);
            mDeviceTv.setEnabled(true);
        } else {
            mUpDown.setVisibility(View.GONE);
            mDeviceTv.setEnabled(false);
        }
    }

    Runnable mLoadConnectedTask = new Runnable() {
        @Override
        public void run() {
            loadLastestConnectionDevice();
        }
    };

    private void loadLastestConnectionDevice() {
        List<RemoteBoxDevice> remoteBoxDeviceList = mGreenDaoUserUtil.querydata();
        Log.d(TAG, "loadLastestConnectionDevice mCurrentDevicesList=" + mCurrentDevicesList);
        mCurrentDevicesList.clear();
        int i = 0;
        while (remoteBoxDeviceList.size() > i && !mIsSuccess) {
            RemoteBoxDevice device = remoteBoxDeviceList.get(i);
            i++;
            if (device != null) {
                ConnectManager.getInstance().connect(device, new ConnectManager
                        .ConnetResultCallback() {
                    @Override
                    public void onSuccess(RemoteBoxDevice device) {
                        Log.d(TAG, "onSuccess device=" + device);
                        DevicesUtil.setTarget(device);
                        if (mCurrentDevicesList.size() == 0) {
                            mCurrentDevicesList.add(device);
                        }
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
        Message msg = Message.obtain();
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
                        List<RemoteBoxDevice> remoteBoxDeviceList = mGreenDaoUserUtil.querydata();
                        refreshSpinnerListView(remoteBoxDeviceList);
                    }

                    @Override
                    public void onFail(String msg) {
                        LogUtil.d("Connect fail:" + remoteDevice.toString());
                        mCurrentDevicesList.remove(remoteDevice);
                        DevicesUtil.setCurrentListResult(mCurrentDevicesList);
                        CommonUtils.showToastBottom(getString(R.string.connect_fail));
                    }
                });
            }
            mSpinerPopWindow.dismiss();
        }
    };

    private PopupWindow.OnDismissListener dismissListener = new PopupWindow.OnDismissListener() {
        @Override
        public void onDismiss() {
            setBackgroundTransparent(1.0f);
            mUpDown.setImageResource(R.drawable.icon_down);
        }
    };

    Handler mLoadTargetDevice = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            List<RemoteBoxDevice> deviceList = bundle.getParcelableArrayList(PhiConstants
                    .SPINNER_DEVICES_LIST);
            Log.d(TAG, " handleMessage deviceList.size()=" + deviceList.size());
            //手机后台自动connect连接记录的device，然后更新下拉展开栏
            List<RemoteBoxDevice> remoteBoxDeviceList = mGreenDaoUserUtil.querydata();
            refreshSpinnerListView(remoteBoxDeviceList);
            if (deviceList.size() > 0) {
                mDeviceTv.setText(deviceList.get(0).getName());
            } else {
                mDeviceTv.setText(R.string.unable_to_connect_device);
                mUpDown.setVisibility(View.GONE);
            }
        }
    };

    @Override
    @OnClick({R.id.login, R.id.scan, R.id.rl_connected_device})
    public void onClick(View view) { //继承BaseFragment震动事件
        super.onClick(view);

        switch (view.getId()) {
            case R.id.login:
                CommonUtils.startIntent(getActivity(), null, PersonalActivity.class);
                break;
            case R.id.scan:
                Intent intent = new Intent(getContext(), DeviceDiscoveryActivity.class);
                intent.putExtra(PhiConstants.ACTION_BAR_NAME, mDeviceTv.getText());
                startActivity(intent);
                break;
            case R.id.rl_connected_device:
                WindowManager wm = (WindowManager) getContext()
                        .getSystemService(Context.WINDOW_SERVICE);

                if (DevicesUtil.getTarget() != null) {
                    setBackgroundTransparent(0.4f);
                    mUpDown.setImageResource(R.drawable.icon_up);
                    mSpinerPopWindow.setWidth(wm.getDefaultDisplay().getWidth());
                    mSpinerPopWindow.showAsDropDown(mLlSpinlist);
                }
                break;
        }
    }

    private void setBackgroundTransparent(float value) {
        WindowManager.LayoutParams lp = getActivity().getWindow()
                .getAttributes();
        lp.alpha = value;
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getActivity().getWindow().setAttributes(lp);

    }

    @Override
    public void onDestroy() {
        DevicesUtil.setTarget(null);
        super.onDestroy();
    }
}
