package com.phicomm.remotecontrol.modules.personal.account.token;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.phicomm.remotecontrol.base.BaseApplication;
import com.phicomm.remotecontrol.modules.personal.account.event.LogoutEvent;
import com.phicomm.remotecontrol.modules.personal.account.http.HttpErrorCode;
import com.phicomm.remotecontrol.modules.personal.account.local.LocalDataRepository;
import com.phicomm.remotecontrol.modules.personal.account.resultbean.BaseResponseBean;
import com.phicomm.remotecontrol.modules.personal.account.resultbean.TokenUpdateBean;
import com.phicomm.remotecontrol.preference.PreferenceDef;
import com.phicomm.remotecontrol.util.CommonUtils;

import org.greenrobot.eventbus.EventBus;

import static com.phicomm.remotecontrol.modules.personal.account.token.TokenManager.TokenStatus.REFRESH;

/**
 * Created by yong04.zhou on 2017/9/14.
 */

public class TokenManager implements TokenManagerContract.View {

    private static TokenManager mTokenManager;

    public static final int LOGIN = 1;
    public static final int LOGOUT = 2;

    private static TokenManagerContract.Presenter mTokenmanagerPresenter;

    private TokenManager() {
        if (mTokenmanagerPresenter == null) {
            mTokenmanagerPresenter = new TokenManagerPresenter(this);
        }
    }

    public static TokenManager getInstance() {
        if (mTokenManager == null) {
            mTokenManager = new TokenManager();
        }
        return mTokenManager;
    }

    /**
     * 保存两个token以及它们的有效期限
     *
     * @param accessToken     访问令牌
     * @param refreshToken    刷新访问令牌的刷新令牌
     * @param accessValidity  访问令牌的有效期 单位秒
     * @param refreshValidity 刷新令牌的有效期 单位秒
     */
    public void saveTokens(String accessToken, String refreshToken, String refreshValidity, String accessValidity) {
        long currentTimeMillis = System.currentTimeMillis();

        LocalDataRepository.getInstance(BaseApplication.getContext()).setAccessToken(accessToken);
        LocalDataRepository.getInstance(BaseApplication.getContext()).setRefreshToken(refreshToken);
        LocalDataRepository.getInstance(BaseApplication.getContext()).setAccessStartTime(currentTimeMillis);
        LocalDataRepository.getInstance(BaseApplication.getContext()).setRefreshStartTime(currentTimeMillis);
        LocalDataRepository.getInstance(BaseApplication.getContext()).setAccessValidity(Long.parseLong(accessValidity));
        LocalDataRepository.getInstance(BaseApplication.getContext()).setRefreshValidity(Long.parseLong(refreshValidity));

    }

    /**
     * 保存刷新令牌
     *
     * @param accessToken    访问令牌
     * @param accessValidity 访问令牌有效期
     */
    private void saveAccessToken(String accessToken, String accessValidity) {
        long currentTimeMillis = System.currentTimeMillis();

        LocalDataRepository.getInstance(BaseApplication.getContext()).setAccessToken(accessToken);
        LocalDataRepository.getInstance(BaseApplication.getContext()).setAccessStartTime(currentTimeMillis);
        LocalDataRepository.getInstance(BaseApplication.getContext()).setAccessValidity(Long.parseLong(accessValidity));//access_token_expire
    }

    /**
     * 获取token的可用状态
     *
     * @return
     */
    public TokenStatus getTokenStatus() {
        String accessToken = LocalDataRepository.getInstance(BaseApplication.getContext()).getAccessToken();
        String refreshToken = LocalDataRepository.getInstance(BaseApplication.getContext()).getRefreshToken();
        long accessValidity = LocalDataRepository.getInstance(BaseApplication.getContext()).getAccessValidity();
        long refreshValidity = LocalDataRepository.getInstance(BaseApplication.getContext()).getRefreshValidity();
        long accessStartTime = LocalDataRepository.getInstance(BaseApplication.getContext()).getAccessStartTime();
        long refreshStartTime = LocalDataRepository.getInstance(BaseApplication.getContext()).getRefreshStartTime();
        if (TextUtils.isEmpty(accessToken) || TextUtils.isEmpty(refreshToken)
                || accessValidity == 0 || refreshValidity == 0) {
            return TokenStatus.LOGOUT;
        }
        if (accessStartTime + accessValidity * 1000 > System.currentTimeMillis()) {
            return TokenStatus.ACCESS;
        }
        if (refreshStartTime + refreshValidity * 1000 > System.currentTimeMillis()) {
            return REFRESH;
        }
        return TokenStatus.LOGOUT;
    }

    /**
     * 获取访问令牌
     *
     * @return
     */
    public String getAccessToken() {
        return LocalDataRepository.getInstance(BaseApplication.getContext()).getAccessToken();
    }

    public void clear() {
        LocalDataRepository.getInstance(BaseApplication.getContext()).clearSPByName(PreferenceDef.SP_NAME_TOKEN);
    }

    /**
     * 刷新访问令牌
     *
     * @param authCode 授权码
     */
    public void refreshToken(@NonNull String authCode) {
        String refreshToken = LocalDataRepository.getInstance(BaseApplication.getContext()).getRefreshToken();
        if (TextUtils.isEmpty(refreshToken) || TokenStatus.LOGOUT == getTokenStatus()) {
            return;
        }
        mTokenmanagerPresenter.refreshToken(refreshToken, authCode);
    }

    public enum TokenStatus {
        ACCESS,     //access token有效可用
        REFRESH,    //access token过期，可通过refresh token刷新得到
        LOGOUT     //refresh token也不可用了，需要清除登录状态重新登录
    }


    public static int checkAccessTokenAvailable() {
        TokenManager.TokenStatus tokenStatus = TokenManager.getInstance().getTokenStatus();

        //for test refresh token
//        tokenStatus = REFRESH;
        switch (tokenStatus) {
            case ACCESS:
                return LOGIN;
            case REFRESH:
                TokenManager.getInstance().refreshToken(LocalDataRepository.getInstance(BaseApplication.getContext()).getAuthorizationCode());
                return LOGIN;
            case LOGOUT:
                LocalDataRepository.getInstance(BaseApplication.getContext()).setCloudLoginStatus(false);
                return LOGOUT;
            default:
                return LOGIN;
        }
    }

    @Override
    public void analysisResponseBean(BaseResponseBean t) {
        if (t instanceof TokenUpdateBean) {
            TokenUpdateBean bean = (TokenUpdateBean) t;
            if (bean.error.equals(HttpErrorCode.SUCCESS)) {
                //刷新成功
                if (!TextUtils.isEmpty(bean.access_token) && !TextUtils.isEmpty(bean.access_token_expire)) {
                    saveAccessToken(bean.access_token, bean.access_token_expire);
                    LocalDataRepository.getInstance(BaseApplication.getContext()).setAccessToken(bean.access_token);
                }
            } else if (bean.error.equals("5") || bean.error.equals("6") || bean.error.equals("30")) {

                //handler abnormal resonose
                int errorCode = Integer.parseInt(bean.error);
                int errorStringRes = HttpErrorCode.getErrorStringByErrorCode(errorCode);
                CommonUtils.showToastBottom(BaseApplication.getContext().getString(errorStringRes));

                //send logout event
                EventBus.getDefault().post(new LogoutEvent());
            } else {
                //refresh fail,maybe network error,refresh again

            }
        }
    }
}
