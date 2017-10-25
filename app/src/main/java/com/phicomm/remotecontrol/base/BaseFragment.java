package com.phicomm.remotecontrol.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.LinearLayout;

import com.phicomm.remotecontrol.modules.main.screenprojection.event.SetEnableEvent;
import com.phicomm.remotecontrol.util.DialogUtils;
import com.phicomm.remotecontrol.util.ScreenUtils;
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
    public void onEventMainThread(SetEnableEvent event) {
    }

    public void onClick(View view) {
        SettingUtil.checkVibrate();//震动事件，子类继承
    }

    /**
     * 沉浸式状态栏 ，为了避免状态栏覆盖到titlebar，需要重新计算titlebar高度，将状态栏高度算进去
     * ScreenUtils.getSystemBarHeight()状态栏高度
     *
     * @param view
     * @param titleBarHeightDp
     */
    public void setMarginForStatusBar(View view, int titleBarHeightDp) {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
        //params.height = ScreenUtils.getSystemBarHeight() + ScreenUtils.dp2px(titleBarHeightDp);
        params.height = ScreenUtils.dp2px(titleBarHeightDp);
        view.setLayoutParams(params);
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
