package com.phicomm.remotecontrol.modules.main.screenprojection.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yong04.zhou on 2017/9/19.
 */


public class MyFragmentAdapter extends FragmentPagerAdapter {

    public interface GetFragmentCallback {
        void initFragmentList(FragmentManager fm, List<Fragment> fragmentList);

    }

    List<Fragment> mFragmentList = new ArrayList<>();
    private GetFragmentCallback mCallback;

    public MyFragmentAdapter(FragmentManager fm, GetFragmentCallback callback) {
        super(fm);
        this.mCallback = callback;
        callback.initFragmentList(fm, mFragmentList);
    }


    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

}

