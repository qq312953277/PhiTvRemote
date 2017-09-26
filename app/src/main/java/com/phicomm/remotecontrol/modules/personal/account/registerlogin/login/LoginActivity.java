package com.phicomm.remotecontrol.modules.personal.account.registerlogin.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding.widget.RxTextView;
import com.jakewharton.rxbinding.widget.TextViewAfterTextChangeEvent;
import com.phicomm.remotecontrol.R;
import com.phicomm.remotecontrol.base.BaseActivity;
import com.phicomm.remotecontrol.base.BaseApplication;
import com.phicomm.remotecontrol.constant.PhiConstants;
import com.phicomm.remotecontrol.modules.personal.account.local.LocalDataRepository;
import com.phicomm.remotecontrol.modules.personal.account.registerlogin.forgetpassword.ForgetPasswordActivity;
import com.phicomm.remotecontrol.modules.personal.account.registerlogin.register.RegisterActivity;
import com.phicomm.remotecontrol.modules.personal.account.resultbean.AuthorizationResponseBean;
import com.phicomm.remotecontrol.modules.personal.account.resultbean.BaseResponseBean;
import com.phicomm.remotecontrol.modules.personal.account.resultbean.LoginResponseBean;
import com.phicomm.remotecontrol.modules.personal.account.token.TokenManager;
import com.phicomm.remotecontrol.modules.personal.personaldetail.PersonalActivity;
import com.phicomm.remotecontrol.util.CommonUtils;
import com.phicomm.remotecontrol.util.DialogUtils;
import com.phicomm.remotecontrol.util.MD5Utils;
import com.phicomm.remotecontrol.util.NetworkManagerUtils;
import com.phicomm.remotecontrol.util.StringUtils;

import butterknife.BindView;
import butterknife.OnClick;
import rx.functions.Action1;

import static com.phicomm.remotecontrol.constant.PhiConstants.TITLE_BAR_HEIGHT_DP;

public class LoginActivity extends BaseActivity implements LoginContract.View {


    @BindView(R.id.phoneNumber)
    EditText mPhoneNumberEdit;

    @BindView(R.id.password)
    EditText mPasswordEdit;

    @BindView(R.id.rememberme_checkbox)
    CheckBox rememberme_checkbox;

    @BindView(R.id.bt_login)
    Button bt_login;

    @BindView(R.id.rl_title)
    RelativeLayout mRlTitle;

    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @BindView(R.id.iv_back)
    ImageView mBack;

    @BindView(R.id.password_display_imageview)
    ImageView password_display_imageview;

    private boolean mIsPasswordDisplay = true;
    private String mPhoneNo = "";
    private String mPassword = "";
    private String mAuthorizationCode = "";

    public static final String PHONE_NUMBER = "phone_number";
    public static final int REQUEST_CODE_MODIFY_PASSWORD = 10;

