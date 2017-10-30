package com.phicomm.remotecontrol.modules.personal.account.http;

import android.util.Log;

import com.phicomm.remotecontrol.R;
import com.phicomm.remotecontrol.base.BaseApplication;
import com.phicomm.remotecontrol.modules.personal.account.event.LogoutEvent;
import com.phicomm.remotecontrol.modules.personal.account.event.MultiLogoutEvent;
import com.phicomm.remotecontrol.modules.personal.account.resultbean.BaseResponseBean;
import com.phicomm.remotecontrol.modules.personal.account.resultbean.CaptchaResponseBean;
import com.phicomm.remotecontrol.modules.personal.account.resultbean.CheckphonenumberResponseBean;
import com.phicomm.remotecontrol.modules.personal.account.resultbean.LoginResponseBean;
import com.phicomm.remotecontrol.modules.personal.account.resultbean.ModifypasswordResponseBean;
import com.phicomm.remotecontrol.modules.personal.account.resultbean.RegisterResponseBean;
import com.phicomm.remotecontrol.modules.personal.account.resultbean.TokenUpdateBean;
import com.phicomm.remotecontrol.modules.personal.account.resultbean.VerifycodeResponseBean;
import com.phicomm.remotecontrol.util.CommonUtils;
import com.phicomm.remotecontrol.util.DialogUtils;
import com.phicomm.remotecontrol.util.StringUtils;

import org.greenrobot.eventbus.EventBus;

import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import rx.Subscriber;

/**
 * Created by yong04.zhou on 2017/9/13.
 */

public abstract class CustomSubscriber<T> extends Subscriber<T> {

    private final String TAG = "OkHttpLogInfo";

    abstract public void onCustomNext(T t);

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        if (e != null) {
            Log.d(TAG, "onError:" + e.toString());
        }
        DialogUtils.cancelLoadingDialog();

        //get errorstring by exception type
        int errorStringRes = R.string.common_error;
        if (e instanceof SocketTimeoutException) {
            errorStringRes = R.string.connect_timeout;
        } else if (e instanceof UnknownHostException || e instanceof SocketException) {
            errorStringRes = R.string.net_connect_fail;
        }

        CommonUtils.showToastBottom(BaseApplication.getContext().getString(errorStringRes));

    }

    @Override
    public void onNext(T t) {
        if (interceptAbnormalResponse(t))
            return;
        onCustomNext(t);

    }

    public boolean interceptAbnormalResponse(T t) {
        if (t instanceof BaseResponseBean) {
            BaseResponseBean baseResponseBean = (BaseResponseBean) t;
            if (StringUtils.isNull(baseResponseBean.error) || "0".equals(baseResponseBean.error)) {
                return false;//不拦截
            } else {

                //token异常问题需要提前拦截
                if (baseResponseBean.error.equals("5") //token失效
                        || baseResponseBean.error.equals("26")) { //账户已退出
                    //send logout event
                    DialogUtils.cancelLoadingDialog();
                    EventBus.getDefault().post(new LogoutEvent());
                    return true;
                } else if (baseResponseBean.error.equals("30")) { //多端登录
                    //multi login,need logout
                    DialogUtils.cancelLoadingDialog();
                    EventBus.getDefault().post(new MultiLogoutEvent());
                    return true;
                }

                //not intercept list
                if (t instanceof TokenUpdateBean
                        || t instanceof CaptchaResponseBean
                        || t instanceof VerifycodeResponseBean
                        || t instanceof CheckphonenumberResponseBean
                        || t instanceof RegisterResponseBean) {
                    return false;//not intercept
                }
                DialogUtils.cancelLoadingDialog();

                int errorCode = Integer.parseInt(baseResponseBean.error);
                int errorStringRes = HttpErrorCode.getErrorStringByErrorCode(errorCode);

                if (errorStringRes == R.string.password_error && t instanceof ModifypasswordResponseBean) {
                    errorStringRes = R.string.old_password_error;
                } else if (errorStringRes == R.string.password_error && t instanceof LoginResponseBean) {
                    errorStringRes = R.string.account_password_not_match;
                }

                CommonUtils.showToastBottom(BaseApplication.getContext().getString(errorStringRes));

                return true;
            }
        }
        return false;
    }

}
