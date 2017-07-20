package com.phicomm.remotecontrol.fragments.controlpanel;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.phicomm.remotecontrol.R;
import com.phicomm.remotecontrol.fragments.spinnerlist.SpinnerListFragment;
import com.phicomm.remotecontrol.util.ActivityUtils;
import com.phicomm.remotecontrol.util.LogUtil;

import java.util.ArrayList;

/**
 * Created by xufeng02.zhou on 2017/7/17.
 */

public class ControlPanelFragment extends Fragment implements PanelContract.View {
    private KeyPanelFregment mKeypanelFragment;
    private TouchPanelFregment mTouchPanelFragment;
    private ArrayList<Fragment> mFragmentList;
    private PanelContract.Presenter mPresenter;

    public ControlPanelFragment() {
        mPresenter = new PanelPresenter();
        mPresenter.setView(this);

        mKeypanelFragment = KeyPanelFregment.newInstance();
        mKeypanelFragment.setPresenter(mPresenter);

        mTouchPanelFragment = TouchPanelFregment.newInstance();
        mTouchPanelFragment.setPresenter(mPresenter);

        mFragmentList = new ArrayList<Fragment>();
        mFragmentList.add(mKeypanelFragment);
        mFragmentList.add(mTouchPanelFragment);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_controler, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LogUtil.d("ControlPanelFragment onViewCreated");
        SpinnerListFragment spinnerListFragment = (SpinnerListFragment)
                getChildFragmentManager().findFragmentById(R.id.spinner_container);

        if (spinnerListFragment == null) {
            spinnerListFragment = SpinnerListFragment.newInstance();
            ActivityUtils.addFragmentToFragment(getChildFragmentManager(),
                    spinnerListFragment, R.id.spinner_container);
        }

        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewPageMainContent);
        FragmentManager fm = this.getChildFragmentManager();
        viewPager.setAdapter(new ViewPageAdapter(fm, mFragmentList));

    }

    @Override
    public void onStop() {
        super.onStop();
    }


    @Override
    public void toastMessage() {

    }

    @Override
    public void setPresenter(PanelContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
