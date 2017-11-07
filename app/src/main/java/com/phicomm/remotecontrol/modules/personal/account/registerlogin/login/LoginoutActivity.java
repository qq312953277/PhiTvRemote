package com.phicomm.remotecontrol.modules.personal.account.registerlogin.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.phicomm.remotecontrol.R;
import com.phicomm.remotecontrol.base.BaseActivity;
import com.phicomm.remotecontrol.base.BaseApplication;
import com.phicomm.remotecontrol.modules.personal.account.event.LogoutEvent;
import com.phicomm.remotecontrol.modules.personal.account.http.CustomSubscriber;
import com.phicomm.remotecontrol.modules.personal.account.http.HttpDataRepository;
import com.phicomm.remotecontrol.modules.personal.account.local.LocalDataRepository;
import com.phicomm.remotecontrol.modules.personal.account.resultbean.AccountDetailBean;
import com.phicomm.remotecontrol.util.SettingUtil;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

import static com.phicomm.remotecontrol.constant.PhiConstants.TITLE_BAR_HEIGHT_DP;
import static com.phicomm.remotecontrol.modules.personal.personaldetail.PersonalActivity.LOGINOUT_RESULT_CODE;

/**
 * Created by yong04.zhou on 2017/9/14.
 */

public class LoginoutActivity extends BaseActivity {

    @BindView(R.id.iv_header_picture)
    ImageView mHeaderPicture;

    @BindView(R.id.tv_user_name)
    TextView mUserName;

    @BindView(R.id.rl_title)
    RelativeLayout mRlTitle;

    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @BindView(R.id.iv_back)
    ImageView mBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginout);

        initViews();

        checkMultiLoginedRequest();
    }

    private void initViews() {
        mTvTitle.setText(getString(R.string.exit));
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent = getIntent();
        String img = intent.getStringExtra("img");
        String userName = intent.getStringExtra("userName");
        if (!TextUtils.isEmpty(userName)) {
            mUserName.setText(getString(R.string.phicomm_account) + userName);
        }
        if (!TextUtils.isEmpty(img)) {
            Glide.with(BaseApplication.getContext()).load(img).dontAnimate()
                    .error(R.drawable.default_avatar).placeholder(R.drawable.icon_photo_loading).priority(Priority.HIGH).into(mHeaderPicture);
        } else {
            mHeaderPicture.setImageResource(R.drawable.default_avatar);
        }

    }

    private void checkMultiLoginedRequest() {
        String access_token = LocalDataRepository.getInstance(BaseApplication.getContext()).getAccessToken();
        HttpDataRepository.getInstance().accountDetail(new CustomSubscriber<AccountDetailBean>() {
            @Override
            public void onCustomNext(AccountDetailBean accountDetailBean) {

            }
        }, access_token);
    }

    @Override
    @OnClick({R.id.bt_loginout, R.id.iv_header_picture, R.id.tv_user_name})
    public void onClick(View view) {
        super.onClick(view);

        final AlertDialog myDialog = new AlertDialog.Builder(this).create();
        myDialog.show();
        myDialog.setCancelable(false);
        myDialog.getWindow().setContentView(R.layout.loginout_alertdialog);
        myDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);//与background属性配合设置圆角边框的作用
        TextView confirmBtn = (TextView) myDialog.getWindow().findViewById(R.id.tv_confirm);
        TextView cancelBtn = (TextView) myDialog.getWindow().findViewById(R.id.bt_cancel);

        cancelBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                SettingUtil.checkVibrate();
                myDialog.dismiss();
            }
        });

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingUtil.checkVibrate();
                myDialog.dismiss();
                //send logout event
                EventBus.getDefault().post(new LogoutEvent());//BaseActivity接收该事件

                setResult(LOGINOUT_RESULT_CODE);//返回个人中心 resultcode
                finish();
            }
        });
    }

}
