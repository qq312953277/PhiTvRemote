package com.phicomm.remotecontrol.fragments.controlpanel;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by xufeng02.zhou on 2017/7/14.
 */

public class ViewPageAdapter extends FragmentPagerAdapter {
    List<Fragment> mFragmentList;

    public ViewPageAdapter(FragmentManager fm, List<Fragment> list) {
        super(fm);
        mFragmentList = list;
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
