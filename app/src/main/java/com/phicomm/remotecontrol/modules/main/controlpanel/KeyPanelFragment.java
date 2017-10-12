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
            CommonUtils.showShortToast("btn_right");
            mPresenter.sendKeyEvent(KeyCode.RIGHT);
        } else if (v == mLeftBtn) {
            CommonUtils.showShortToast("btn_left");
            mPresenter.sendKeyEvent(KeyCode.LEFT);
        } else if (v == mUpBtn) {
            CommonUtils.showShortToast("btn_up");
            mPresenter.sendKeyEvent(KeyCode.UP);
        } else if (v == mDownBtn) {
            CommonUtils.showShortToast("btn_down");
            mPresenter.sendKeyEvent(KeyCode.DOWN);
        } else if (v == mEnterBtn) {
            CommonUtils.showShortToast("btn_ok");
            mPresenter.sendKeyEvent(KeyCode.CENTER);
        } else if (v == mVolDownBtn) {
            CommonUtils.showShortToast("btn_vol_down");
            mPresenter.sendKeyEvent(KeyCode.VOL_DOWN);
        } else if (v == mVolUpBtn) {
            CommonUtils.showShortToast("btn_vol_up");
            mPresenter.sendKeyEvent(KeyCode.VOL_UP);
        } else if (v == mHomeBtn) {
            CommonUtils.showShortToast("btn_home");
            mPresenter.sendKeyEvent(KeyCode.HOME);
        } else if (v == mBackBtn) {
            CommonUtils.showShortToast("btn_back");
            mPresenter.sendKeyEvent(KeyCode.BACK);
        } else if (v == mMenuBtn) {
            CommonUtils.showShortToast("btn_menu");
            mPresenter.sendKeyEvent(KeyCode.MENU);
        } else if (v == mSettingBtn) {
            CommonUtils.showShortToast("btn_setting");
            mPresenter.sendCommand(Commands.OPEN_SETTING);
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
