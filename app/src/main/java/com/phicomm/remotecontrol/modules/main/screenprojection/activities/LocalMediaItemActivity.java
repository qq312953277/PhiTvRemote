package com.phicomm.remotecontrol.modules.main.screenprojection.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.phicomm.remotecontrol.R;
import com.phicomm.remotecontrol.base.BaseActivity;
import com.phicomm.remotecontrol.modules.main.screenprojection.adapter.MyFragmentAdapter;
import com.phicomm.remotecontrol.modules.main.screenprojection.fragments.MainFragmentTab;

import java.util.List;

import butterknife.BindView;

import static com.phicomm.remotecontrol.constant.PhiConstants.TITLE_BAR_HEIGHT_DP;

/**
 * Created by kang.sun on 2017/8/31.
 */
public class LocalMediaItemActivity extends BaseActivity implements MyFragmentAdapter.GetFragmentCallback {
    @BindView(R.id.view_pager)
    ViewPager mViewPage;

    @BindView(R.id.radio_group)
    RadioGroup mRadioGroup;

    @BindView(R.id.picture)
    RadioButton mPic;

    @BindView(R.id.video)
    RadioButton mVid;

    @BindView(R.id.rl_title)
    RelativeLayout mRlTitle;

    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @BindView(R.id.iv_back)
    ImageView mBack;

    private MyFragmentAdapter mPageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screenprojection);
        initTitleView();
        initView();
    }

    private void initTitleView() {
        setMarginForStatusBar(mRlTitle, TITLE_BAR_HEIGHT_DP);
        mTvTitle.setText(getString(R.string.local_screenprojection));
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private void initView() {
        mPageAdapter = new MyFragmentAdapter(getSupportFragmentManager(), this);
        mViewPage.setAdapter(mPageAdapter);
        mViewPage.setOffscreenPageLimit(mPageAdapter.getCount());//表示两个界面之间来回切换都不会重新加载
        mViewPage.setOnPageChangeListener(new ViewPager.OnPageChangeListener() { //导航条同步
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    mPic.setChecked(true);
                    mVid.setChecked(false);
                } else {
                    mVid.setChecked(true);
                    mPic.setChecked(false);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.picture:
                        mViewPage.setCurrentItem(0, false);//切换效果
                        break;
                    case R.id.video:
                        mViewPage.setCurrentItem(1, false);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    @Override
    public void initFragmentList(FragmentManager fm, List<Fragment> fragmentList) {
        for (MainFragmentTab tab : MainFragmentTab.values()) {
            try {
                Fragment fragment = null;
                List<Fragment> fs = fm.getFragments();
                if (fs != null) {
                    for (Fragment f : fs) {
                        if (f.getClass() == tab.mClazz) {
                            fragment = f;
                            break;
                        }
                    }
                }
                if (fragment == null) {
                    fragment = tab.mClazz.newInstance();
                }
                fragmentList.add(fragment);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

}
