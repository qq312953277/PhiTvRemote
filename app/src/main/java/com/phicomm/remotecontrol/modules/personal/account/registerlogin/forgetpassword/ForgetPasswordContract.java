package com.phicomm.remotecontrol.modules.personal.account.registerlogin.forgetpassword;

import com.phicomm.remotecontrol.modules.personal.account.resultbean.BaseResponseBean;

/**
 * Created by yong04.zhou on 2017/9/14.
 */

public interface ForgetPasswordContract {

    interface View {
        //点击获取验证码按钮后，倒计时60s
        void startVerfyCodeTimerTask();

        //解析http response
        void analysisResponseBean(BaseResponseBean t);
    }

    interface Presenter {

        //request phicomm authorization
        void doAuthorization(String client_id, String response_type, String scope, String client_secret);

        //reset account password
        void doResetPassword(String authorizationcode, String phonenumber, String newpassword, String verificationcode);

        //get image code
        void doGetCaptchaImageCode(String authorizationcode);

        //check valid account
        void doCheckAccountRegistered(String authorizationcode, String phonenumber);

        //get sms verify code
        void doGetVerifyCode(String authorizationcode, String phonenumber, String verificationtype, String captcha, String captchaid);
    }
}
