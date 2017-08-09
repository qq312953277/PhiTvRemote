package com.phicomm.remotecontrol.fragments.controlpanel;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import com.phicomm.remotecontrol.R;
import com.phicomm.remotecontrol.base.BaseFragment;

/**
 * Created by xufeng02.zhou on 2017/7/13.
 */

public class TouchPanelFregment extends BaseFragment implements PanelContract.View {

    PanelContract.Presenter mPresenter;
    private Toast mToast;

    public static TouchPanelFregment newInstance() {
        return new TouchPanelFregment();
    }

    public TouchPanelFregment() {

    }

    @Override
    public android.view.View onCreateView(LayoutInflater inflater, ViewGroup container,
                                          Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_touchpanel, container, false);
    }

    @Override
    public void onViewCreated(android.view.View view, @Nullable Bundle savedInstanceState) {

    }

    @Override
    public void setPresenter(PanelContract.Presenter presenter) {
        mPresenter = presenter;
        mPresenter.setView(this);
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
