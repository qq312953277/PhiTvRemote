package com.phicomm.remotecontrol.modules.personal.account.token;

import com.phicomm.remotecontrol.modules.personal.account.http.CustomSubscriber;
import com.phicomm.remotecontrol.modules.personal.account.http.HttpDataRepository;
import com.phicomm.remotecontrol.modules.personal.account.resultbean.TokenUpdateBean;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yong04.zhou on 2017/9/14.
 */

public class TokenManagerPresenter implements TokenManagerContract.Presenter {

    private TokenManagerContract.View myTokenView;

    public TokenManagerPresenter(TokenManagerContract.View myView) {
        myTokenView = myView;

    }

    @Override
    public void refreshToken(String refreshToken, String authorizationcode) {

        Map<String, String> parameters = new HashMap<>();
        parameters.put("authorizationcode", authorizationcode);
        parameters.put("grant_type", "refresh_token"); //refresh_token固定的
        HttpDataRepository.getInstance().refreshToken(
                new CustomSubscriber<TokenUpdateBean>() {
                    @Override
                    public void onCustomNext(TokenUpdateBean tokenUpdateBean) {
                        myTokenView.analysisResponseBean(tokenUpdateBean);
                    }
                }, refreshToken, parameters);
    }
}