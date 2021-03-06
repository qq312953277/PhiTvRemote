package com.phicomm.remotecontrol.modules.main.controlpanel;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.phicomm.remotecontrol.R;
import com.phicomm.remotecontrol.constant.Commands;
import com.phicomm.remotecontrol.constant.KeyCode;
import com.phicomm.remotecontrol.util.CommonUtils;
import com.phicomm.remotecontrol.util.DevicesUtil;
import com.phicomm.remotecontrol.util.LogUtil;
import com.phicomm.remotecontrol.util.SettingUtil;
import com.phicomm.remotecontrol.widget.CircleView;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by xufeng02.zhou on 2017/7/13.
 */

public class KeyPanelFragment extends Fragment implements PanelContract.View, android.view.View.OnClickListener, CircleView.TouchListener {
    final static String TAG = "keypanel";
    private PanelContract.Presenter mPresenter;
    private Toast mToast;

    @BindView(R.id.btn_vol_up)
    ImageButton mVolUpBtn;

    @BindView(R.id.btn_vol_down)
    ImageButton mVolDownBtn;

    @BindView(R.id.btn_home)
    ImageButton mHomeBtn;

    @BindView(R.id.btn_back)
    ImageButton mBackBtn;

    @BindView(R.id.btn_setting)
    ImageButton mSettingBtn;

    @BindView(R.id.btn_menu)
    ImageButton mMenuBtn;

    @BindView(R.id.btn_power)
    ImageButton mPowerBtn;

    @BindView(R.id.circleView)
    CircleView mCircleView;


    public static KeyPanelFragment newInstance() {
        return new KeyPanelFragment();
    }

    public KeyPanelFragment() {
    }

    @Override
    public android.view.View onCreateView(LayoutInflater inflater, ViewGroup container,
                                          Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        LogUtil.d(TAG, "onCreateView");
        return inflater.inflate(R.layout.fragment_keypanel, container, false);
    }

    @Override
    public void onViewCreated(android.view.View view, @Nullable Bundle savedInstanceState) {
        LogUtil.d(TAG, "onViewCreated");
        ButterKnife.bind(this, view);
        mVolDownBtn.setOnClickListener(this);
        mVolUpBtn.setOnClickListener(this);
        mHomeBtn.setOnClickListener(this);
        mBackBtn.setOnClickListener(this);
        mSettingBtn.setOnClickListener(this);
        mMenuBtn.setOnClickListener(this);
        mPowerBtn.setOnClickListener(this);

        mCircleView.setTouchListener(this);
    }

    @Override
    public void setPresenter(PanelContract.Presenter presenter) {
        mPresenter = presenter;
        mPresenter.setView(this);
    }

    @Override
    public void onClick(android.view.View v) {
        SettingUtil.checkVibrate();
        LogUtil.d(TAG, "onClick");
        if (v == mVolDownBtn) {
            mPresenter.sendKeyEvent(KeyCode.VOL_DOWN);
        } else if (v == mVolUpBtn) {
            mPresenter.sendKeyEvent(KeyCode.VOL_UP);
        } else if (v == mHomeBtn) {
            mPresenter.sendKeyEvent(KeyCode.HOME);
        } else if (v == mBackBtn) {
            mPresenter.sendKeyEvent(KeyCode.BACK);
        } else if (v == mMenuBtn) {
            mPresenter.sendKeyEvent(KeyCode.MENU);
        } else if (v == mSettingBtn) {
            mPresenter.sendCommand(Commands.OPEN_SETTING);
        } else if (v == mPowerBtn) {
            mPresenter.sendKeyEvent(KeyCode.POWER);
        }
    }

    @Override
    public void onTouchRight() {
        mPresenter.sendKeyEvent(KeyCode.RIGHT);
    }

    @Override
    public void onTouchLeft() {
        mPresenter.sendKeyEvent(KeyCode.LEFT);
    }

    @Override
    public void onTouchUp() {
        mPresenter.sendKeyEvent(KeyCode.UP);
    }

    @Override
    public void onTouchDown() {
        mPresenter.sendKeyEvent(KeyCode.DOWN);
    }

    @Override
    public void onTouchCenter() {
        mPresenter.sendKeyEvent(KeyCode.CENTER);
    }

    @Override
    public void onTouchOut() {
    }

    @Override
    public void toastMessage(String msg) {
        if (mToast == null) {
            mToast = Toast.makeText(this.getContext(), msg, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(msg);
        }
        mToast.show();
    }

    @Override
    public void connectFail() {
        EventBus.getDefault().post(new LogoffNoticeEvent(true));
        DevicesUtil.setTarget(null);
        CommonUtils.showShortToast(getString(R.string.unable_to_connect_device));
    }
}
