package com.phicomm.remotecontrol.modules.personal.account.registerlogin.login;


import com.phicomm.remotecontrol.modules.personal.account.http.CustomSubscriber;
import com.phicomm.remotecontrol.modules.personal.account.http.HttpDataRepository;
import com.phicomm.remotecontrol.modules.personal.account.resultbean.AuthorizationResponseBean;
import com.phicomm.remotecontrol.modules.personal.account.resultbean.LoginResponseBean;

import java.util.HashMap;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.RequestBody;

/**
 * Created by yong04.zhou on 2017/9/4.
 */

public class LoginPresenter implements LoginContract.Presenter {

    private LoginContract.View mLoginView;

    public LoginPresenter(LoginContract.View mLoginView) {
        this.mLoginView = mLoginView;
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
                mLoginView.analysisResponseBean(authorizationResponseBean);

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
                        mLoginView.analysisResponseBean(loginResponseBean);
                    }
                }, formBody);

    }


}