    private LoginContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mPresenter = new LoginPresenter(this);
        initViews();
        initTitleView();
        setEditTextListener();

    }


    //注册是号码已存在，直接跳转到登录界面
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        try {
            String phone = intent.getStringExtra(PHONE_NUMBER);
            mPhoneNumberEdit.setText(phone);
            if (!StringUtils.isNull(phone)) {
                mPasswordEdit.requestFocus();
                mPasswordEdit.setText("");
            }
        } catch (Exception ex) {

        }
    }

    //忘记密码跳转回来
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((resultCode == RESULT_OK && requestCode == REQUEST_CODE_MODIFY_PASSWORD)) {
            try {
                String phone = data.getStringExtra(PHONE_NUMBER);
                mPhoneNumberEdit.setText(phone);
                if (!StringUtils.isNull(phone)) {
                    mPasswordEdit.requestFocus();
                    mPasswordEdit.setText("");
                }
            } catch (Exception ex) {

            }
        }
    }

    private void initTitleView() {
        setMarginForStatusBar(mRlTitle, TITLE_BAR_HEIGHT_DP);
        mTvTitle.setText(getString(R.string.login));
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initViews() {
        password_display_imageview.setEnabled(true);//表示ImageView可点击

        boolean isRememberMe = LocalDataRepository.getInstance(BaseApplication.getContext()).getRememberMe();
        rememberme_checkbox.setChecked(isRememberMe);
        if (isRememberMe) {
            mPhoneNumberEdit.setText(LocalDataRepository.getInstance(BaseApplication.getContext()).getUserName());
            mPasswordEdit.setText(LocalDataRepository.getInstance(BaseApplication.getContext()).getPassword());
        } else {
            mPhoneNumberEdit.setText(null);
            mPasswordEdit.setText(null);
        }
    }

    private void setEditTextListener() {
        RxTextView.afterTextChangeEvents(mPhoneNumberEdit).subscribe(new Action1<TextViewAfterTextChangeEvent>() {
            @Override
            public void call(TextViewAfterTextChangeEvent textViewAfterTextChangeEvent) {
                mPhoneNo = textViewAfterTextChangeEvent.editable().toString();
                if (!StringUtils.isNull(mPhoneNo)
                        && !StringUtils.isNull(mPassword)) {
                    bt_login.setEnabled(true);
                } else {
                    bt_login.setEnabled(false);
                }
            }
        });

        RxTextView.afterTextChangeEvents(mPasswordEdit).subscribe(new Action1<TextViewAfterTextChangeEvent>() {
            @Override
            public void call(TextViewAfterTextChangeEvent textViewAfterTextChangeEvent) {
                mPassword = textViewAfterTextChangeEvent.editable().toString();
                if (!StringUtils.isNull(mPhoneNo)
                        && !StringUtils.isNull(mPassword)) {
                    bt_login.setEnabled(true);
                } else {
                    bt_login.setEnabled(false);
                }
            }
        });
    }

    @OnClick(R.id.password_display_imageview)
    public void clickDisplayPassword() {
        if (mIsPasswordDisplay) {
            mIsPasswordDisplay = false;
            password_display_imageview.setImageResource(R.drawable.icon_eye_open_white);
            //显示密码
            mPasswordEdit.setTransformationMethod(HideReturnsTransformationMethod.getInstance());

        } else {
            mIsPasswordDisplay = true;
            password_display_imageview.setImageResource(R.drawable.icon_eye_close_white);
            //隐藏密码
            mPasswordEdit.setTransformationMethod(PasswordTransformationMethod.getInstance());

        }
        mPasswordEdit.setSelection(mPasswordEdit.getText().toString().length());
    }


    @OnClick(R.id.bt_login)
    public void login() {
        mPhoneNo = mPhoneNumberEdit.getText().toString();//这里不能省略，因为密码易加密处理
        mPassword = mPasswordEdit.getText().toString();
        if (StringUtils.isNull(mPhoneNo)) {
            CommonUtils.showToastBottom(getString(R.string.phonenum_is_null));
            return;
        }

        if (!StringUtils.checkMobile(mPhoneNo)) {
            CommonUtils.showToastBottom(getString(R.string.phonenum_is_illegal));
            return;
        }

        if (StringUtils.isNull(mPassword)) {
            CommonUtils.showToastBottom(getString(R.string.password_is_null));
            return;
        }

        if (NetworkManagerUtils.instance().networkError()) {
            CommonUtils.showToastBottom(getString(R.string.net_connect_fail));
            return;
        }

        DialogUtils.showLoadingDialog(this);

        mPassword = MD5Utils.encryptedByMD5(mPassword);//规定MD5加密
        mAuthorizationCode = LocalDataRepository.getInstance(BaseApplication.getContext()).getAuthorizationCode();
        if (StringUtils.isNull(mAuthorizationCode)) {
            mPresenter.doAuthorization(PhiConstants.CLIENT_ID, PhiConstants.RESPONSE_TYPE, PhiConstants.SCOPE, PhiConstants.CLIENT_SECRET);
        } else {
            mPresenter.doPhoneLogin(mAuthorizationCode, mPhoneNo, mPassword);
        }

    }

    @OnClick(R.id.tv_register)
    public void clickRegister() {
        CommonUtils.startIntent(this, null, RegisterActivity.class);
    }

    @OnClick(R.id.tv_forget_password)
    public void clickResetPassword() {
        Intent forgetPasswordIntent = new Intent(this, ForgetPasswordActivity.class);
        forgetPasswordIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivityForResult(forgetPasswordIntent, REQUEST_CODE_MODIFY_PASSWORD);
    }

    @Override
    public void analysisResponseBean(BaseResponseBean t) {
        if (t instanceof AuthorizationResponseBean) {
            AuthorizationResponseBean bean = (AuthorizationResponseBean) t;
            mAuthorizationCode = bean.authorizationcode;
            if (!StringUtils.isNull(bean.authorizationcode)) {
                mAuthorizationCode = bean.authorizationcode;
                LocalDataRepository.getInstance(BaseApplication.getContext()).setAuthorizationCode(mAuthorizationCode);
                mPresenter.doPhoneLogin(mAuthorizationCode, mPhoneNo, mPassword);
            } else {
                DialogUtils.cancelLoadingDialog();
                CommonUtils.showToastBottom(getString(R.string.authorization_error));
            }
        } else if (t instanceof LoginResponseBean) {
            DialogUtils.cancelLoadingDialog();
            CommonUtils.showToastBottom(getString(R.string.login_success));

            LoginResponseBean bean = (LoginResponseBean) t;
            DialogUtils.cancelLoadingDialog();


            String access_token = bean.access_token;
            //保存斐讯云access_token
            LocalDataRepository.getInstance(BaseApplication.getContext()).setAccessToken(access_token);
            LocalDataRepository.getInstance(BaseApplication.getContext()).setCloudLoginStatus(true);
            BaseApplication.getApplication().isLogined = true;//登录成功后返回personal主页，需要判断登录状态
            TokenManager.getInstance().saveTokens(bean.access_token, bean.refresh_token,
                    bean.refresh_token_expire, bean.access_token_expire);

            LocalDataRepository.getInstance(BaseApplication.getContext()).setUserName(mPhoneNo);

            if (rememberme_checkbox.isChecked()) {
                LocalDataRepository.getInstance(BaseApplication.getContext()).setPassword(mPasswordEdit.getText().toString());
                LocalDataRepository.getInstance(BaseApplication.getContext()).setRememberMe(true);
            } else {
                LocalDataRepository.getInstance(BaseApplication.getContext()).setPassword("");
                LocalDataRepository.getInstance(BaseApplication.getContext()).setRememberMe(false);
            }

            CommonUtils.startIntent(this, null, PersonalActivity.class);
            finish();

        }

    }
}
