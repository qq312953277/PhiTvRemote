package com.phicomm.remotecontrol.modules.personal.account.registerlogin.register;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.jakewharton.rxbinding.widget.TextViewAfterTextChangeEvent;
import com.phicomm.remotecontrol.R;
import com.phicomm.remotecontrol.base.BaseActivity;
import com.phicomm.remotecontrol.base.BaseApplication;
import com.phicomm.remotecontrol.constant.PhiConstants;
import com.phicomm.remotecontrol.modules.personal.account.http.HttpErrorCode;
import com.phicomm.remotecontrol.modules.personal.account.local.LocalDataRepository;
import com.phicomm.remotecontrol.modules.personal.account.registerlogin.login.LoginActivity;
import com.phicomm.remotecontrol.modules.personal.account.resultbean.AuthorizationResponseBean;
import com.phicomm.remotecontrol.modules.personal.account.resultbean.BaseResponseBean;
import com.phicomm.remotecontrol.modules.personal.account.resultbean.CaptchaResponseBean;
import com.phicomm.remotecontrol.modules.personal.account.resultbean.CheckphonenumberResponseBean;
import com.phicomm.remotecontrol.modules.personal.account.resultbean.LoginResponseBean;
import com.phicomm.remotecontrol.modules.personal.account.resultbean.RegisterResponseBean;
import com.phicomm.remotecontrol.modules.personal.account.resultbean.VerifycodeResponseBean;
import com.phicomm.remotecontrol.modules.personal.account.token.TokenManager;
import com.phicomm.remotecontrol.modules.personal.personaldetail.PersonalActivity;
import com.phicomm.remotecontrol.util.CommonUtils;
import com.phicomm.remotecontrol.util.DialogUtils;
import com.phicomm.remotecontrol.util.MD5Utils;
import com.phicomm.remotecontrol.util.NetworkManagerUtils;
import com.phicomm.remotecontrol.util.StringUtils;
import com.phicomm.widgets.alertdialog.PhiGuideDialog;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by yong04.zhou on 2017/9/14.
 */

public class RegisterActivity extends BaseActivity implements RegisterContract.View {

    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @BindView(R.id.iv_back)
    ImageView mBack;

    @BindView(R.id.phonenumber)
    EditText mPhoneNumberEdit;

    @BindView(R.id.code)
    EditText mCodeEdit;

    @BindView(R.id.captcha)
    EditText mCaptchaEdit;

    @BindView(R.id.captcha_code)
    ImageView mCaptchaImage;

    @BindView(R.id.btn_code)
    Button mGetCodeBtn;

    @BindView(R.id.password)
    EditText mPasswordEdit;

    private boolean isPasswordDisplay = true;
    @BindView(R.id.password_display_imageview)
    ImageView password_display_imageview;

    @BindView(R.id.password_strength)
    ImageView password_strength;

    @BindView(R.id.bt_register)
    Button mRegister;

    @BindView(R.id.protocal_checkbox)
    CheckBox mProtocalCheckbox;

    private boolean isProtocalChecked = false;
    private String mPhoneNo = "";
    private String mVerifyCode = "";
    private String mCaptchaCode = "";
    private String mCaptchaId = "";
    private String mPassword = "";
    private String mAuthorizationCode = "";
    private boolean mTimerRunning = false;

    private static final int INPUT_TEXT_SIZE = 14;
    private static final int INPUT_PASSWORD_HINT_SIZE = 10;

    private RegisterContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mPresenter = new RegisterPresenter(this);

