package com.phicomm.remotecontrol.modules.main.screenprojection.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.phicomm.remotecontrol.R;
import com.phicomm.remotecontrol.base.BaseApplication;
import com.phicomm.remotecontrol.modules.main.screenprojection.entity.PhotoUpImageBucket;

import java.util.ArrayList;
import java.util.List;

public class AlbumsAdapter extends BaseAdapter {

    private List<PhotoUpImageBucket> mList;
    private LayoutInflater mLayoutInflater;

    public AlbumsAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
        mList = new ArrayList<>(); //初始化集合

    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        View view;
        if (convertView == null) {
            holder = new Holder();
            view = mLayoutInflater.inflate(R.layout.ablums_adapter_item, parent, false);
            holder.image1 = (ImageView) view.findViewById(R.id.image1);
            holder.image2 = (ImageView) view.findViewById(R.id.image2);
            holder.image3 = (ImageView) view.findViewById(R.id.image3);
            holder.name = (TextView) view.findViewById(R.id.name);
            holder.count = (TextView) view.findViewById(R.id.count);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (Holder) convertView.getTag();
        }
        holder.count.setText("" + mList.get(position).getCount());
        holder.name.setText(mList.get(position).getBucketName());

        Glide.with(BaseApplication.getContext()).load("file://" + mList.get(position).getImageList().get(0).getmImagePath())
                .error(R.drawable.album_default_loading_pic).centerCrop().placeholder(R.drawable.album_default_loading_pic).priority(Priority.HIGH).into(holder.image1);

        if (mList.get(position).getImageList().size() > 1) {
            Glide.with(BaseApplication.getContext()).load("file://" + mList.get(position).getImageList().get(1).getmImagePath())
                    .error(R.drawable.album_default_loading_pic).centerCrop().placeholder(R.drawable.album_default_loading_pic).priority(Priority.HIGH).into(holder.image2);
        }

        if (mList.get(position).getImageList().size() > 2) {
            Glide.with(BaseApplication.getContext()).load("file://" + mList.get(position).getImageList().get(2).getmImagePath())
                    .error(R.drawable.album_default_loading_pic).centerCrop().placeholder(R.drawable.album_default_loading_pic).priority(Priority.HIGH).into(holder.image3);
        }
        return view;
    }

    class Holder {
        ImageView image1;
        ImageView image2;
        ImageView image3;
        TextView name;
        TextView count;
    }

    public void setArrayList(List<PhotoUpImageBucket> arrayList) {
        this.mList = arrayList;
    }
}
