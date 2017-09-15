package com.phicomm.remotecontrol.modules.personal.personaldetail;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.phicomm.remotecontrol.BuildConfig;
import com.phicomm.remotecontrol.R;
import com.phicomm.remotecontrol.base.BaseActivity;
import com.phicomm.remotecontrol.constant.PhiConstants;
import com.phicomm.remotecontrol.modules.personal.about.AboutActivity;
import com.phicomm.remotecontrol.modules.personal.setting.SettingActivity;
import com.phicomm.remotecontrol.modules.personal.upgrade.UpdateView;
import com.phicomm.remotecontrol.modules.personal.upgrade.UpdateInfoResponseBean;
import com.phicomm.remotecontrol.modules.personal.upgrade.UpdatePresenter;
import com.phicomm.remotecontrol.modules.personal.upgrade.UpdatePresenterImpl;
import com.phicomm.remotecontrol.preference.PreferenceDef;
import com.phicomm.remotecontrol.preference.PreferenceRepository;
import com.phicomm.remotecontrol.util.CommonUtils;
import com.phicomm.remotecontrol.util.DialogUtils;
import com.phicomm.remotecontrol.util.SettingUtil;
import com.phicomm.widgets.alertdialog.PhiGuideDialog;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class PersonalActivity extends BaseActivity implements UpdateView {

    @BindView(R.id.iv_header_picture)
    ImageView mHeaderPicture;

    @BindView(R.id.tv_user_name)
    TextView mUserName;

    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @BindView(R.id.tv_version)
    TextView mTvVersion;

    @BindView(R.id.new_version_indicator)
    ImageView mIvVersionIndicator;

    private UpdatePresenter mUpdatePresenter;
    private PreferenceRepository mPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);
        init();
    }

    private void init() {
        mPreference = new PreferenceRepository(this);
        boolean isHasNewVersion = (boolean) mPreference.get(PreferenceDef.APP_VERSION, PreferenceDef.IS_HAVE_NEW_VERSIOM, false);
        mTvTitle.setText(getString(R.string.personal_title));
        mTvVersion.setText(BuildConfig.VERSION_NAME);
        mUpdatePresenter = new UpdatePresenterImpl(this, this);
        if (isHasNewVersion) {
            mIvVersionIndicator.setVisibility(View.VISIBLE);
        } else {
            mIvVersionIndicator.setVisibility(View.GONE);
        }
    }



    @OnClick({R.id.rl_about, R.id.rl_appilcation, R.id.rl_setting, R.id.rl_version, R.id.iv_header_picture, R.id.iv_back})
    public void onMyClick(View view) {
        switch (view.getId()) {
            case R.id.rl_appilcation:
                break;
            case R.id.rl_setting:
                CommonUtils.startIntent(this, null, SettingActivity.class);
                break;
            case R.id.rl_about:
                CommonUtils.startIntent(this, null, AboutActivity.class);
                break;
            case R.id.rl_version:
                checkNewVersion();
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

    private void checkNewVersion() {
        showLoadingDialog(null);
        Map<String, String> options = new HashMap<>();
        options.put("appid", PhiConstants.APP_ID);
        options.put("channel", CommonUtils.getAppChannel());
        options.put("vercode", BuildConfig.VERSION_CODE + "");
        mUpdatePresenter.checkVersion(options);
    }

    private void showUpdateInfoDialog(final boolean isForceUpdate, final String versionName, String versionInfo, final String url) {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_content_for_update_info, null);
        TextView title = (TextView) view.findViewById(R.id.update_title);
        TextView message = (TextView) view.findViewById(R.id.update_message);
        title.setText(String.format(getString(R.string.version_update_title), versionName));
        message.setText(versionInfo);
        final PhiGuideDialog dialog = new PhiGuideDialog(this);
        dialog.setContentPanel(view);
        dialog.setLeftGuideOnclickListener(getResources().getString(R.string.update_later), R.color.syn_text_color, new PhiGuideDialog.onLeftGuideOnclickListener() {
            @Override
            public void onLeftGuideClick() {
                if (!isForceUpdate) {
                    dialog.dismiss();
                }
            }

        });
        dialog.setRightGuideOnclickListener(getResources().getString(R.string.update_now), R.color.weight_line_color, new PhiGuideDialog.onRightGuideOnclickListener() {
            @Override
            public void onRightGuideClick() {
                if (!isForceUpdate) {
                    dialog.dismiss();
                }
                mUpdatePresenter.downloadFile(url, versionName);
            }
        });
        if (isForceUpdate) {
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
        }
        dialog.show();
    }

    @Override
    public void checkVersion(UpdateInfoResponseBean bean) {
        if (bean == null) {
            return;
        }
        String ret = bean.getRet();
        String updateType = bean.getVerType();
        boolean isForceUpdate = false;
        if (!TextUtils.isEmpty(updateType) && updateType.equals("1")) {
            isForceUpdate = true;
        }

        if (!TextUtils.isEmpty(ret)) {
            if (ret.equals("0")) {
                showUpdateInfoDialog(isForceUpdate, bean.getVerName(), bean.getVerInfos(), bean.getVerDown());
            } else {
                CommonUtils.showShortToast(getString(R.string.current_version_newest));
            }
        }

    }

    @Override
    public void showMessage(Object message) {
        CommonUtils.showShortToast((String) message);
    }

    @Override
    public void onSuccess(Object message) {
        DialogUtils.cancelLoadingDialog();
    }

    @Override
    public void onFailure(Object message) {

    }
}
