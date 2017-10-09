package com.phicomm.remotecontrol.modules.main.screenprojection.activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kang.sun on 2017/10/8.
 */

public class MyFragmentAdapter extends FragmentPagerAdapter {
    public interface GetFragmentCallback {
        void initFragmentList(FragmentManager fm, List<Fragment> fragmentList);

        CharSequence getPageTitle(int position);
    }

    List<Fragment> fragmentList = new ArrayList<Fragment>();
    private GetFragmentCallback callback;

    public MyFragmentAdapter(FragmentManager fm, GetFragmentCallback callback) {
        super(fm);
        this.callback = callback;
        callback.initFragmentList(fm, fragmentList);
    }

    public MyFragmentAdapter(FragmentManager fm, List<Fragment> fragmentList) {
        super(fm);
        this.fragmentList = fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return callback.getPageTitle(position);
    }
}
