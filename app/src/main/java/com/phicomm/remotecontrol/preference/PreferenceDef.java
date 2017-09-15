package com.phicomm.remotecontrol.preference;

/**
 * Created by yong04.zhou on 2017/9/11.
 */

public class PreferenceDef {

    //user info
    public static final String SP_SETTINGS = "sp_settings"; //preferenceName

    public static final String VOICE_STATUS = "voice_status";//voice key
    public static final String VIBRATE_STATUS = "vibrate_status";//vibrate key

    public static final String APP_VERSION = "app_version";
    public static final String VERSION_DOWNLOAD_ID = "app_version";
    public static final String IS_HAVE_NEW_VERSIOM = "isHaveNewVersion";


    //login info
    public static final String SP_NAME_USER_COOKIE = "sp_cookie_utils";

    public static final String CLOUD_LOGIN_STATUS = "cloud_login_status";
    public static final String CLOUD_USERNAME = "cloud_username";
    public static final String CLOUD_PASSWORD = "cloud_password";
    public static final String CLOUD_REMEMBER_ME = "remember_me";
    public static final String AUTHORIZATION_CODE = "authorization_code";

    //token manager
    public static final String SP_NAME_TOKEN = "sp_token";

    public static final String ACCESS_TOKEN = "access_token";
    public static final String REFRESH_TOKEN = "refresh_token";
    public static final String ACCESS_TOKEN_START_TIME = "access_token_start_time";
    public static final String REFRESH_TOKEN_START_TIME = "refresh_token_start_time";
    public static final String ACCESS_TOKEN_VALIDITY = "access_token_validity";
    public static final String REFRESH_TOKEN_VALIDITY = "refresh_token_validity";
}
