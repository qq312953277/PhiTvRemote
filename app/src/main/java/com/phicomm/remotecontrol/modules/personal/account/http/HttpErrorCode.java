package com.phicomm.remotecontrol.modules.personal.account.http;

import com.phicomm.remotecontrol.R;

/**
 * Created by yong04.zhou on 2017/9/13.
 */

public class HttpErrorCode {

    public static final String SUCCESS = "0";

    public static int getErrorStringByErrorCode(int error) {
        int returnStringRes = R.string.common_error;
        switch (error) {
            case 1:
                returnStringRes = R.string.verifycode_error;
                break;
            case 2:
                returnStringRes = R.string.verifycode_expire;
                break;
            case 3:
                returnStringRes = R.string.not_verify_by_oldphonenumber;
                break;
            case 4:
                returnStringRes = R.string.old_password_error;
                break;
            case 5:
                returnStringRes = R.string.token_invalid;
                break;
            case 6:
                returnStringRes = R.string.refresh_token_expire;
                break;
            case 7:
                returnStringRes = R.string.account_not_exist;
                break;
            case 8:
                returnStringRes = R.string.password_error;
                break;
            case 9:
                returnStringRes = R.string.cliend_id_not_exist;
                break;
            case 10:
                returnStringRes = R.string.client_secret_error;
                break;
            case 11:
                returnStringRes = R.string.authorization_error;
                break;
            case 12:
                returnStringRes = R.string.parameters_error;
                break;
            case 13:
                returnStringRes = R.string.get_verifycode_failed;
                break;
            case 14:
                returnStringRes = R.string.account_exist;
                break;
            case 15:
                returnStringRes = R.string.password_not_set;
                break;
            case 16:
                returnStringRes = R.string.get_detail_userinfo_fail;
                break;
            case 17:
                returnStringRes = R.string.no_authority;
                break;
            case 18:
                returnStringRes = R.string.image_format_fail;
                break;
            case 19:
                returnStringRes = R.string.image_is_empty;
                break;
            case 20:
                returnStringRes = R.string.notset_user_icon;
                break;
            case 21:
                returnStringRes = R.string.token_error;
                break;
            case 22:
                returnStringRes = R.string.account_not_active;
                break;
            case 23:
                returnStringRes = R.string.verifycode_has_used;
                break;
            case 24:
                returnStringRes = R.string.account_is_exist;
                break;
            case 25:
                returnStringRes = R.string.mail_is_exist;
                break;
            case 26:
                returnStringRes = R.string.account_is_logout;
                break;
            case 27:
                returnStringRes = R.string.need_refresh_token;
                break;
            case 28:
                returnStringRes = R.string.password_securiry_not_set;
                break;
            case 29:
                returnStringRes = R.string.password_security_error;
                break;
            case 30:
                returnStringRes = R.string.multi_account_error;
                break;
            case 31:
                returnStringRes = R.string.mail_format_error;
                break;
            case 32:
                returnStringRes = R.string.password_format_error;
                break;
            case 33:
                returnStringRes = R.string.username_format_error;
                break;
            case 34:
                returnStringRes = R.string.phonenum_format_error;
                break;
            case 35:
                returnStringRes = R.string.phonenumber_not_register_account;
                break;
            case 36:
                returnStringRes = R.string.captcha_expire;
                break;
            case 37:
                returnStringRes = R.string.captcha_error;
                break;
            case 38:
                returnStringRes = R.string.get_verifycode_too_fast;
                break;
            case 39:
                returnStringRes = R.string.get_verifycode_count_expire;
                break;
            case 50:
                returnStringRes = R.string.server_abnormal;
                break;
        }
        return returnStringRes;
    }
}
