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
import com.phicomm.remotecontrol.modules.main.screenprojection.entity.MItem;
import com.phicomm.remotecontrol.modules.main.screenprojection.model.MediaContentBiz;

import java.util.List;

/**
 * Created by kang.sun on 2017/10/21.
 */

public class VideoAdapter extends BaseAdapter {
    public static final int TITTLE_LIMIT_LENGTH = 25;
    public static final int TITTLE_FRONT_LENGTH = 20;
    public static final int TITTLE_BACK_LENGTH = 4;

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<MItem> mVideoDataList;

    public VideoAdapter(Context context, List<MItem> videoDataList) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        mVideoDataList = videoDataList;
    }

    @Override
    public int getCount() {
        return mVideoDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return mVideoDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String mTittle = mVideoDataList.get(position).getTitle();
        MItem mMItem = mVideoDataList.get(position);
        String mDuration = mMItem.getFirstResource().getDuration();
        String movieValues = MediaContentBiz.mVideoMapList.get(mMItem.getId());
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.video_list_adapter, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.mFileName.setText(mTittle);
        Glide.with(mContext).load("file://" + movieValues).dontAnimate()
                .error(R.drawable.album_default_loading_pic).centerCrop()
                .placeholder(R.drawable.album_default_loading_pic)
                .priority(Priority.HIGH).into(viewHolder.mFileImage);
        viewHolder.mFileLength.setText(mDuration);
        return convertView;
    }

    public class ViewHolder {
        ImageView mFileImage;
        TextView mFileName;
        TextView mFileLength;

        public ViewHolder(View view) {
            mFileImage = (ImageView) view.findViewById(R.id.file_thumbnail);
            mFileName = (TextView) view.findViewById(R.id.file_name);
            mFileLength = (TextView) view.findViewById(R.id.file_length);
        }
    }
}
