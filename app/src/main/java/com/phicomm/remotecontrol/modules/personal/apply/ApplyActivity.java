package com.phicomm.remotecontrol.modules.personal.apply;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.phicomm.remotecontrol.R;
import com.phicomm.remotecontrol.base.BaseActivity;
import com.phicomm.remotecontrol.base.BaseRecycleAdapter;
import com.phicomm.remotecontrol.util.CommonUtils;
import com.phicomm.remotecontrol.util.DialogUtils;
import com.phicomm.remotecontrol.util.NetworkManagerUtils;
import com.phicomm.remotecontrol.widget.refreshlayout.CustomPtrFrameLayoutRefreshHeader;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

import static com.phicomm.remotecontrol.constant.PhiConstants.TITLE_BAR_HEIGHT_DP;

/**
 * Created by hao04.wu on 2017/9/18.
 */

public class ApplyActivity extends BaseActivity implements ApplyView, BaseRecycleAdapter.OnItemViewClickListener {

    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @BindView(R.id.recyclerview_app_list)
    RecyclerView mRecyclerView;

    @BindView(R.id.fl_app_infos)
    CustomPtrFrameLayoutRefreshHeader mRefresh;

    @BindView(R.id.rl_title)
    RelativeLayout mRlTitle;

    private ApplyAdapter mApplyAdapter;
    private ApplyPresenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply);
        init();
    }

    private void init() {
        setMarginForStatusBar(mRlTitle, TITLE_BAR_HEIGHT_DP);
        mTvTitle.setText(getString(R.string.apply_title));
        mApplyAdapter = new ApplyAdapter(this);
        mApplyAdapter.setOnItemViewClickListener(this);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        mRecyclerView.setAdapter(mApplyAdapter);
        mPresenter = new ApplyPresenterImpl(this, this);
        checkNet();
        setRefresh();
    }

    private void checkNet() {
        if (NetworkManagerUtils.instance().isDataUp()) {
            if (CommonUtils.getCurrentUrl() != null) {
                showLoadingDialog(null);
                mPresenter.getAppInfo();
            } else {
                CommonUtils.showShortToast(getString(R.string.unable_to_connect_device));
            }
        } else {
            CommonUtils.showShortToast(getString(R.string.net_connect_fail));
        }
    }

    @OnClick({R.id.iv_back})
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void showMessage(Object message) {
        CommonUtils.showShortToast((String) message);
    }

    @Override
    public void onSuccess(Object message) {
        DialogUtils.cancelLoadingDialog();
    }

    @Override
    public void onFailure(Object message) {
        DialogUtils.cancelLoadingDialog();
    }

    @Override
    public void getAppInfos(ApplyInfosBean bean) {
        if (bean == null) {
            return;
        }
        if (bean.apps != null && bean.apps.size() > 0) {
            mApplyAdapter.setData(bean.apps);
        }
    }

    private void setRefresh() {
        mRefresh.setLastUpdateTimeRelateObject(this);
        mRefresh.setPtrHandler(new PtrHandler() {

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }

            @Override
            public void onRefreshBegin(final PtrFrameLayout frame) {
                mPresenter.getAppInfo();
                frame.refreshComplete();

            }
        });
    }

    @Override
    public void onItemViewClick(Object object, int position) {
        showLoadingDialog(null);
        ApplyInfosBean.AppInfo info = (ApplyInfosBean.AppInfo) object;
        Map<String, String> options = new HashMap<>();
        options.put("activity", info.activity);
        options.put("package", info.appid);
        mPresenter.openApplication(options);
    }
}
