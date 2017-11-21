package com.phicomm.remotecontrol.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.phicomm.remotecontrol.modules.main.controlpanel.DeviceDetectEvent;
import com.phicomm.remotecontrol.modules.main.controlpanel.LogoffNoticeEvent;
import com.phicomm.remotecontrol.modules.main.screenprojection.event.CheckTargetEvent;
import com.phicomm.remotecontrol.modules.main.screenprojection.event.SetClickStateEvent;
import com.phicomm.remotecontrol.util.DialogUtils;
import com.phicomm.remotecontrol.util.SettingUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.ButterKnife;

/**
 * Created by hao04.wu on 2017/8/1.
 */

public class BaseFragment extends Fragment {
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }

    @Override
    public void onResume() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onPause();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(SetClickStateEvent event) {
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(CheckTargetEvent event) {
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(LogoffNoticeEvent event) {
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(DeviceDetectEvent event) {
    }

    public void onClick(View view) {
        SettingUtil.checkVibrate();//震动事件，子类继承
    }

    public void showLoadingDialog(Integer stringRes) {
        if (stringRes == null) {
            DialogUtils.showLoadingDialog(getActivity());
        } else {
            DialogUtils.showLoadingDialog(getActivity(), stringRes);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
