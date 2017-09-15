package com.phicomm.remotecontrol.modules.personal.account.registerlogin.login;

import com.phicomm.remotecontrol.modules.personal.account.resultbean.BaseResponseBean;

/**
 * Created by yong04.zhou on 2017/9/4.
 */

public interface LoginContract {

    interface View {
        //解析http response
        void analysisResponseBean(BaseResponseBean t);
    }

    interface Presenter {
        //request phicomm authorization
        void doAuthorization(String client_id, String response_type, String scope, String client_secret);

        //request phicomm login
        void doPhoneLogin(String authorizationcode, String phonenumber, String password);
    }


}
