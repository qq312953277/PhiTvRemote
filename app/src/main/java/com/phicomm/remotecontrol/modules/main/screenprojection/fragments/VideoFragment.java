package com.phicomm.remotecontrol.modules.main.screenprojection.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.phicomm.remotecontrol.R;
import com.phicomm.remotecontrol.base.BaseApplication;
import com.phicomm.remotecontrol.base.BaseFragment;
import com.phicomm.remotecontrol.modules.main.screenprojection.activities.VideoControlActivity;
import com.phicomm.remotecontrol.modules.main.screenprojection.adapter.VideoAdapter;
import com.phicomm.remotecontrol.modules.main.screenprojection.entity.DeviceDisplay;
import com.phicomm.remotecontrol.modules.main.screenprojection.model.MediaContentBiz;
import com.phicomm.remotecontrol.modules.main.screenprojection.model.UpnpServiceBiz;
import com.phicomm.remotecontrol.util.CommonUtils;
import com.phicomm.remotecontrol.util.DevicesUtil;
import com.phicomm.remotecontrol.util.SettingUtil;

import org.fourthline.cling.model.meta.Device;
import org.fourthline.cling.model.types.UDAServiceType;
import org.fourthline.cling.support.model.item.Item;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnItemClick;

import static com.phicomm.remotecontrol.base.BaseApplication.getApplication;

public class VideoFragment extends BaseFragment {
    private VideoAdapter mVideoAdapter;

    @BindView(R.id.video_listview)
    ListView mListView;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mVideoAdapter = new VideoAdapter(getContext(), MediaContentBiz.mVideoItemArrayList);
        mListView.setAdapter(mVideoAdapter);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_video, container, false);

    }

    @OnItemClick(R.id.video_listview)
    public void onItemClick(int position) {
        SettingUtil.checkVibrate();

        selectDMPToPlay(MediaContentBiz.mVideoItemArrayList.get(position), VideoControlActivity.class, position);
    }

    private void selectDMPToPlay(final Item mItem, Class<?> cls, int position) {
        boolean flag = false;
        ArrayList<Device> mPlayDeviceList = new ArrayList<>(
                UpnpServiceBiz.newInstance().getDevices(new UDAServiceType("AVTransport")));
        for (int i = 0; i < mPlayDeviceList.size(); i++) {
            if (mPlayDeviceList.get(i).toString().indexOf(DevicesUtil.getTarget().getAddress()) != -1) {
                BaseApplication mBaseApplication = getApplication();
                mBaseApplication.setDeviceDisplay(new DeviceDisplay(mPlayDeviceList.get(i)));
                mBaseApplication.setItem(mItem);
                Intent intent = new Intent();
                intent.putExtra("videoName", MediaContentBiz.mVideoItemArrayList.get(position).getTitle());
                CommonUtils.startIntent(getActivity(), intent, cls);
                flag = true;
                break;
            }
        }
        if (!flag) {
            CommonUtils.showShortToast("设备已掉线，请重新连接");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
