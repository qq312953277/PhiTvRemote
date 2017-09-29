package com.phicomm.remotecontrol.modules.devices.connectrecords;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.phicomm.remotecontrol.R;
import com.phicomm.remotecontrol.base.BaseFragment;
import com.phicomm.remotecontrol.greendao.Entity.RemoteDevice;
import com.phicomm.remotecontrol.modules.devices.connectrecords.RecentDevicesContract.Presenter;
import com.phicomm.remotecontrol.util.SettingUtil;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.phicomm.remotecontrol.constant.PhiConstants.TITLE_BAR_HEIGHT_DP;

/**
 * Created by chunya02.li on 2017/7/10.
 */

public class RecentDevicesFragment extends BaseFragment implements RecentDevicesContract.View {

    private static String TAG = "RecentDevicesFragment";

    private Presenter mPresenter;
    @BindView(R.id.recent_devices_list)
    public ListView mRecentDeivesList;

    @BindView(R.id.bt_cancel)
    public Button mCancelBtn;

    @BindView(R.id.bt_delete)
    public Button mDeleteBtn;

    @BindView(R.id.tv_sum)
    public TextView mCountTv;

    @BindView(R.id.linearLayout)
    public LinearLayout mActionBarLl;

    @BindView(android.R.id.empty)
    public TextView mEmptyTv;

    @BindView(R.id.iv_back)
    ImageView mIvBack;

    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @BindView(R.id.rl_title)
    RelativeLayout mRlTitle;

    @BindView(R.id.tv_right)
    TextView mTvEdit;

    private RecentDeviceAdapter mRecentDeviceAdapter;
    private List<RemoteDevice> mDeleteList = new ArrayList<>();
    private boolean mIsMultiSelect = false;


    public RecentDevicesFragment() {

    }

    public static RecentDevicesFragment newInstance() {
        RecentDevicesFragment fragment = new RecentDevicesFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recent_device, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        init();
        initAdapter();
        setonClickListener();

    }

    private void init() {
        mTvTitle.setText(getString(R.string.recent_connect_devices));
        mTvEdit.setVisibility(View.VISIBLE);
        mTvEdit.setText(getString(R.string.edit_recent_connect_devices));
        setMarginForStatusBar(mRlTitle, TITLE_BAR_HEIGHT_DP);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void setPresenter(RecentDevicesContract.Presenter presenter) {
        mPresenter = presenter;
    }

    private void updateView() {
        int listCount = mRecentDeviceAdapter.getCount();
        if (mIsMultiSelect) {
            for (int i = 0; i < listCount; i++) {
                mRecentDeviceAdapter.getCheckBoxVisible().put(i, View.VISIBLE);
                mRecentDeviceAdapter.getChecked().put(i, false);
            }
            mTvEdit.setVisibility(View.INVISIBLE);
            mActionBarLl.setVisibility(View.VISIBLE);
        } else {
            for (int i = 0; i < listCount; i++) {
                mRecentDeviceAdapter.getCheckBoxVisible().put(i, View.INVISIBLE);
                mRecentDeviceAdapter.getChecked().put(i, true);
            }
            mTvEdit.setVisibility(View.VISIBLE);
            mActionBarLl.setVisibility(View.GONE);
        }
    }

    @Override
    public void showRecentDevices(List<RemoteDevice> devices) {
        Log.d(TAG, "devices.size=" + devices.size());
        mRecentDeviceAdapter.notifyDataChange(devices);
    }

    @Override
    public void removeItems(List<RemoteDevice> removeItems) {
        mRecentDeviceAdapter.removeItems(removeItems);
    }

    private void initAdapter() {
        mRecentDeivesList.setEmptyView(mEmptyTv);
        mRecentDeivesList.setDivider(new ColorDrawable(Color.GRAY));
        mRecentDeivesList.setDividerHeight(1);
        mRecentDeviceAdapter = new RecentDeviceAdapter();
        mRecentDeivesList.setAdapter(mRecentDeviceAdapter);
    }

    private void setonClickListener() {
        mRecentDeivesList.setOnItemClickListener(onItemClick);
    }

    @Override
    @OnClick({R.id.iv_back, R.id.tv_right, R.id.bt_cancel, R.id.bt_delete})
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.iv_back:
                getActivity().onBackPressed();
                break;
            case R.id.tv_right:
                List<RemoteDevice> list = mRecentDeviceAdapter.getRecentDeviceList();
                if (list != null && list.size() > 0) {
                    mIsMultiSelect = true;
                    updateView();
                    clearListDelete();
                }
                break;
            case R.id.bt_cancel:
                mIsMultiSelect = false;
                updateView();
                break;
            case R.id.bt_delete:
                mIsMultiSelect = false;
                deleteChooseBox();
                updateView();
                break;
            default:
                break;
        }
    }

    AdapterView.OnItemClickListener onItemClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (SettingUtil.isVibrateOn()) {
                SettingUtil.doVibrate();
            }

            RemoteDevice device = (RemoteDevice) parent.getAdapter().getItem(position);
            RecentDeviceAdapter.DeviceViewHolder holder = (RecentDeviceAdapter.DeviceViewHolder)
                    view.getTag();
            if (mIsMultiSelect && mRecentDeviceAdapter.getRecentDeviceList().size() > 0) {
                CheckBox collectCheckBox = holder.getCollectCheckBox();
                if (collectCheckBox.isChecked()) {
                    collectCheckBox.setChecked(false);
                    mDeleteList.remove(device);
                } else {
                    collectCheckBox.setChecked(true);
                    mDeleteList.add(device);
                }
                mCountTv.setText(makeDeviceCountLabel(mDeleteList.size()));
            }
        }
    };

    private void clearListDelete() {
        mDeleteList.clear();
        mCountTv.setText(makeDeviceCountLabel(0));
    }

    private void deleteChooseBox() {
        mPresenter.removeSelectedDevice(mDeleteList);
    }

    public String makeDeviceCountLabel(int count) {
        StringBuilder deviceCounts = new StringBuilder();
        StringBuilder format = new StringBuilder();
        Formatter formatter = new Formatter(format, Locale.getDefault());
        String f = getResources().getQuantityText(R.plurals.choose_count_box, count)
                .toString();
        deviceCounts.setLength(0);
        formatter.format(f, Integer.valueOf(count));
        deviceCounts.append(format);
        return deviceCounts.toString();
    }
}
