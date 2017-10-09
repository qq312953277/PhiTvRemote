package com.phicomm.remotecontrol.modules.main.screenprojection.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.phicomm.remotecontrol.R;
import com.phicomm.remotecontrol.base.BaseFragment;
import com.phicomm.remotecontrol.modules.main.screenprojection.activities.PictureEvent;
import com.phicomm.remotecontrol.modules.main.screenprojection.adapter.GeneralAdapter;
import com.phicomm.remotecontrol.modules.main.screenprojection.entity.ContentItem;
import com.phicomm.remotecontrol.modules.main.screenprojection.presenter.LocalMediaItemPresenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnItemClick;


/**
 * Created by yong04.zhou on 2017/9/19.
 */
public class PictureFragment extends BaseFragment {
    public static int mLayer = 0;
    private GeneralAdapter<ContentItem> mContentItemAdapter;
    private LocalMediaItemPresenter mLocalMediaItemPresenter;
    @BindView(R.id.album_gridv)
    GridView mGridView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_picture, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState); //init eventbus
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @OnItemClick(R.id.album_gridv)
    public void onItemClick(int position) {
        mLayer = 1;
        mLocalMediaItemPresenter.browserItems(position, this);
    }

    /**
     * 显示本地视频和照片
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void showItems(PictureEvent event) {
        if (event.mType == 0) {
            mGridView.setAdapter(event.mItems);
            mContentItemAdapter = event.mItems;
            mLocalMediaItemPresenter = event.mLocalMediaItemPresenter;
        }
    }

    @Override
    public void onDestroy() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();
    }
}
