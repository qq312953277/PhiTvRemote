package com.phicomm.remotecontrol.modules.personal.personaldetail;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.phicomm.remotecontrol.BuildConfig;
import com.phicomm.remotecontrol.R;
import com.phicomm.remotecontrol.base.BaseActivity;
import com.phicomm.remotecontrol.base.BaseApplication;
import com.phicomm.remotecontrol.constant.PhiConstants;
import com.phicomm.remotecontrol.modules.personal.about.AboutActivity;
import com.phicomm.remotecontrol.modules.personal.account.event.OffLineEvent;
import com.phicomm.remotecontrol.modules.personal.account.http.HttpErrorCode;
import com.phicomm.remotecontrol.modules.personal.account.registerlogin.login.LoginActivity;
import com.phicomm.remotecontrol.modules.personal.account.registerlogin.login.LoginoutActivity;
import com.phicomm.remotecontrol.modules.personal.account.resultbean.AccountDetailBean;
import com.phicomm.remotecontrol.modules.personal.account.resultbean.BaseResponseBean;
import com.phicomm.remotecontrol.modules.personal.apply.ApplyActivity;
import com.phicomm.remotecontrol.modules.personal.setting.SettingActivity;
import com.phicomm.remotecontrol.modules.personal.upgrade.UpdateInfoResponseBean;
import com.phicomm.remotecontrol.modules.personal.upgrade.UpdatePresenter;
import com.phicomm.remotecontrol.modules.personal.upgrade.UpdatePresenterImpl;
import com.phicomm.remotecontrol.modules.personal.upgrade.UpdateView;
import com.phicomm.remotecontrol.preference.PreferenceDef;
import com.phicomm.remotecontrol.preference.PreferenceRepository;
import com.phicomm.remotecontrol.util.CommonUtils;
import com.phicomm.remotecontrol.util.DialogUtils;
import com.phicomm.remotecontrol.util.ScreenUtils;
import com.phicomm.widgets.alertdialog.PhiGuideDialog;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

import static com.phicomm.remotecontrol.constant.PhiConstants.TITLE_BAR_HEIGHT_DP;

public class PersonalActivity extends BaseActivity implements UpdateView, PersonalContract.View {

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

    @BindView(R.id.rl_title)
    RelativeLayout mRlTitle;

    private UpdatePresenter mUpdatePresenter;
    private PreferenceRepository mPreference;

    private PersonalContract.Presenter myPresenter;
    private PersonalInforManager personalInforManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);
        init();
        myPresenter = new PersonalPresenter(this);
    }

    private void init() {
        setMarginForStatusBar(mRlTitle, TITLE_BAR_HEIGHT_DP);
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

    @Override
    protected void onResume() {
        super.onResume();
        if (BaseApplication.getApplication().isLogined) {
            myPresenter.getPersonInfoFromServer();
            refreshDataInUI();
        } else {
            mHeaderPicture.setImageResource(R.drawable.default_avatar);
            mUserName.setText("请点击登录您的斐讯账号");
        }
    }

    /**
     * OffLineEvent EventBus
     *
     * @param msg
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventOffLineEvent(OffLineEvent msg) {  //用户下线处理
        mHeaderPicture.setImageResource(R.drawable.default_avatar);
        mUserName.setText("请点击登录您的斐讯账号");
    }

    @Override
    @OnClick({R.id.rl_about, R.id.rl_appilcation, R.id.rl_setting, R.id.rl_version, R.id.iv_header_picture, R.id.iv_back})
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.rl_appilcation:
                CommonUtils.startIntent(this, null, ApplyActivity.class);
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
                toLoginOrloginoutActivity();
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

    private void toLoginOrloginoutActivity() {
        if (BaseApplication.getApplication().isLogined) {
            personalInforManager = PersonalInforManager.getInstance();
            AccountDetailBean mAccount = personalInforManager.getAccountDetailBean();
            Intent intent = new Intent(this, LoginoutActivity.class);
            intent.putExtra("img", mAccount.data.img);
            intent.putExtra("userName", mAccount.data.phonenumber);
            startActivity(intent);
        } else {
            CommonUtils.startIntent(this, null, LoginActivity.class);//没有finish自己
        }

    }

    @Override
    public void analysisResponseBean(BaseResponseBean t) {
        if (t instanceof AccountDetailBean) {
            AccountDetailBean bean = (AccountDetailBean) t;
            if (bean.error.equals(HttpErrorCode.SUCCESS)) {
                personalInforManager = PersonalInforManager.getInstance();
                personalInforManager.setAccountAndSave(bean);
                refreshDataInUI();//登录成功跳过来时刷新，不能少
            }
        }
    }

    @Override
    public void refreshDataInUI() {
        personalInforManager = PersonalInforManager.getInstance();
        AccountDetailBean mAccount = personalInforManager.getAccountDetailBean();
        if (mAccount == null) {
            return;
        }
        //头像
        String img = mAccount.data.img;
        if (!TextUtils.isEmpty(img)) {
            myPresenter.setImageAvatarByUrl(img, mHeaderPicture);
        } else {
            mHeaderPicture.setImageResource(R.drawable.default_avatar);
        }
        //用户名
        String userName = mAccount.data.phonenumber;
        if (!TextUtils.isEmpty(userName)) {
            mUserName.setText("斐讯账户" + userName);
        } else {
            mUserName.setText("斐讯账户" + mAccount.data.uid);
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
