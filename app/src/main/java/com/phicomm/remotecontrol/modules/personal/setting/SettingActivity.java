package com.phicomm.remotecontrol.modules.personal.setting;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.phicomm.remotecontrol.R;
import com.phicomm.remotecontrol.base.BaseActivity;
import com.phicomm.remotecontrol.util.SettingUtil;

import butterknife.BindView;
import butterknife.OnClick;

public class SettingActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @BindView(R.id.tb_vibrate)
    ToggleButton mVibrate;

    @BindView(R.id.tb_voice)
    ToggleButton mVoice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        init();
    }

    private void init() {
        //进入设置界面时获取本地保存的音量、震动初始值
        mTvTitle.setText(getString(R.string.setting_title));

        mVibrate.setChecked(SettingUtil.isVibrateOn());
        mVibrate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (SettingUtil.isVibrateOn()) {
                    SettingUtil.doVibrate();
                }

                if (isChecked)
                    SettingUtil.putVibrateStatus(true);
                else
                    SettingUtil.putVibrateStatus(false);

            }
        });

        mVoice.setChecked(SettingUtil.getIsVoiceOn());
        mVoice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (SettingUtil.isVibrateOn()) {
                    SettingUtil.doVibrate();
                }

                if (isChecked)
                    SettingUtil.putVoiceStatus(true);
                else
                    SettingUtil.putVoiceStatus(false);
            }
        });
    }

    @OnClick(R.id.iv_back)
    public void onClick(View view) {
        if (SettingUtil.isVibrateOn()) {
            SettingUtil.doVibrate();
        }

        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            default:
                break;
        }
    }
}