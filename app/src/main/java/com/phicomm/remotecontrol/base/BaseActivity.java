package com.phicomm.remotecontrol.base;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.phicomm.remotecontrol.R;
import com.phicomm.remotecontrol.RemoteBoxDevice;
import com.phicomm.remotecontrol.modules.personal.account.event.LogoutEvent;
import com.phicomm.remotecontrol.modules.personal.account.event.MultiLogoutEvent;
import com.phicomm.remotecontrol.modules.personal.account.event.OffLineEvent;
import com.phicomm.remotecontrol.modules.personal.account.local.LocalDataRepository;
import com.phicomm.remotecontrol.modules.personal.personaldetail.PersonalActivity;
import com.phicomm.remotecontrol.util.DevicesUtil;
import com.phicomm.remotecontrol.util.DialogUtils;
import com.phicomm.remotecontrol.util.ScreenUtils;
import com.phicomm.remotecontrol.util.SettingUtil;
import com.phicomm.widgets.alertdialog.PhiAlertDialog;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.ButterKnife;

/**
 * Created by hao04.wu on 2017/8/1.
 */

public class BaseActivity extends AppCompatActivity {

    public final String TAG = BaseActivity.this.getClass().getSimpleName();

    private static boolean mIsMultiLoginDialogShowing = false;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        injectViews();
        BaseApplication.getApplication().add(this);
        //StatusBarUtils.setImmersionStatusBar(this);
    }

    private void injectViews() {
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        //init eventbus
        EventBus.getDefault().register(this);

    }

    public void showLoadingDialog(Integer stringRes) {
        if (stringRes == null) {
            DialogUtils.showLoadingDialog(this);
        } else {
            DialogUtils.showLoadingDialog(this, stringRes);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        try {
            EventBus.getDefault().unregister(this);
        } catch (Exception ex) {

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BaseApplication.getApplication().remove(this);
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

    /**
     * Logout EventBus
     * 该子类凡是调用 post(new LogoutEvent())都会执行此方法
     *
     * @param msg
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventLogout(LogoutEvent msg) {

        EventBus.getDefault().post(new OffLineEvent());//使用户中心下线，在PersonalActivity接收

        //clear user data 仅清楚云端登录状态，不能把所有sp清空，登录界面“记住我”还需从本地获取登录信息
        LocalDataRepository.getInstance(BaseApplication.getContext()).setCloudLoginStatus(false);
        BaseApplication.getApplication().isLogined = false;

        //判断当前activity不是PersonalActivity则跳转过来
        if (TAG.equalsIgnoreCase(PersonalActivity.class.getSimpleName())) {

        } else {
            Intent extras = new Intent();
            extras.putExtra("offline_flag", true);
            startActivityClearTopAndFinishSelf(extras, PersonalActivity.class);
        }

    }

    /**
     * MutltiAccont EventBus
     *
     * @param msg
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMultiAccountLogout(MultiLogoutEvent msg) {

        //only show once
        if (mIsMultiLoginDialogShowing) {
            return;
        }

        mIsMultiLoginDialogShowing = true;
        PhiAlertDialog.Builder builder = new PhiAlertDialog.Builder(this);
        builder.setTitle(R.string.exit);
        builder.setMessage(R.string.kick_tips);
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EventBus.getDefault().post(new LogoutEvent());
                mIsMultiLoginDialogShowing = false;
            }
        });
        builder.show();
    }


    public void onClick(View view) {
        SettingUtil.checkVibrate();//震动事件，子类继承
    }

    /**
     * 沉浸式状态栏 ，为了避免状态栏覆盖到titlebar，需要重新计算titlebar高度，将状态栏高度算进去
     * ScreenUtils.getSystemBarHeight()状态栏高度
     *
     * @param view
     * @param titleBarHeightDp
     */
    public void setMarginForStatusBar(View view, int titleBarHeightDp) {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
//        params.height = ScreenUtils.getSystemBarHeight() + ScreenUtils.dp2px(titleBarHeightDp);
        params.height = ScreenUtils.dp2px(titleBarHeightDp);
        view.setLayoutParams(params);
    }

    private void startActivityClearTopAndFinishSelf(Intent extras, Class<?> cls) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        if (extras != null) {
            intent.putExtras(extras);
        }
        this.startActivity(intent);
        if (!this.isFinishing()) {
            this.finish();
        }
    }

}
