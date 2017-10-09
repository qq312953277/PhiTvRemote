package com.phicomm.remotecontrol.modules.main.screenprojection.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.phicomm.remotecontrol.R;
import com.phicomm.remotecontrol.base.BaseFragment;
import com.phicomm.remotecontrol.modules.main.screenprojection.activities.PictureEvent;
import com.phicomm.remotecontrol.modules.main.screenprojection.presenter.LocalMediaItemPresenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnItemClick;


/**
 * Created by yong04.zhou on 2017/9/19.
 */

public class VideoFragment extends BaseFragment {
    public static int mLayer = 0;
    private LocalMediaItemPresenter mLocalMediaItemPresenter;
    @BindView(R.id.video_listview)
    ListView mListView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_video, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    /**
     * 显示本地视频和照片
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void showItems(PictureEvent event) {
        if (event.mType == 1) {
            mListView.setAdapter(event.mItems);
            mLocalMediaItemPresenter = event.mLocalMediaItemPresenter;
        }
    }

    @OnItemClick(R.id.video_listview)
    public void onItemClick(int position) {
        mLayer = 1;
        mLocalMediaItemPresenter.browserItems(position, this);
    }

    @Override
    public void onDestroy() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();
    }
}
