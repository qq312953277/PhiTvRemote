package com.phicomm.remotecontrol.modules.devices.connectrecords;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.phicomm.remotecontrol.R;
import com.phicomm.remotecontrol.greendao.Entity.RemoteDevice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by chunya02.li on 2017/7/11.
 */

public class RecentDeviceAdapter extends BaseAdapter {

    private List<RemoteDevice> mRecentDeviceList;
    private HashMap<Integer, Integer> mCheckBosVisibleMap;
    private HashMap<Integer, Boolean> mCheckedMap;

    public RecentDeviceAdapter() {
        mRecentDeviceList = new ArrayList<>();
        mCheckBosVisibleMap = new HashMap<Integer, Integer>();
        mCheckedMap = new HashMap<Integer, Boolean>();
    }

    public HashMap<Integer, Integer> getCheckBoxVisible() {
        return mCheckBosVisibleMap;
    }

    public HashMap<Integer, Boolean> getChecked() {
        return mCheckedMap;
    }

    @Override
    public int getCount() {
        return mRecentDeviceList.size();
    }

    @Override
    public Object getItem(int position) {
        return mRecentDeviceList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public List<RemoteDevice> getRecentDeviceList() {
        return mRecentDeviceList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DeviceViewHolder holder;
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_recent_device, parent, false);
            holder = new RecentDeviceAdapter.DeviceViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (RecentDeviceAdapter.DeviceViewHolder) convertView.getTag();
        }
        final int pos = position; //pos must be final
        holder.mCollectCheckBox.setOnCheckedChangeListener(new CompoundButton
                .OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean ischeck) {
                mCheckedMap.put(pos, ischeck);
            }
        });
        holder.mCollectCheckBox.setChecked(mCheckedMap.get(position));
        holder.bind(mRecentDeviceList.get(position), position);
        return convertView;
    }

    public void notifyDataChange(List<RemoteDevice> deviceList) {
        mRecentDeviceList.clear();
        mRecentDeviceList.addAll(deviceList);
        for (int i = 0; i < mRecentDeviceList.size(); i++) {
            mCheckBosVisibleMap.put(i, View.INVISIBLE);
            mCheckedMap.put(i, false);
        }
        notifyDataSetChanged();
    }

    public void removeItems(List<RemoteDevice> list_delete) {
        mRecentDeviceList.removeAll(list_delete);
        notifyDataSetChanged();
    }

    public class DeviceViewHolder {
        TextView mBoxName;
        ImageView mDeviceIcon;
        CheckBox mCollectCheckBox;
        View mItemView;

        DeviceViewHolder(View itemView) {
            mItemView = itemView;
            mBoxName = (TextView) mItemView.findViewById(R.id.recent_device_name);
            mCollectCheckBox = (CheckBox) mItemView.findViewById(R.id.recent_collect_check);
            mDeviceIcon = (ImageView) mItemView.findViewById(R.id.recent_device_icon);
        }

        private void bind(RemoteDevice device, int position) {
            if (device != null) {
                mBoxName.setText(device.getName());
                mCollectCheckBox.setChecked(mCheckedMap.get(position));
                mCollectCheckBox.setVisibility(mCheckBosVisibleMap.get(position));
            }
        }

        public CheckBox getCollectCheckBox() {
            return mCollectCheckBox;
        }
    }
}