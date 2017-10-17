package com.phicomm.remotecontrol.modules.personal.account.registerlogin.forgetpassword;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import com.phicomm.remotecontrol.modules.personal.account.resultbean.ForgetpasswordResponseBean;
import com.phicomm.remotecontrol.modules.personal.account.resultbean.VerifycodeResponseBean;
import com.phicomm.remotecontrol.util.CommonUtils;
import com.phicomm.remotecontrol.util.DialogUtils;
import com.phicomm.remotecontrol.util.MD5Utils;
import com.phicomm.remotecontrol.util.NetworkManagerUtils;
import com.phicomm.remotecontrol.util.StringUtils;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static com.phicomm.remotecontrol.constant.PhiConstants.TITLE_BAR_HEIGHT_DP;

public class ForgetPasswordActivity extends BaseActivity implements ForgetPasswordContract.View {

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

    @BindView(R.id.password_display_imageview)
    ImageView password_display_imageview;

    @BindView(R.id.password_strength)
    ImageView password_strength;

    @BindView(R.id.bt_submit)
    Button mSubmitBtn;

    @BindView(R.id.rl_title)
    RelativeLayout mRlTitle;

    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @BindView(R.id.iv_back)
    ImageView mBack;

    private boolean mIsPasswordDisplay = true;
    private String mPhoneNo = "";
    private String mVerifyCode = "";
    private String mCaptchaCode = "";
    private String mCaptchaId = "";
    private String mPassword = "";
    private String mAuthorizationCode = "";
    private boolean mTimerRunning = false;

    private static final int INPUT_TEXT_SIZE = 14;
    private static final int INPUT_PASSWORD_HINT_SIZE = 10;

    private ForgetPasswordContract.Presenter mResetPasswordPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        mResetPasswordPresenter = new ForgetPasswordPresenter(this);

        initTitleView();
        initViews();
        setEditTextListener();
        getCaptchaCode();
    }

    private void initTitleView() {
        setMarginForStatusBar(mRlTitle, TITLE_BAR_HEIGHT_DP);
        mTvTitle.setText(getString(R.string.forget_password));
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
                        && !StringUtils.isNull(mPassword)) {
                    mSubmitBtn.setEnabled(true);
                } else {
                    mSubmitBtn.setEnabled(false);
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
                        && !StringUtils.isNull(mPassword)) {
                    mSubmitBtn.setEnabled(true);
                } else {
                    mSubmitBtn.setEnabled(false);
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
                        && !StringUtils.isNull(mPassword)) {
                    mSubmitBtn.setEnabled(true);
                } else {
                    mSubmitBtn.setEnabled(false);
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
                        && !StringUtils.isNull(mPassword)) {
                    mSubmitBtn.setEnabled(true);
                } else {
                    mSubmitBtn.setEnabled(false);
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
            mResetPasswordPresenter.doAuthorization(PhiConstants.CLIENT_ID, PhiConstants.RESPONSE_TYPE, PhiConstants.SCOPE, PhiConstants.CLIENT_SECRET);
        } else {
            mCaptchaImage.setImageResource(R.drawable.captcha_loading);
            mResetPasswordPresenter.doGetCaptchaImageCode(mAuthorizationCode);
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
            CommonUtils.showToastBottom(getString(R.string.net_connect_fail));
            return;
        }

        mGetCodeBtn.setEnabled(false);
        DialogUtils.showLoadingDialog(this);
        mAuthorizationCode = LocalDataRepository.getInstance(BaseApplication.getContext()).getAuthorizationCode();

        if (StringUtils.isNull(mAuthorizationCode)) {
            mResetPasswordPresenter.doAuthorization(PhiConstants.CLIENT_ID, PhiConstants.RESPONSE_TYPE, PhiConstants.SCOPE, PhiConstants.CLIENT_SECRET);
        } else {
            mResetPasswordPresenter.doCheckAccountRegistered(mAuthorizationCode, mPhoneNumberEdit.getText().toString());
        }
    }

    @OnClick(R.id.bt_submit)
    public void submit() {
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
            CommonUtils.showToastBottom(getString(R.string.input_captcha_code));
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

        if (NetworkManagerUtils.instance().networkError()) {
            CommonUtils.showToastBottom(getString(R.string.net_connect_fail));
            return;
        }
        DialogUtils.showLoadingDialog(this);

        mAuthorizationCode = LocalDataRepository.getInstance(BaseApplication.getContext()).getAuthorizationCode();
        mPassword = MD5Utils.encryptedByMD5(mPassword);
        if (StringUtils.isNull(mAuthorizationCode)) {
            mResetPasswordPresenter.doAuthorization(PhiConstants.CLIENT_ID, PhiConstants.RESPONSE_TYPE, PhiConstants.SCOPE, PhiConstants.CLIENT_SECRET);
        } else {
            mResetPasswordPresenter.doResetPassword(mAuthorizationCode, mPhoneNo, mPassword, mVerifyCode);
        }
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
                        mGetCodeBtn.setText(showText + getString(R.string.verifycode_resend));
                        mTimerRunning = true;
                    }

                    @Override
                    public void onCompleted() {
                        mGetCodeBtn.setEnabled(true);
                        mGetCodeBtn.setText(R.string.send_again);
                        mTimerRunning = false;
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Long aLong) {
                        String showText = String.format("%s s", aLong);
                        mGetCodeBtn.setText(showText + getString(R.string.verifycode_resend));
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
                mResetPasswordPresenter.doGetCaptchaImageCode(mAuthorizationCode);
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
            if (bean.error.equals("14")) {//has been register
                mAuthorizationCode = LocalDataRepository.getInstance(BaseApplication.getContext()).getAuthorizationCode();
                mResetPasswordPresenter.doGetVerifyCode(mAuthorizationCode, mPhoneNumberEdit.getText().toString(), PhiConstants.SMS_VERIFICATION,
                        mCaptchaCode, mCaptchaId);
            } else {
                DialogUtils.cancelLoadingDialog();
                int errorCode = Integer.parseInt(bean.error);
                int errorStringRes = R.string.account_not_exist;
                CommonUtils.showToastBottom(getString(errorStringRes));
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
        } else if (t instanceof ForgetpasswordResponseBean) {
            DialogUtils.cancelLoadingDialog();

            ForgetpasswordResponseBean bean = (ForgetpasswordResponseBean) t;
            if (bean.error.equals(HttpErrorCode.SUCCESS)) {
                CommonUtils.showToastBottom(getString(R.string.reset_success));

                Intent intent = getIntent();//给登录界面返回参数
                intent.putExtra(LoginActivity.PHONE_NUMBER, mPhoneNumberEdit.getText().toString());
                setResult(RESULT_OK, intent);//向LoginActivity返回responseCode
                finish();
            }
        }
    }

}
