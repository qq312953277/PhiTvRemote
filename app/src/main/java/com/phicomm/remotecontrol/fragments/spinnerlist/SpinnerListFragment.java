package com.phicomm.remotecontrol.fragments.spinnerlist;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.phicomm.remotecontrol.ConnectManager;
import com.phicomm.remotecontrol.discovery.DeviceDiscoveryActivity;
import com.phicomm.remotecontrol.constant.PhiConstants;
import com.phicomm.remotecontrol.R;
import com.phicomm.remotecontrol.RemoteBoxDevice;
import com.phicomm.remotecontrol.util.DevicesUtil;
import com.phicomm.remotecontrol.greendao.GreenDaoUserUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by chunya02.li on 2017/7/13.
 */

public class SpinnerListFragment extends Fragment {
    private static String TAG = "SpinnerListFragment";
    private SpinnerWindowView mSpinerPopWindow;
    private GreenDaoUserUtil mGreenDaoUserUtil;
    private List<RemoteBoxDevice> mCurrentDevicesList = new ArrayList<>(0);

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
        setonClickListener();
    }

    @Override
    public void onResume() {
        DevicesUtil.loadRecentList();
        List<RemoteBoxDevice> deviceList = DevicesUtil.getCurrentDevicesListResult();
        RemoteBoxDevice target = DevicesUtil.getTarget();
        if (target != null) {
            mDeviceTv.setText(target.getName());
        } else {
            mDeviceTv.setText(getString(R.string.unable_to_connect_device));
        }
        refreshSpinnerListView(deviceList);
        super.onResume();
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

    private void setonClickListener() {
        mDeviceTv.setOnClickListener(onButtonClick);
        mDiscoveryBtn.setOnClickListener(onButtonClick);
        mLoginIBtn.setOnClickListener(onButtonClick);
    }

    private void initAdapter() {
        mSpinerPopWindow = new SpinnerWindowView(getContext(), itemClickListener);
        mSpinerPopWindow.setOnDismissListener(dismissListener);
        mGreenDaoUserUtil = new GreenDaoUserUtil();
        new Thread(mLoadConnectedTask).start();
        DevicesUtil.setGreenDaoUserUtil(mGreenDaoUserUtil);
    }


    Runnable mLoadConnectedTask = new Runnable() {
        @Override
        public void run() {
            Message msg = new Message();
            Bundle data = new Bundle();
            data.putParcelableArrayList(PhiConstants.SPINNER_DEVICES_LIST, (ArrayList<? extends
                    Parcelable>) loadLastestConnectionDevice());
            msg.setData(data);
            mLoadTargetDevice.sendMessage(msg);
        }
    };

    private List<RemoteBoxDevice> loadLastestConnectionDevice() {
        List<RemoteBoxDevice> recenteddevices = mGreenDaoUserUtil.querydata();
        Log.d(TAG, "loadLastestConnectionDevice mCurrentDevicesList=" + mCurrentDevicesList);
        mCurrentDevicesList.clear();
        if (recenteddevices.size() > 0) {
            RemoteBoxDevice targetDevice = recenteddevices.get(0);
            DevicesUtil.setTarget(targetDevice);
            mCurrentDevicesList.add(recenteddevices.get(0));
        } else {
            DevicesUtil.setTarget(null);
        }
        DevicesUtil.setCurrentListResult(mCurrentDevicesList);
        return mCurrentDevicesList;
    }

    View.OnClickListener onButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v == mDiscoveryBtn) {
                Intent intent = new Intent(getContext(), DeviceDiscoveryActivity.class);
                intent.putExtra(PhiConstants.ACTION_BAR_NAME,mDeviceTv.getText());
                startActivity(intent);
            } else if (v == mDeviceTv) {
                mSpinerPopWindow.setWidth(mDeviceTv.getWidth());
                mSpinerPopWindow.showAsDropDown(mDeviceTv);
                setTextImage(R.drawable.icon_up);
            }
        }
    };

    private AdapterView.OnItemClickListener itemClickListener = new AdapterView
            .OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            RemoteBoxDevice remoteDevice = (RemoteBoxDevice) parent.getAdapter().getItem(position);
            Log.d(TAG, "onItemSelected remoteDevice=" + remoteDevice);
            if (remoteDevice != null) {
                if (ConnectManager.getInstance().connect(remoteDevice)) {
                    RemoteBoxDevice target = DevicesUtil.getTarget();
                    if (target != null && !(target.getBssid().equals(remoteDevice.getBssid()))) {
                        DevicesUtil.insertOrUpdateRecentDevices(remoteDevice);
                    }
                    mDeviceTv.setText(remoteDevice.getName());
                } else {
                    mCurrentDevicesList.remove(remoteDevice);
                    DevicesUtil.setCurrentListResult(mCurrentDevicesList);
                    mSpinerPopWindow.notifyDataChange(mCurrentDevicesList);
                    mDeviceTv.setText(getString(R.string.unable_to_connect_device));
                }
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
            mSpinerPopWindow.notifyDataChange(deviceList);
            if (deviceList != null && deviceList.size() > 0) {
                mDeviceTv.setText(deviceList.get(0).getName());
            }
        }
    };

}
