package com.phicomm.remotecontrol.modules.personal.account.registerlogin.register;

import com.phicomm.remotecontrol.modules.personal.account.http.CustomSubscriber;
import com.phicomm.remotecontrol.modules.personal.account.http.HttpDataRepository;
import com.phicomm.remotecontrol.modules.personal.account.resultbean.AuthorizationResponseBean;
import com.phicomm.remotecontrol.modules.personal.account.resultbean.CaptchaResponseBean;
import com.phicomm.remotecontrol.modules.personal.account.resultbean.CheckphonenumberResponseBean;
import com.phicomm.remotecontrol.modules.personal.account.resultbean.LoginResponseBean;
import com.phicomm.remotecontrol.modules.personal.account.resultbean.RegisterResponseBean;
import com.phicomm.remotecontrol.modules.personal.account.resultbean.VerifycodeResponseBean;

import java.util.HashMap;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.RequestBody;

/**
 * Created by yong04.zhou on 2017/9/14.
 */

public class RegisterPresenter implements RegisterContract.Presenter {

    private RegisterContract.View mRegisterView;

    public RegisterPresenter(RegisterContract.View mRegisterView) {
        this.mRegisterView = mRegisterView;
    }

    @Override
    public void doAuthorization(String client_id, String response_type, String scope, String client_secret) {
        Map<String, String> map = new HashMap<>();
        map.put("client_id", client_id);
        map.put("response_type", response_type);
        map.put("scope", scope);
        map.put("client_secret", client_secret);
        HttpDataRepository.getInstance().getAuthorization(new CustomSubscriber<AuthorizationResponseBean>() {
            @Override
            public void onCustomNext(AuthorizationResponseBean authorizationResponseBean) {
                mRegisterView.analysisResponseBean(authorizationResponseBean);

            }
        }, map);
    }

    @Override
    public void doRegister(String authorizationcode, String phonenumber, String password, String registersource, String verificationcode) {
        RequestBody formBody = new FormBody.Builder()
                .add("authorizationcode", authorizationcode)
                .add("phonenumber", phonenumber)
                .add("password", password)
                .add("registersource", registersource)
                .add("verificationcode", verificationcode)
                .build();
        HttpDataRepository.getInstance().register(new CustomSubscriber<RegisterResponseBean>() {
            @Override
            public void onCustomNext(RegisterResponseBean bean) {
                mRegisterView.analysisResponseBean(bean);
            }
        }, formBody);

    }

    @Override
    public void doCheckAccountRegistered(String authorizationcode, String phonenumber) {
        Map<String, String> map = new HashMap<>();
        map.put("authorizationcode", authorizationcode);
        map.put("phonenumber", phonenumber);
        HttpDataRepository.getInstance().checkPhoneNumber(new CustomSubscriber<CheckphonenumberResponseBean>() {
            @Override
            public void onCustomNext(CheckphonenumberResponseBean bean) {
                mRegisterView.analysisResponseBean(bean);
            }
        }, map);
    }

    @Override
    public void doGetVerifyCode(String authorizationcode, String phonenumber, String verificationtype, String captcha, String captchaid) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("authorizationcode", authorizationcode);
        parameters.put("phonenumber", phonenumber);
        parameters.put("verificationtype", verificationtype);
        parameters.put("captcha", captcha);
        parameters.put("captchaid", captchaid);
        HttpDataRepository.getInstance().verifyCode(new CustomSubscriber<VerifycodeResponseBean>() {
            @Override
            public void onCustomNext(VerifycodeResponseBean verifycodeResponseBean) {
                mRegisterView.analysisResponseBean(verifycodeResponseBean);
            }
        }, parameters);

    }

    @Override
    public void doGetCaptchaImageCode(String authorizationcode) {
        Map<String, String> map = new HashMap<>();
        map.put("authorizationcode", authorizationcode);
        HttpDataRepository.getInstance().captcha(new CustomSubscriber<CaptchaResponseBean>() {
            @Override
            public void onCustomNext(CaptchaResponseBean captchaResponseBean) {
                mRegisterView.analysisResponseBean(captchaResponseBean);
            }
        }, map);
    }

    @Override
    public void doPhoneLogin(String authorizationcode, String phonenumber, String password) {
        RequestBody formBody = new FormBody.Builder()
                .add("authorizationcode", authorizationcode)
                .add("phonenumber", phonenumber)
                .add("password", password)
                .build();
        HttpDataRepository.getInstance().login(
                new CustomSubscriber<LoginResponseBean>() {
                    @Override
                    public void onCustomNext(LoginResponseBean loginResponseBean) {
                        mRegisterView.analysisResponseBean(loginResponseBean);
                    }
                }, formBody);
    }
}