        initTitleView();
        initViews();
        setEditTextListener();
        getCaptchaCode();

    }

    private void initTitleView() {
        mTvTitle.setText(getString(R.string.new_user_register));
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void initViews() {
        password_display_imageview.setEnabled(true);

        //防抖
        RxView.clicks(mGetCodeBtn)
                .throttleFirst(3, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        getVerifyCode();
                    }
                });
    }

    private void setEditTextListener() {
        RxTextView.afterTextChangeEvents(mPhoneNumberEdit).subscribe(new Action1<TextViewAfterTextChangeEvent>() {
            @Override
            public void call(TextViewAfterTextChangeEvent textViewAfterTextChangeEvent) {
                mPhoneNo = textViewAfterTextChangeEvent.editable().toString();
                if (!StringUtils.isNull(mPhoneNo)
                        && !StringUtils.isNull(mCaptchaCode)
                        && !mTimerRunning) {
                    mGetCodeBtn.setEnabled(true);
                } else {
                    mGetCodeBtn.setEnabled(false);
                }

                if (!StringUtils.isNull(mPhoneNo)
                        && !StringUtils.isNull(mCaptchaCode)
                        && !StringUtils.isNull(mVerifyCode)
                        && !StringUtils.isNull(mPassword)
                        && isProtocalChecked) {
                    mRegister.setEnabled(true);
                } else {
                    mRegister.setEnabled(false);
                }
            }
        });

        RxTextView.afterTextChangeEvents(mCaptchaEdit).subscribe(new Action1<TextViewAfterTextChangeEvent>() {
            @Override
            public void call(TextViewAfterTextChangeEvent textViewAfterTextChangeEvent) {
                mCaptchaCode = textViewAfterTextChangeEvent.editable().toString();
                if (!StringUtils.isNull(mPhoneNo)
                        && !StringUtils.isNull(mCaptchaCode)
                        && !mTimerRunning) {
                    mGetCodeBtn.setEnabled(true);
                } else {
                    mGetCodeBtn.setEnabled(false);
                }

                if (!StringUtils.isNull(mPhoneNo)
                        && !StringUtils.isNull(mCaptchaCode)
                        && !StringUtils.isNull(mVerifyCode)
                        && !StringUtils.isNull(mPassword)
                        && isProtocalChecked) {
                    mRegister.setEnabled(true);
                } else {
                    mRegister.setEnabled(false);
                }
            }
        });

        RxTextView.afterTextChangeEvents(mCodeEdit).subscribe(new Action1<TextViewAfterTextChangeEvent>() {
            @Override
            public void call(TextViewAfterTextChangeEvent textViewAfterTextChangeEvent) {
                mVerifyCode = textViewAfterTextChangeEvent.editable().toString();
                if (!StringUtils.isNull(mPhoneNo)
                        && !StringUtils.isNull(mCaptchaCode)
                        && !StringUtils.isNull(mVerifyCode)
                        && !StringUtils.isNull(mPassword)
                        && isProtocalChecked) {
                    mRegister.setEnabled(true);
                } else {
                    mRegister.setEnabled(false);
                }
            }
        });

        RxTextView.afterTextChangeEvents(mPasswordEdit).subscribe(new Action1<TextViewAfterTextChangeEvent>() {
            @Override
            public void call(TextViewAfterTextChangeEvent textViewAfterTextChangeEvent) {
                mPassword = textViewAfterTextChangeEvent.editable().toString();

                if (!StringUtils.isNull(mPassword)) {
                    mPasswordEdit.setTextSize(INPUT_TEXT_SIZE);
                } else {
                    mPasswordEdit.setTextSize(INPUT_PASSWORD_HINT_SIZE);
                }

                if (!StringUtils.isNull(mPhoneNo)
                        && !StringUtils.isNull(mCaptchaCode)
                        && !StringUtils.isNull(mVerifyCode)
                        && !StringUtils.isNull(mPassword)
                        && isProtocalChecked) {
                    mRegister.setEnabled(true);
                } else {
                    mRegister.setEnabled(false);
                }

                checkPasswordStrength(mPassword);
            }
        });
    }

    private void checkPasswordStrength(String password) {
        int strength = CommonUtils.getPasswordStrength(password);
        password_strength.setVisibility(View.VISIBLE);
        if (strength == 1) {
            password_strength.setImageResource(R.drawable.password_weak);
        } else if (strength == 2) {
            password_strength.setImageResource(R.drawable.password_middle);
        } else if (strength == 3) {
            password_strength.setImageResource(R.drawable.password_strong);
        } else {
            password_strength.setVisibility(View.GONE);
        }
    }


    @OnClick(R.id.captcha_code)
    public void getCaptchaCode() {
        if (NetworkManagerUtils.instance().networkError()) {
            return;
        }
        mAuthorizationCode = LocalDataRepository.getInstance(BaseApplication.getContext()).getAuthorizationCode();
        if (StringUtils.isNull(mAuthorizationCode)) {
            mPresenter.doAuthorization(PhiConstants.CLIENT_ID, PhiConstants.RESPONSE_TYPE, PhiConstants.SCOPE, PhiConstants.CLIENT_SECRET);
        } else {
            mCaptchaImage.setImageResource(R.drawable.captcha_loading);
            mPresenter.doGetCaptchaImageCode(mAuthorizationCode);
        }
    }

    /**
     * 获取验证码
     */
    public void getVerifyCode() {
        if (!StringUtils.checkMobile(mPhoneNumberEdit.getText().toString())) {
            CommonUtils.showToastBottom(getString(R.string.phonenum_is_illegal));
            return;
        }

        mCaptchaCode = mCaptchaEdit.getText().toString();
        if (StringUtils.isNull(mCaptchaCode)) {
            CommonUtils.showToastBottom(getString(R.string.input_captcha_code));
            return;
        }

        if (NetworkManagerUtils.instance().networkError()) {
            return;
        }

        mGetCodeBtn.setEnabled(false);
        DialogUtils.showLoadingDialog(this);
        mAuthorizationCode = LocalDataRepository.getInstance(BaseApplication.getContext()).getAuthorizationCode();

        if (StringUtils.isNull(mAuthorizationCode)) {
            mPresenter.doAuthorization(PhiConstants.CLIENT_ID, PhiConstants.RESPONSE_TYPE, PhiConstants.SCOPE, PhiConstants.CLIENT_SECRET);
        } else {
            mPresenter.doCheckAccountRegistered(mAuthorizationCode, mPhoneNumberEdit.getText().toString());
        }
    }

    @OnCheckedChanged(R.id.protocal_checkbox)
    public void protocalChecked() {
        if (mProtocalCheckbox.isChecked()) {
            isProtocalChecked = true;
        } else {
            isProtocalChecked = false;
        }

        if (!StringUtils.isNull(mPhoneNo)
                && !StringUtils.isNull(mCaptchaCode)
                && !StringUtils.isNull(mVerifyCode)
                && !StringUtils.isNull(mPassword)
                && isProtocalChecked) {
            mRegister.setEnabled(true);
        } else {
            mRegister.setEnabled(false);
        }
    }

    @OnClick(R.id.protocal_textview)
    public void clickProtocal() {

    }

    /**
     * 斐讯云注册
     */
    @OnClick(R.id.bt_register)
    public void register() {
        mPhoneNo = mPhoneNumberEdit.getText().toString();
        mVerifyCode = mCodeEdit.getText().toString();
        mPassword = mPasswordEdit.getText().toString();
        mCaptchaCode = mCaptchaEdit.getText().toString();

        if (StringUtils.isNull(mPhoneNo)) {
            CommonUtils.showToastBottom(getString(R.string.phonenum_is_null));
            return;
        }
        if (!StringUtils.checkMobile(mPhoneNo)) {
            CommonUtils.showToastBottom(getString(R.string.phonenum_is_illegal));
            return;
        }

        if (StringUtils.isNull(mVerifyCode)) {
            CommonUtils.showToastBottom(getString(R.string.input_verify_code));
            return;
        }

        if (StringUtils.isNull(mCaptchaCode)) {
            CommonUtils.showToastBottom(getString(R.string.input_captcha_code));
            return;
        }

        if (StringUtils.isNull(mPassword)) {
            CommonUtils.showToastBottom(getString(R.string.password_is_null));
            return;
        }

        if (mPassword.length() < 6) {
            CommonUtils.showToastBottom(getString(R.string.password_length_wrong));
            return;
        }

        if (!StringUtils.isPassword(mPassword)) {
            CommonUtils.showToastBottom(getString(R.string.password_format_wrong));
            return;
        }

        if (!mProtocalCheckbox.isChecked()) {
            CommonUtils.showToastBottom(getString(R.string.not_read_protocal));
            return;
        }

        if (NetworkManagerUtils.instance().networkError()) {
            return;
        }
        DialogUtils.showLoadingDialog(this);
        mAuthorizationCode = LocalDataRepository.getInstance(BaseApplication.getContext()).getAuthorizationCode();
        mPassword = MD5Utils.encryptedByMD5(mPassword);
        if (StringUtils.isNull(mAuthorizationCode)) {
            mPresenter.doAuthorization(PhiConstants.CLIENT_ID, PhiConstants.RESPONSE_TYPE, PhiConstants.SCOPE, PhiConstants.CLIENT_SECRET);
        } else {
            mPresenter.doRegister(mAuthorizationCode, mPhoneNo, mPassword, PhiConstants.REQUEST_SOURCE, mVerifyCode);
        }
    }

    private void enterIntoLoginDialog() {
        final PhiGuideDialog deleteDialog = new PhiGuideDialog(this);
        deleteDialog.setTitle(getResources().getString(R.string.login_tips));
        deleteDialog.setLeftGuideOnclickListener(getResources().getString(R.string.cancel), R.color.syn_text_color, new PhiGuideDialog.onLeftGuideOnclickListener() {
            @Override
            public void onLeftGuideClick() {
                deleteDialog.dismiss();

                //reenable getCodeBtn
                if (!StringUtils.isNull(mPhoneNo)
                        && !StringUtils.isNull(mCaptchaCode)
                        && !mTimerRunning) {
                    mGetCodeBtn.setEnabled(true);
                } else {
                    mGetCodeBtn.setEnabled(false);
                }
            }

        });
        deleteDialog.setRightGuideOnclickListener(getResources().getString(R.string.please_login), R.color.weight_line_color, new PhiGuideDialog.onRightGuideOnclickListener() {
            @Override
            public void onRightGuideClick() {
                deleteDialog.dismiss();

                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                intent.putExtra(LoginActivity.PHONE_NUMBER, mPhoneNumberEdit.getText().toString());
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
        });
        deleteDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                //reenable getCodeBtn
                if (!StringUtils.isNull(mPhoneNo)
                        && !StringUtils.isNull(mCaptchaCode)
                        && !mTimerRunning) {
                    mGetCodeBtn.setEnabled(true);
                } else {
                    mGetCodeBtn.setEnabled(false);
                }
            }
        });
        deleteDialog.show();
    }


    @Override
    public void startVerfyCodeTimerTask() {
        final long count = 60;
        Observable.interval(0, 1, TimeUnit.SECONDS)
                .take(61)//计时次数
                .map(new Func1<Long, Long>() {
                    @Override
                    public Long call(Long integer) {
                        return count - integer;
                    }
                }).subscribeOn(Schedulers.io())//主要改变的是订阅的线程，即call()执行的线程;
                .observeOn(AndroidSchedulers.mainThread())//改变的是发送的线程，即onNext()执行的线程
                .subscribe(new Subscriber<Long>() {

                    @Override
                    public void onStart() {
                        super.onStart();
                        mGetCodeBtn.setEnabled(false);
                        String showText = String.format("%s s", count);
                        mGetCodeBtn.setText(showText + "重发");
                        mTimerRunning = true;
                    }

                    @Override
                    public void onCompleted() {
                        mGetCodeBtn.setEnabled(true);
                        mGetCodeBtn.setText("重新发送");
                        mTimerRunning = false;
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Long aLong) {
                        String showText = String.format("%s s", aLong);
                        mGetCodeBtn.setText(showText + "重发");
                    }
                });

    }

    @Override
    public void analysisResponseBean(BaseResponseBean t) {
        if (t instanceof AuthorizationResponseBean) {
            AuthorizationResponseBean bean = (AuthorizationResponseBean) t;
            if (!StringUtils.isNull(bean.authorizationcode) && bean.error.equals(HttpErrorCode.SUCCESS)) {
                mAuthorizationCode = bean.authorizationcode;
                LocalDataRepository.getInstance(BaseApplication.getContext()).setAuthorizationCode(mAuthorizationCode);
                mCaptchaImage.setImageResource(R.drawable.captcha_loading);
                mPresenter.doGetCaptchaImageCode(mAuthorizationCode);
            } else {
                DialogUtils.cancelLoadingDialog();
                CommonUtils.showToastBottom(getString(R.string.authorization_error));
            }
        } else if (t instanceof CaptchaResponseBean) {
            CaptchaResponseBean bean = (CaptchaResponseBean) t;
            if (bean.error.equals(HttpErrorCode.SUCCESS)) {
                mCaptchaId = bean.captchaid;
                Bitmap bitmap = CommonUtils.base64ToBitmap((String) bean.captcha);
                if (bitmap != null) {
                    mCaptchaImage.setImageBitmap(bitmap);
                } else {
                    mCaptchaImage.setImageResource(R.drawable.captcha_no_internet);
                }
            } else {
                //handler abnormal resonose
                int errorCode = Integer.parseInt(bean.error);
                int errorStringRes = HttpErrorCode.getErrorStringByErrorCode(errorCode);
                CommonUtils.showToastBottom(getString(errorStringRes));

                mCaptchaImage.setImageResource(R.drawable.captcha_no_internet);
            }
        } else if (t instanceof CheckphonenumberResponseBean) {
            CheckphonenumberResponseBean bean = (CheckphonenumberResponseBean) t;
            if (bean.error.equals(HttpErrorCode.SUCCESS)) {
                mAuthorizationCode = LocalDataRepository.getInstance(BaseApplication.getContext()).getAuthorizationCode();
                mPresenter.doGetVerifyCode(mAuthorizationCode, mPhoneNumberEdit.getText().toString(), PhiConstants.SMS_VERIFICATION,
                        mCaptchaCode, mCaptchaId);
            } else {
                DialogUtils.cancelLoadingDialog();
                int errorCode = Integer.parseInt(bean.error);

                if (errorCode == 14) {
                    enterIntoLoginDialog();
                } else {
                    int errorStringRes = HttpErrorCode.getErrorStringByErrorCode(errorCode);
                    CommonUtils.showToastBottom(getString(errorStringRes));

                }
            }
        } else if (t instanceof VerifycodeResponseBean) {
            DialogUtils.cancelLoadingDialog();
            VerifycodeResponseBean bean = (VerifycodeResponseBean) t;
            if (bean.error.equals(HttpErrorCode.SUCCESS)) {
                CommonUtils.showToastBottom(getString(R.string.send_verifycode_success));
                startVerfyCodeTimerTask();
            } else {
                //handler captcha error
                int errorCode = Integer.parseInt(bean.error);
                int errorStringRes = HttpErrorCode.getErrorStringByErrorCode(errorCode);
                CommonUtils.showToastBottom(getString(errorStringRes));
                mGetCodeBtn.setEnabled(true);

                if (errorStringRes == R.string.get_verifycode_failed
                        || errorStringRes == R.string.captcha_error
                        || errorStringRes == R.string.captcha_expire
                        || errorStringRes == R.string.get_verifycode_too_fast
                        || errorStringRes == R.string.get_verifycode_count_expire
                        ) {
                    //refresh captcha code
                    getCaptchaCode();
                    mCaptchaEdit.setText("");
                }
            }
        } else if (t instanceof RegisterResponseBean) {
            RegisterResponseBean bean = (RegisterResponseBean) t;
            if (bean.error.equals(HttpErrorCode.SUCCESS)) {
                mAuthorizationCode = LocalDataRepository.getInstance(BaseApplication.getContext()).getAuthorizationCode();
                mPresenter.doPhoneLogin(mAuthorizationCode, mPhoneNo, mPassword);//注册成功直接登录
            } else {
                DialogUtils.cancelLoadingDialog();

                int errorCode = Integer.parseInt(bean.error);

                if (errorCode == 14) {
                    enterIntoLoginDialog();
                } else {
                    int errorStringRes = HttpErrorCode.getErrorStringByErrorCode(errorCode);
                    CommonUtils.showToastBottom(getString(errorStringRes));
                }
            }
        } else if (t instanceof LoginResponseBean) {
            DialogUtils.cancelLoadingDialog();
            CommonUtils.showToastBottom(getString(R.string.login_success));

            LoginResponseBean bean = (LoginResponseBean) t;


            String access_token = bean.access_token;
            //保存斐讯云access_token
            LocalDataRepository.getInstance(BaseApplication.getContext()).setAccessToken(access_token);
            BaseApplication.getApplication().isLogined = true;
            LocalDataRepository.getInstance(BaseApplication.getContext()).setCloudLoginStatus(true);
            TokenManager.getInstance().saveTokens(bean.access_token, bean.refresh_token,
                    bean.refresh_token_expire, bean.access_token_expire);

            //保存手机号
            LocalDataRepository.getInstance(BaseApplication.getContext()).setUserName(mPhoneNo);

            //donot remember me when loging after register
            LocalDataRepository.getInstance(BaseApplication.getContext()).setPassword("");
            LocalDataRepository.getInstance(BaseApplication.getContext()).setRememberMe(false);

            //跳转到用户界面
            CommonUtils.startIntent(this, null, PersonalActivity.class);

        }
    }

}
