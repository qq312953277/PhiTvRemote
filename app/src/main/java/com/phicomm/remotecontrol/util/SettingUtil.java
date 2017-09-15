package com.phicomm.remotecontrol.util;

import android.widget.Toast;

import com.phicomm.remotecontrol.TaskQuene;
import com.phicomm.remotecontrol.base.BaseApplication;
import com.phicomm.remotecontrol.constant.KeyCode;
import com.phicomm.remotecontrol.httpclient.PhiCallBack;
import com.phicomm.remotecontrol.preference.PreferenceDef;

/**
 * Created by yong04.zhou on 2017/9/11.
 */

public class SettingUtil {

    public static final int VIBRATE_DURATION = 60;

    public static boolean isVibrateOn() {
        return (Boolean) BaseApplication.getApplication().getmPreferenceRepository().get(PreferenceDef.SP_SETTINGS, PreferenceDef.VIBRATE_STATUS, false);
    }

    public static void putVibrateStatus(Boolean status) {
        BaseApplication.getApplication().getmPreferenceRepository().put(PreferenceDef.SP_SETTINGS, PreferenceDef.VIBRATE_STATUS, status);
    }

    public static boolean getIsVoiceOn() {
        return (Boolean) BaseApplication.getApplication().getmPreferenceRepository().get(PreferenceDef.SP_SETTINGS, PreferenceDef.VOICE_STATUS, false);
    }

    public static void putVoiceStatus(Boolean status) {
        BaseApplication.getApplication().getmPreferenceRepository().put(PreferenceDef.SP_SETTINGS, PreferenceDef.VOICE_STATUS, status);
    }

    public static void doVibrate() {
        VibrateUtil.vibrate(BaseApplication.getContext(), VIBRATE_DURATION);
    }

    public static void voiceUp() {
        TaskQuene.getInstance().sendKeyEvent(KeyCode.VOL_UP, new PhiCallBack() {
            @Override
            public void onSuccess(Object model) {
                Toast.makeText(BaseApplication.getContext(), "SUCCESS", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(String msg) {

            }

            @Override
            public void onFinish() {

            }
        });
    }

    public static void voiceDown() {
        TaskQuene.getInstance().sendKeyEvent(KeyCode.VOL_UP, new PhiCallBack() {
            @Override
            public void onSuccess(Object model) {
                Toast.makeText(BaseApplication.getContext(), "SUCCESS", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(String msg) {

            }

            @Override
            public void onFinish() {

            }
        });
    }

    public static void isVibrate() {
        if (SettingUtil.isVibrateOn()) {
            SettingUtil.doVibrate();
        }
    }
}






