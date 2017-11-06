package com.phicomm.remotecontrol.modules.main.screenprojection.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.phicomm.remotecontrol.ConnectManager;
import com.phicomm.remotecontrol.R;
import com.phicomm.remotecontrol.base.BaseActivity;
import com.phicomm.remotecontrol.base.BaseApplication;
import com.phicomm.remotecontrol.modules.main.controlpanel.DeviceDetectEvent;
import com.phicomm.remotecontrol.modules.main.screenprojection.adapter.AlbumItemAdapter;
import com.phicomm.remotecontrol.modules.main.screenprojection.entity.DeviceDisplay;
import com.phicomm.remotecontrol.modules.main.screenprojection.entity.PictureItemList;
import com.phicomm.remotecontrol.modules.main.screenprojection.model.UpnpServiceBiz;
import com.phicomm.remotecontrol.modules.main.screenprojection.presenter.LocalMediaItemPresenterImpl;
import com.phicomm.remotecontrol.util.CommonUtils;
import com.phicomm.remotecontrol.util.DevicesUtil;
import com.phicomm.remotecontrol.util.DialogUtils;
import com.phicomm.remotecontrol.util.SettingUtil;

import org.fourthline.cling.model.meta.Device;
import org.fourthline.cling.model.types.UDAServiceType;
import org.fourthline.cling.support.model.item.Item;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnItemClick;

import static com.phicomm.remotecontrol.constant.PhiConstants.TITLE_BAR_HEIGHT_DP;



/**
 * Created by kang.sun on 2017/10/20.
 */

public class AlbumItemActivity extends BaseActivity {
    private PictureItemList mPictureItemList;
    private AlbumItemAdapter adapter;
    private String mAlbumName;
    private int mPosition;

    @BindView(R.id.album_item_grid)
    GridView mGridView;

    @BindView(R.id.rl_title)
    RelativeLayout mRlTitle;

    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @BindView(R.id.iv_back)
    ImageView mBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_album_item);
        initTitleView();
        init();
    }

    private void init() {
        adapter = new AlbumItemAdapter(mPictureItemList.getPictureItemList(), this);
        mGridView.setAdapter(adapter);
    }

    private void initTitleView() {
        Intent intent = getIntent();
        mPictureItemList = (PictureItemList) intent.getSerializableExtra("imageList");
        mAlbumName = (String) intent.getSerializableExtra("pictureName");
        mTvTitle.setText(mAlbumName);
        setMarginForStatusBar(mRlTitle, TITLE_BAR_HEIGHT_DP);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingUtil.checkVibrate();

                finish();
            }
        });
    }

    @OnItemClick(R.id.album_item_grid)
    public void onItemClick(int position) {
        mPosition = position;
        SettingUtil.checkVibrate();
        //检测设备是否在线
        ConnectManager.getInstance().deviceDetect();
        DialogUtils.showLoadingDialog(this);
    }

    @Override
    public void onEventMainThread(DeviceDetectEvent event) {
        DialogUtils.cancelLoadingDialog();
        if (event.getTargetState()) {
            selectDMPToPlay(LocalMediaItemPresenterImpl.mDlnaPictureMapList
                    .get(mPictureItemList.getPictureItemList().get(mPosition).getId()), PictureControlActivity.class);
        } else {
            CommonUtils.showToastBottom(getString(R.string.fail_screenprojection));
        }
    }

    private void selectDMPToPlay(final Item mItem, Class<?> cls) {
        boolean flag = false;
        ArrayList<Device> mPlayDeviceList = new ArrayList<Device>(
                UpnpServiceBiz.newInstance().getDevices(new UDAServiceType("AVTransport")));
        for (int i = 0; i < mPlayDeviceList.size(); i++) {
            if (mPlayDeviceList.get(i).toString().indexOf(DevicesUtil.getTarget().getAddress()) != -1) {
                BaseApplication mBaseApplication = (BaseApplication) getApplication();
                mBaseApplication.setDeviceDisplay(new DeviceDisplay(mPlayDeviceList.get(i)));
                mBaseApplication.setItem(mItem);
                CommonUtils.startIntent(this, null, cls);
                flag = true;
                break;
            }
        }
        if (!flag) {
            CommonUtils.showToastBottom(getString(R.string.fail_screenprojection));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
