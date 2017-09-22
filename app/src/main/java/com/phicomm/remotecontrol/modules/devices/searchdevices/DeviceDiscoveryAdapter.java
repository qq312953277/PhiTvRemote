package com.phicomm.remotecontrol.modules.devices.searchdevices;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import com.phicomm.remotecontrol.R;
import com.phicomm.remotecontrol.RemoteBoxDevice;
import com.phicomm.remotecontrol.util.LogUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by chunya02.li on 2017/7/11.
 */
public class DeviceDiscoveryAdapter extends BaseAdapter {
    private static String TAG = "DeviceDiscoveryAdapter";
    private List<RemoteBoxDevice> mDeviceList;
    private HashMap<String, Boolean> mStatus;

    public DeviceDiscoveryAdapter() {
        mDeviceList = new ArrayList<>();
        mStatus = new HashMap<String, Boolean>();
    }

    @Override
    public int getCount() {
        return mDeviceList.size();
    }

    @Override
    public Object getItem(int position) {
        return mDeviceList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public List<RemoteBoxDevice> getDeviceList() {
        return mDeviceList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DeviceViewHolder holder;
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_discovery_device, parent, false);
            holder = new DeviceViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (DeviceViewHolder) convertView.getTag();
        }
        final DeviceViewHolder finalHolder = holder;
        finalHolder.mRadioButton.setClickable(false);
        /*final int pos = position;
        holder.mRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearStates(pos);
                finalHolder.mRadioButton.setChecked(getStates(pos));
                notifyDataSetChanged();
            }
        });*/
        boolean res;
        if (getStates(position) == null || !(getStates(position))) {
            res = false;
            setStates(position, false);
        } else {
            res = true;
        }
        holder.mRadioButton.setChecked(res);
        holder.bind(mDeviceList.get(position));
        return convertView;
    }

    public void clearStates(int position) {
        for (String key : mStatus.keySet()) {
            mStatus.put(key, false);
        }
        mStatus.put(String.valueOf(position), true);
    }

    public void noClearStates(int position) {
        for (String key : mStatus.keySet()) {
            mStatus.put(key, false);
        }
        mStatus.put(String.valueOf(position), false);
    }

    private Boolean getStates(int position) {
        return mStatus.get(String.valueOf(position));
    }

    private void setStates(int position, boolean isChecked) {
        mStatus.put(String.valueOf(position), isChecked);
    }

    public void notifyDataChange(List<RemoteBoxDevice> deviceList) {
        mDeviceList.clear();
        mDeviceList.addAll(deviceList);
        LogUtil.d(TAG, "notifyDataChange");
        notifyDataSetChanged();
    }

    public class DeviceViewHolder {
        TextView mBoxBssid;
        TextView mBoxName;
        TextView mBoxLocalIp;
        RadioButton mRadioButton;
        View mItemView;

        DeviceViewHolder(View itemView) {
            mItemView = itemView;
            mBoxBssid = (TextView) mItemView.findViewById(R.id.box_device_bssid);
            mBoxName = (TextView) mItemView.findViewById(R.id.box_device_name);
            mBoxLocalIp = (TextView) mItemView.findViewById(R.id.box_device_localip);
            mRadioButton = (RadioButton) mItemView.findViewById(R.id.box_connect_device);
        }

        private void bind(RemoteBoxDevice device) {
            if (device != null) {
                mBoxBssid.setText(device.getBssid());
                mBoxName.setText(device.getName());
                mBoxLocalIp.setText(device.getAddress().toString());
            }
        }
    }
}
