package com.phicomm.remotecontrol.modules.main.screenprojection.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.phicomm.remotecontrol.R;
import com.phicomm.remotecontrol.base.BaseApplication;
import com.phicomm.remotecontrol.base.BaseFragment;
import com.phicomm.remotecontrol.modules.main.screenprojection.adapter.VideoAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;

/**
 * Created by yong04.zhou on 2017/9/19.
 */

public class VideoFragment extends BaseFragment {

    @BindView(R.id.video_listview)
    ListView mVideoListView;

    private VideoAdapter mVideoAdapter;
    private Cursor mCursor;

    public static HashMap<String, Long> mVideoMap = new HashMap<>();
    public static ArrayList<File> mVidData = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_video, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        initView();
    }

    private void initView() {
        mVideoMap.clear();
        mVidData.clear();
        setVideoListData();
        mVideoAdapter = new VideoAdapter(mVidData, mVideoMap);
        mVideoListView.setAdapter(mVideoAdapter);
        mVideoListView.setOnItemClickListener(new VideoItemClickListener());
    }


    private void setVideoListData() {
        try {
            mCursor = BaseApplication.getContext().getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                    new String[]{MediaStore.Video.Media.TITLE, MediaStore.Video.Media.MIME_TYPE, MediaStore.Video.Media.DATA,
                            MediaStore.Video.Media.DURATION}, null, null, null);
            if (mCursor != null) {
                mCursor.moveToFirst();
                int count = mCursor.getCount();
                for (int i = 0; i < count; i++) {
                    mVideoMap.put(mCursor.getString(2).trim(), (long) mCursor.getInt(3));
                    mVidData.add(new File(mCursor.getString(2).trim()));
                    mCursor.moveToNext();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (mCursor != null) {
                mCursor.close();
            }
        }
    }


    class VideoItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            Uri uri = Uri.parse("file://" + mVidData.get(position));
            intent.setDataAndType(uri, "video/*");
            startActivity(intent);
        }
    }

}
