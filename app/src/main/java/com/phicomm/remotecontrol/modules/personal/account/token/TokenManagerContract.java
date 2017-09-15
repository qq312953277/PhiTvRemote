package com.phicomm.remotecontrol.modules.personal.account.token;

import com.phicomm.remotecontrol.modules.personal.account.resultbean.BaseResponseBean;

/**
 * Created by yong04.zhou on 2017/9/14.
 */

public interface TokenManagerContract {
    interface View {
        //解析http response
        void analysisResponseBean(BaseResponseBean t);
    }

    interface Presenter {
        //刷新token
        void refreshToken(String refreshToken, String authorizationCode);
    }
}
