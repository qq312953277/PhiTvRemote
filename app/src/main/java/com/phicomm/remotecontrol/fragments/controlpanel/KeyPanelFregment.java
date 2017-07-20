package com.phicomm.remotecontrol.fragments.controlpanel;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;

import com.phicomm.remotecontrol.R;
import com.phicomm.remotecontrol.util.LogUtil;
import com.phicomm.remotecontrol.constant.KeyCode;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by xufeng02.zhou on 2017/7/13.
 */

public class KeyPanelFregment extends Fragment implements PanelContract.View, android.view.View.OnClickListener {
    final static String TAG = "keypanel";

    PanelContract.Presenter mPresenter;

    @BindView(R.id.btn_right)
    Button mRightBtn;
    @BindView(R.id.btn_left)
    Button mLeftBtn;
    @BindView(R.id.btn_up)
    Button mUpBtn;
    @BindView(R.id.btn_down)
    Button mDownBtn;
    @BindView(R.id.btn_enter)
    Button mEnterBtn;

    @BindView(R.id.btn_vol_up)
    Button mVolUpBtn;
    @BindView(R.id.btn_vol_down)
    Button mVolDownBtn;

    @BindView(R.id.btn_chanel_up)
    Button mChanelUpBtn;
    @BindView(R.id.btn_chanel_down)
    Button mChanelDownBtn;

    @BindView(R.id.btn_home)
    Button mHomeBtn;
    @BindView(R.id.btn_back)
    Button mBackBtn;


    public static KeyPanelFregment newInstance() {
        return new KeyPanelFregment();
    }

    public KeyPanelFregment() {

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

        mChanelDownBtn.setOnClickListener(this);
        mChanelUpBtn.setOnClickListener(this);

        mHomeBtn.setOnClickListener(this);
        mBackBtn.setOnClickListener(this);
    }

    @Override
    public void setPresenter(PanelContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onClick(android.view.View v) {
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
            mPresenter.sendKeyEvent(KeyCode.ENTER);
        } else if (v == mVolDownBtn) {
            mPresenter.sendKeyEvent(KeyCode.VOL_DOWN);
        } else if (v == mVolUpBtn) {
            mPresenter.sendKeyEvent(KeyCode.VOL_UP);
        } else if (v == mChanelDownBtn) {
            mPresenter.sendKeyEvent(KeyCode.CHANEL_DOWN);
        } else if (v == mChanelUpBtn) {
            mPresenter.sendKeyEvent(KeyCode.CHANEL_UP);
        } else if (v == mHomeBtn) {
            mPresenter.sendKeyEvent(KeyCode.HOME);
        } else if (v == mBackBtn) {
            mPresenter.sendKeyEvent(KeyCode.BACK);
        }
    }

    @Override
    public void toastMessage() {

    }
}
