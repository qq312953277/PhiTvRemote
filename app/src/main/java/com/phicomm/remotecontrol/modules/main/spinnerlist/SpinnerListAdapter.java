package com.phicomm.remotecontrol.modules.main.spinnerlist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.phicomm.remotecontrol.R;
import com.phicomm.remotecontrol.RemoteBoxDevice;
import com.phicomm.remotecontrol.util.DevicesUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chunya02.li on 2017/7/12.
 */

public class SpinnerListAdapter extends BaseAdapter {
    private List<RemoteBoxDevice> mSpinnerDeviceList;

    public SpinnerListAdapter() {
        mSpinnerDeviceList = new ArrayList<>();
    }

    @Override
    public int getCount() {
        if (mSpinnerDeviceList != null) {
            return mSpinnerDeviceList.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        return mSpinnerDeviceList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_spinner_device, parent, false);
            holder = new ViewHolder();
            holder.mTextView = (TextView) convertView.findViewById(R.id.tv_device_name);
            holder.mImageViewTV = (ImageView) convertView.findViewById(R.id.imageViewTV);
            holder.mImageViewSelected = (ImageView) convertView.findViewById(R.id.imageViewSelectede);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();

        }
        String mDeviceName = mSpinnerDeviceList.get(position).getName();
        holder.mTextView.setText(mDeviceName);

        if (position == 0 && DevicesUtil.getTarget() != null) {
            holder.mImageViewSelected.setVisibility(View.VISIBLE);
        } else {
            holder.mImageViewSelected.setVisibility(View.INVISIBLE);
        }
        return convertView;
    }

    public void notifyDataChange(List<RemoteBoxDevice> deviceList) {
        mSpinnerDeviceList = deviceList;
        notifyDataSetChanged();
    }

    public static class ViewHolder {
        public TextView mTextView;
        public ImageView mImageViewTV;
        public ImageView mImageViewSelected;
    }
}