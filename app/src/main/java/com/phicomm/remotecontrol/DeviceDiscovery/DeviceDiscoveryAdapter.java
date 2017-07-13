package com.phicomm.remotecontrol.DeviceDiscovery;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.phicomm.remotecontrol.R;
import com.phicomm.remotecontrol.RemoteBoxDevice;


/**
 * Created by chunya02.li on 2017/7/11.
 */

public class DeviceDiscoveryAdapter extends BaseAdapter {
    private static String TAG = "DeviceDiscoveryAdapter";
    private List<RemoteBoxDevice> mDeviceList;
    private HashMap<Integer, Boolean> mIsChecked;

    public DeviceDiscoveryAdapter() {
        mDeviceList = new ArrayList<>();
        mIsChecked = new HashMap<Integer, Boolean>();
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
        DeviceDiscoveryAdapter.DeviceViewHolder holder = null;
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_discovery_device, parent, false);
            holder = new DeviceViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (DeviceViewHolder) convertView.getTag();
        }
        holder.bind(mDeviceList.get(position));
        return convertView;
    }

    public void notifyDataChange(List<RemoteBoxDevice> deviceList) {
        mDeviceList.clear();
        mDeviceList.addAll(deviceList);
        notifyDataSetChanged();
    }

    public class DeviceViewHolder {
        TextView mBoxBssid;
        TextView mBoxName;
        TextView mBoxLocalIp;
        CheckBox mBoxCheckBox;
        View mItemView;

        DeviceViewHolder(View itemView) {
            mItemView = itemView;
            mBoxBssid = (TextView) mItemView.findViewById(R.id.box_device_bssid);
            mBoxName = (TextView) mItemView.findViewById(R.id.box_device_name);
            mBoxLocalIp = (TextView) mItemView.findViewById(R.id.box_device_localip);
            mBoxCheckBox = (CheckBox) mItemView.findViewById(R.id.box_connect_device);
        }

        public void bind(RemoteBoxDevice device) {
            if (device != null) {
                mBoxBssid.setText(device.getBssid());
                mBoxName.setText(device.getName());
                mBoxLocalIp.setText(device.getAddress().toString());
            }
        }
    }
}
