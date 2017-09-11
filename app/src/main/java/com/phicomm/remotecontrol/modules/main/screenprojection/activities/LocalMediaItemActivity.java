package com.phicomm.remotecontrol.modules.main.screenprojection.activities;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;

import com.phicomm.remotecontrol.R;
import com.phicomm.remotecontrol.base.BaseActivity;
import com.phicomm.remotecontrol.base.BaseApplication;
import com.phicomm.remotecontrol.modules.main.screenprojection.adapter.GeneralAdapter;
import com.phicomm.remotecontrol.modules.main.screenprojection.entity.ContentItem;
import com.phicomm.remotecontrol.modules.main.screenprojection.presenter.LocalMediaItemPresenter;
import com.phicomm.remotecontrol.modules.main.screenprojection.presenter.LocalMediaItemPresenterImpl;

import butterknife.BindView;
import butterknife.OnItemClick;

/**
 * Created by kang.sun on 2017/8/31.
 */
public class LocalMediaItemActivity extends BaseActivity implements LocalMediaItemView {
    private final static String TAG = "LocalMediaItemActivity";
    private LocalMediaItemPresenter mLocalMediaItemPresenter;
    @BindView(R.id.tb_title)
    Toolbar mToolbar;
    @BindView(R.id.lv_devices)
    ListView mListViewContents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screenprojection);
        init();
        setToolBar();
        mLocalMediaItemPresenter.showItems();
    }

    private void setToolBar() {
        // 设置显示ToolBar
        setSupportActionBar(mToolbar);
    }

    private void init() {
        mLocalMediaItemPresenter = new LocalMediaItemPresenterImpl(this, this, (BaseApplication) getApplication());
    }

    @OnItemClick(R.id.lv_devices)
    public void onItemClick(int position) {
        mLocalMediaItemPresenter.browserItems(position, LocalMediaItemActivity.this);
    }

    /**
     * 显示本地视频和照片
     */
    @Override
    public void showItems(GeneralAdapter<ContentItem> mContentAdapter) {
        mListViewContents.setAdapter(mContentAdapter);
    }

    @Override
    public void showMessage(Object message) {
    }

    @Override
    public void onSuccess(Object message) {
    }

    @Override
    public void onFailure(Object message) {
    }
}
