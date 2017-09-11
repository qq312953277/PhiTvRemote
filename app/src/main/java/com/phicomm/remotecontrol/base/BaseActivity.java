package com.phicomm.remotecontrol.base;

import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;

import com.phicomm.remotecontrol.RemoteBoxDevice;
import com.phicomm.remotecontrol.util.DevicesUtil;
import com.phicomm.remotecontrol.util.SettingUtil;

import butterknife.ButterKnife;

/**
 * Created by hao04.wu on 2017/8/1.
 */

public class BaseActivity extends AppCompatActivity {


    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        injectViews();
        BaseApplication.getApplication().add(this);
    }

    private void injectViews() {
        ButterKnife.bind(this);
    }


    //监听音量按键，它的子Activity都能监听到
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        RemoteBoxDevice target = DevicesUtil.getTarget(); //获取当前连接设备，只有连接上了按键才有效
        if (target != null && SettingUtil.getIsVoiceOn()) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_VOLUME_UP:
                    SettingUtil.voiceUp();
                    return true;
                case KeyEvent.KEYCODE_VOLUME_DOWN:
                    SettingUtil.voiceDown();
                    return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

}
