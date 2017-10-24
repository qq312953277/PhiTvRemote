package com.phicomm.remotecontrol.modules.main.screenprojection.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.phicomm.remotecontrol.R;
import com.phicomm.remotecontrol.base.BaseApplication;
import com.phicomm.remotecontrol.modules.main.screenprojection.entity.MItem;

import java.util.List;

/**
 * Created by kang.sun on 2017/10/20.
 */

public class AlbumItemAdapter extends BaseAdapter {
    private List<MItem> mMItemlist;
    private LayoutInflater layoutInflater;

    public AlbumItemAdapter(List<MItem> list, Context context) {
        this.mMItemlist = list;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mMItemlist.size();
    }

    @Override
    public Object getItem(int position) {
        return mMItemlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.album_item_images_item_view, parent, false);
            holder = new Holder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.image_item);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        Glide.with(BaseApplication.getContext()).load("file://" + mMItemlist.get(position).getFilePath()).dontAnimate()
                .error(R.drawable.album_default_loading_pic).centerCrop().placeholder(R.drawable.album_default_loading_pic)
                .priority(Priority.HIGH).into(holder.imageView);
        return convertView;
    }

    class Holder {
        ImageView imageView;
    }
}
