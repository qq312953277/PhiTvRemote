package com.phicomm.remotecontrol.modules.personal.account.registerlogin.login;

import android.content.Intent;
import android.os.Bundle;
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
import com.phicomm.widgets.alertdialog.PhiGuideDialog;

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
    }

    private void initViews() {
        mTvTitle.setText(getString(R.string.exit));
        setMarginForStatusBar(mRlTitle, TITLE_BAR_HEIGHT_DP);
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
            mUserName.setText("斐讯账户" + userName);
        }
        if (!TextUtils.isEmpty(img)) {
            Glide.with(BaseApplication.getContext()).load(img).dontAnimate()
                    .error(R.drawable.default_avatar).placeholder(R.drawable.icon_photo_loading).priority(Priority.HIGH).into(mHeaderPicture);
        } else {
            mHeaderPicture.setImageResource(R.drawable.default_avatar);
        }

    }

    @OnClick(R.id.bt_loginout)
    public void clickLogout() {
        final PhiGuideDialog deleteDialog = new PhiGuideDialog(this);
        deleteDialog.setTitle(getResources().getString(R.string.account_exit));
        deleteDialog.setMessage(getResources().getString(R.string.account_exit_msg));
        deleteDialog.setLeftGuideOnclickListener(getResources().getString(R.string.cancel), R.color.weight_line_color, new PhiGuideDialog.onLeftGuideOnclickListener() {
            @Override
            public void onLeftGuideClick() {
                deleteDialog.dismiss();
            }

        });
        deleteDialog.setRightGuideOnclickListener(getResources().getString(R.string.ok), R.color.syn_text_color, new PhiGuideDialog.onRightGuideOnclickListener() {
            @Override
            public void onRightGuideClick() {
                deleteDialog.dismiss();
                //send logout event
                EventBus.getDefault().post(new LogoutEvent());//BaseActivity接收该事件

                setResult(LOGINOUT_RESULT_CODE);//返回个人中心 resultcode
                finish();
            }
        });
        deleteDialog.show();
    }

}
