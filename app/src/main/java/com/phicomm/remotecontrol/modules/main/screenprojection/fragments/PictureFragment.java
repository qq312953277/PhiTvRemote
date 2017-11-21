package com.phicomm.remotecontrol.modules.main.screenprojection.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.phicomm.remotecontrol.R;
import com.phicomm.remotecontrol.base.BaseFragment;
import com.phicomm.remotecontrol.modules.main.screenprojection.activities.AlbumItemActivity;
import com.phicomm.remotecontrol.modules.main.screenprojection.adapter.AlbumsAdapter;
import com.phicomm.remotecontrol.modules.main.screenprojection.event.SetClickStateEvent;
import com.phicomm.remotecontrol.modules.main.screenprojection.model.MediaContentBiz;
import com.phicomm.remotecontrol.util.SettingUtil;

import butterknife.BindView;
import butterknife.OnItemClick;


/**
 * Created by yong04.zhou on 2017/9/19.
 */
public class PictureFragment extends BaseFragment {
    public static int mAlbumIndex;
    private AlbumsAdapter adapter;

    @BindView(R.id.album_gridv)
    GridView mGridView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_picture, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mGridView.setEnabled(false);
        adapter = new AlbumsAdapter(getContext());
        adapter.setArrayList(MediaContentBiz.mPictureItemArrayList);
        mGridView.setAdapter(adapter);
    }

    @OnItemClick(R.id.album_gridv)
    public void onItemClick(int position) {
        SettingUtil.checkVibrate();
        mAlbumIndex = position;
        Intent intent = new Intent(getActivity(), AlbumItemActivity.class);
        intent.putExtra("imageList", MediaContentBiz.mPictureItemArrayList.get(position));
        intent.putExtra("pictureName", MediaContentBiz.mPicAlbumNameList.get(position));
        startActivity(intent);
    }

    @Override
    public void onEventMainThread(SetClickStateEvent event) {
        if (event.getClickState()) {
            mGridView.setEnabled(true);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
