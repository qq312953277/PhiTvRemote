package com.phicomm.remotecontrol.modules.personal.account.local;

import android.content.Context;

import com.phicomm.remotecontrol.modules.personal.account.resultbean.AccountDetailBean;
import com.phicomm.remotecontrol.preference.PreferenceDef;
import com.phicomm.remotecontrol.preference.PreferenceRepository;

/**
 * Created by yong04.zhou on 2017/9/13.
 */

public class LocalDataRepository {


    private Context mContext;
    private static LocalDataRepository mInstance = null;
    private PreferenceRepository mPreferenceRepository = null;

    public LocalDataRepository(Context mContext) {
        this.mContext = mContext;
        this.mPreferenceRepository = new PreferenceRepository(mContext);
    }

    public static LocalDataRepository getInstance(Context mContext) {
        if (mInstance == null) {
            mInstance = new LocalDataRepository(mContext);
        }
        return mInstance;
    }

    /**
     * 设置授权码
     */
    public void setAuthorizationCode(String code) {
        mPreferenceRepository.put(PreferenceDef.SP_NAME_USER_COOKIE, PreferenceDef.AUTHORIZATION_CODE, code);
    }

    /**
     * 获取授权码
     */
    public String getAuthorizationCode() {
        return (String) mPreferenceRepository.get(PreferenceDef.SP_NAME_USER_COOKIE, PreferenceDef.AUTHORIZATION_CODE, "");
    }


    public void setRememberMe(boolean rememberMe) {
        mPreferenceRepository.put(PreferenceDef.SP_NAME_USER_COOKIE, PreferenceDef.CLOUD_REMEMBER_ME, rememberMe);
    }

    public boolean getRememberMe() {
        return (boolean) mPreferenceRepository.get(PreferenceDef.SP_NAME_USER_COOKIE, PreferenceDef.CLOUD_REMEMBER_ME, false);//false default
    }

    public void setUserName(String userName) {
        mPreferenceRepository.put(PreferenceDef.SP_NAME_USER_COOKIE, PreferenceDef.CLOUD_USERNAME, userName);
    }

    public String getUserName() {
        return (String) mPreferenceRepository.get(PreferenceDef.SP_NAME_USER_COOKIE, PreferenceDef.CLOUD_USERNAME, "");
    }


    public void setPassword(String password) {
        mPreferenceRepository.put(PreferenceDef.SP_NAME_USER_COOKIE, PreferenceDef.CLOUD_PASSWORD, password);
    }

    public String getPassword() {
        return (String) mPreferenceRepository.get(PreferenceDef.SP_NAME_USER_COOKIE, PreferenceDef.CLOUD_PASSWORD, "");
    }

    /**
     * getAccessToken
     *
     * @return
     */
    public String getAccessToken() {
        return (String) mPreferenceRepository.get(PreferenceDef.SP_NAME_TOKEN, PreferenceDef.ACCESS_TOKEN, "");
    }

    /**
     * setAccessToken
     */
    public void setAccessToken(String accessToken) {
        mPreferenceRepository.put(PreferenceDef.SP_NAME_TOKEN, PreferenceDef.ACCESS_TOKEN, accessToken);
    }

    /**
     * getRefreshToken
     *
     * @return
     */
    public String getRefreshToken() {
        return (String) mPreferenceRepository.get(PreferenceDef.SP_NAME_TOKEN, PreferenceDef.REFRESH_TOKEN, "");
    }

    /**
     * setRefreshToken
     *
     * @param refreshToken
     */
    public void setRefreshToken(String refreshToken) {
        mPreferenceRepository.put(PreferenceDef.SP_NAME_TOKEN, PreferenceDef.REFRESH_TOKEN, refreshToken);
    }

    /**
     * getAccessValidity
     *
     * @return
     */
    public Long getAccessValidity() {
        return (Long) mPreferenceRepository.get(PreferenceDef.SP_NAME_TOKEN, PreferenceDef.ACCESS_TOKEN_VALIDITY, 0l);
    }

    /**
     * setAccessValidity
     *
     * @param accessValidity
     */
    public void setAccessValidity(Long accessValidity) {
        mPreferenceRepository.put(PreferenceDef.SP_NAME_TOKEN, PreferenceDef.ACCESS_TOKEN_VALIDITY, accessValidity);
    }

    /**
     * getRefreshValidity
     *
     * @return
     */
    public Long getRefreshValidity() {
        return (Long) mPreferenceRepository.get(PreferenceDef.SP_NAME_TOKEN, PreferenceDef.REFRESH_TOKEN_VALIDITY, 0l);
    }

    /**
     * setRefreshValidity
     *
     * @param refreshValidity
     */
    public void setRefreshValidity(Long refreshValidity) {
        mPreferenceRepository.put(PreferenceDef.SP_NAME_TOKEN, PreferenceDef.REFRESH_TOKEN_VALIDITY, refreshValidity);
    }

    /**
     * getRefreshStartTime
     *
     * @return
     */
    public Long getRefreshStartTime() {
        return (Long) mPreferenceRepository.get(PreferenceDef.SP_NAME_TOKEN, PreferenceDef.REFRESH_TOKEN_START_TIME, 0l);
    }

    /**
     * setRefreshStartTime
     *
     * @param refreshStartTime
     */
    public void setRefreshStartTime(Long refreshStartTime) {
        mPreferenceRepository.put(PreferenceDef.SP_NAME_TOKEN, PreferenceDef.REFRESH_TOKEN_START_TIME, refreshStartTime);
    }

    /**
     * V
     *
     * @return
     */
    public Long getAccessStartTime() {
        return (Long) mPreferenceRepository.get(PreferenceDef.SP_NAME_TOKEN, PreferenceDef.ACCESS_TOKEN_START_TIME, 0l);
    }

    /**
     * setAccessStartTime
     *
     * @param accessStartTime
     */
    public void setAccessStartTime(Long accessStartTime) {
        mPreferenceRepository.put(PreferenceDef.SP_NAME_TOKEN, PreferenceDef.ACCESS_TOKEN_START_TIME, accessStartTime);
    }

    /**
     * 退出制定的SP
     */
    public void clearSPByName(String SP) {
        mPreferenceRepository.clear(SP);
    }

    /**
     * 云端之前是否已经登录了
     *
     * @return 登录状态
     */
    public boolean isCloudLogined() {
        return (boolean) mPreferenceRepository.get(PreferenceDef.SP_NAME_USER_COOKIE, PreferenceDef.CLOUD_LOGIN_STATUS, false);
    }

    /**
     * 设置云端的登录状态
     *
     * @param loginStatus 已登录为true，已登出为false
     */
    public void setCloudLoginStatus(boolean loginStatus) {
        mPreferenceRepository.put(PreferenceDef.SP_NAME_USER_COOKIE, PreferenceDef.CLOUD_LOGIN_STATUS, loginStatus);
    }

    /**
     * 设置用户的基本信息
     */
    public void setAccountDetailInfo(AccountDetailBean accountDetailBean) {
        mPreferenceRepository.saveObj(PreferenceDef.SP_NAME_USER_COOKIE, AccountDetailBean.class.getSimpleName(), accountDetailBean);
    }

    /**
     * 获取用户的基本信息
     *
     * @return
     */
    public AccountDetailBean getAccountDetailInfo() {
        return (AccountDetailBean) mPreferenceRepository.getObj(PreferenceDef.SP_NAME_USER_COOKIE, AccountDetailBean.class.getSimpleName());
    }

}
