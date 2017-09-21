package com.phicomm.remotecontrol.modules.personal.about;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.phicomm.remotecontrol.BuildConfig;
import com.phicomm.remotecontrol.R;
import com.phicomm.remotecontrol.base.BaseActivity;
import com.phicomm.widgets.alertdialog.PhiGuideDialog;

import butterknife.BindView;
import butterknife.OnClick;

import static com.phicomm.remotecontrol.constant.PhiConstants.TITLE_BAR_HEIGHT_DP;

public class AboutActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView mIvBack;

    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @BindView(R.id.about_version)
    TextView mTvVersion;

    @BindView(R.id.rl_title)
    RelativeLayout mRlTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        init();
    }

    private void init() {
        mTvTitle.setText(getString(R.string.personal_about));
        mTvVersion.setText(getString(R.string.about_project_name, BuildConfig.VERSION_NAME));
        setMarginForStatusBar(mRlTitle,TITLE_BAR_HEIGHT_DP);
    }

    @OnClick({R.id.iv_back, R.id.ll_visit_website, R.id.ll_dial_phone})
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.ll_visit_website:
                showWebsite();
                break;
            case R.id.ll_dial_phone:
                showCallDialog();
                break;
            default:
                break;
        }
    }

    private void showWebsite() {
        String website = getString(R.string.phicomm_website);
        Uri uri = Uri.parse("http://" + website);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    private void showCallDialog() {
        final PhiGuideDialog dialog = new PhiGuideDialog(this);
        dialog.setTitle(getResources().getString(R.string.phicomm_hotline));
        dialog.setLeftGuideOnclickListener(getResources().getString(R.string.cancel), R.color.syn_text_color, new PhiGuideDialog.onLeftGuideOnclickListener() {
            @Override
            public void onLeftGuideClick() {
                dialog.dismiss();
            }

        });
        dialog.setRightGuideOnclickListener(getResources().getString(R.string.dailnumber), R.color.weight_line_color, new PhiGuideDialog.onRightGuideOnclickListener() {
            @Override
            public void onRightGuideClick() {
                enterIntoDailPhone();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void enterIntoDailPhone() {
        String hotLine = getString(R.string.phicomm_hotline);
        hotLine = hotLine.replace("-", "");
        Intent phone = new Intent(Intent.ACTION_DIAL);
        phone.setData(Uri.parse("tel:" + hotLine));
        startActivity(phone);
    }
}
