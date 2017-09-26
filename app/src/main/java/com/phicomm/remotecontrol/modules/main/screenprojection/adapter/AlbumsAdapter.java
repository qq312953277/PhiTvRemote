package com.phicomm.remotecontrol.modules.main.screenprojection.adapter;


import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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

        int imageCount = mList.get(position).getCount();

        if (imageCount == 1) {
            RelativeLayout rl = (RelativeLayout) view.findViewById(R.id.rl);
            rl.setVisibility(View.GONE);

            ViewGroup.LayoutParams params = holder.image1.getLayoutParams();
            params.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 160, BaseApplication.getContext().getResources().getDisplayMetrics());
            params.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 140, BaseApplication.getContext().getResources().getDisplayMetrics());
            holder.image1.setLayoutParams(params);
            glidePhoto(position, 0, holder.image1);
        } else if (imageCount == 2) {
            holder.image3.setVisibility(View.GONE);
            glidePhoto(position, 0, holder.image1);
            glidePhoto(position, 1, holder.image2);
        } else if (imageCount > 2) {
            glidePhoto(position, 0, holder.image1);
            glidePhoto(position, 1, holder.image2);
            glidePhoto(position, 2, holder.image3);
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

    private void glidePhoto(int position, int i, ImageView imageView) {
        Glide.with(BaseApplication.getContext()).load("file://" + mList.get(position).getImageList().get(i).getmImagePath())
                .error(R.drawable.album_default_loading_pic).centerCrop().placeholder(R.drawable.album_default_loading_pic).priority(Priority.HIGH).into(imageView);
    }
}
