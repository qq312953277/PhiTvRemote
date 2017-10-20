package com.phicomm.remotecontrol.modules.main.controlpanel;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.phicomm.remotecontrol.R;
import com.phicomm.remotecontrol.constant.Commands;
import com.phicomm.remotecontrol.constant.KeyCode;
import com.phicomm.remotecontrol.util.CommonUtils;
import com.phicomm.remotecontrol.util.LogUtil;
import com.phicomm.remotecontrol.util.SettingUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by xufeng02.zhou on 2017/7/13.
 */

public class KeyPanelFragment extends Fragment implements PanelContract.View, android.view.View.OnClickListener {
    final static String TAG = "keypanel";

    private PanelContract.Presenter mPresenter;
    private Toast mToast;

    @BindView(R.id.btn_right)
    ImageView mRightBtn;

    @BindView(R.id.btn_left)
    ImageView mLeftBtn;

    @BindView(R.id.btn_up)
    ImageView mUpBtn;

    @BindView(R.id.btn_down)
    ImageView mDownBtn;

    @BindView(R.id.btn_enter)
    ImageView mEnterBtn;

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

        mRightBtn.setOnClickListener(this);
        mLeftBtn.setOnClickListener(this);
        mUpBtn.setOnClickListener(this);
        mDownBtn.setOnClickListener(this);
        mEnterBtn.setOnClickListener(this);

        mVolDownBtn.setOnClickListener(this);
        mVolUpBtn.setOnClickListener(this);

        mHomeBtn.setOnClickListener(this);
        mBackBtn.setOnClickListener(this);

        mSettingBtn.setOnClickListener(this);
        mMenuBtn.setOnClickListener(this);
        mPowerBtn.setOnClickListener(this);
    }

    @Override
    public void setPresenter(PanelContract.Presenter presenter) {
        mPresenter = presenter;
        mPresenter.setView(this);
    }

    @Override
    public void onClick(android.view.View v) {
        SettingUtil.isVibrate();

        LogUtil.d(TAG, "onClick");
        if (v == mRightBtn) {
            mPresenter.sendKeyEvent(KeyCode.RIGHT);
        } else if (v == mLeftBtn) {
            mPresenter.sendKeyEvent(KeyCode.LEFT);
        } else if (v == mUpBtn) {
            mPresenter.sendKeyEvent(KeyCode.UP);
        } else if (v == mDownBtn) {
            mPresenter.sendKeyEvent(KeyCode.DOWN);
        } else if (v == mEnterBtn) {
            mPresenter.sendKeyEvent(KeyCode.CENTER);
        } else if (v == mVolDownBtn) {
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
    public void toastMessage(String msg) {
        if (mToast == null) {
            mToast = Toast.makeText(this.getContext(), msg, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(msg);
        }
        mToast.show();
    }
}
