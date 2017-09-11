package com.phicomm.remotecontrol.modules.personal;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.phicomm.remotecontrol.R;
import com.phicomm.remotecontrol.base.BaseActivity;
import com.phicomm.remotecontrol.modules.personal.about.AboutActivity;
import com.phicomm.remotecontrol.util.CommonUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class PersonalActivity extends BaseActivity {

    @BindView(R.id.iv_header_picture)
    ImageView mHeaderPicture;

    @BindView(R.id.tv_user_name)
    TextView mUserName;

    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.ll_parent)
    LinearLayout llParent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);
        init();
    }

    private void init() {
        mTvTitle.setText(getString(R.string.personal_title));
    }

    @OnClick({R.id.rl_about, R.id.rl_appilcation, R.id.rl_setting, R.id.rl_version, R.id.iv_header_picture, R.id.iv_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_appilcation:
                break;
            case R.id.rl_setting:
                break;
            case R.id.rl_about:
                CommonUtils.startIntent(PersonalActivity.this, null, AboutActivity.class);
                break;
            case R.id.rl_version:
                break;
            case R.id.iv_header_picture:
                break;
            case R.id.iv_back:
                finish();
                break;
            default:
                break;
        }
    }
}
