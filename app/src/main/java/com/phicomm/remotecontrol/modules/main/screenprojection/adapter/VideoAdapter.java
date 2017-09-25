package com.phicomm.remotecontrol.modules.main.screenprojection.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.phicomm.remotecontrol.R;
import com.phicomm.remotecontrol.base.BaseApplication;
import com.phicomm.remotecontrol.modules.main.screenprojection.utils.FiletypeUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by yong04.zhou on 2017/9/18.
 */

public class VideoAdapter extends BaseAdapter {

    public static final int PIC_WIDTH = 252;
    public static final int PIC_HEIGHT = 252;
    public static final int FIllET_DEGREE = 20;
    private ArrayList<File> mVidList = new ArrayList<>();
    private HashMap<String, Long> mVidMap = new HashMap<>();
    private VideoThumbLoader mVideoThumbLoader;

    public VideoAdapter(ArrayList<File> list, HashMap<String, Long> map) {
        mVidList = list;
        mVidMap = map;
        mVideoThumbLoader = new VideoThumbLoader();
    }

    @Override
    public int getCount() {
        return mVidList.size();
    }

    @Override
    public Object getItem(int position) {
        return mVidList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String mTitle = FiletypeUtil.RemoveType(mVidList.get(position));
        long mDuration = mVidMap.get(mVidList.get(position).toString());
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(BaseApplication.getContext()).inflate(R.layout.video_list_adapter, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.mFileName.setText(mTitle);
        viewHolder.mFileImage.setImageResource(R.drawable.video_default_loading_pic);
        viewHolder.mFileImage.setTag(mVidList.get(position).getAbsolutePath());
        mVideoThumbLoader.showThumbByAsynctack(mVidList.get(position).getAbsolutePath(), viewHolder.mFileImage);
        viewHolder.mFileLength.setText(FiletypeUtil.timeParse(mDuration));
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
