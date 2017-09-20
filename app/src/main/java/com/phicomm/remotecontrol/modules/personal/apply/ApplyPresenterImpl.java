package com.phicomm.remotecontrol.modules.personal.apply;

import android.content.Context;

import com.phicomm.remotecontrol.beans.BaseResponseBean;
import com.phicomm.remotecontrol.request.HttpRequestManager;

import java.util.Map;

/**
 * Created by hao04.wu on 2017/9/18.
 */

public class ApplyPresenterImpl implements ApplyPresenter {
    private ApplyView mView;
    private Context mContext;

    public ApplyPresenterImpl(Context context, ApplyView view) {
        mContext = context;
        mView = view;
    }

    @Override
    public void getAppInfo() {
        HttpRequestManager.getInstance().getHttpAppInfosService().getAppInfos(new HttpRequestManager.OnCallListener() {
            @Override
            public void onSuccess(BaseResponseBean responseBean) {
                ApplyInfosBean bean = (ApplyInfosBean) responseBean;
                mView.getAppInfos(bean);
                mView.onSuccess(bean);
            }

            @Override
            public void onError(String message) {
                mView.onFailure(message);
            }
        });
    }

    @Override
    public void openApplication(Map<String, String> options) {
        HttpRequestManager.getInstance().getHttpAppInfosService().openApplication(options, new HttpRequestManager.OnCallListener() {
            @Override
            public void onSuccess(BaseResponseBean responseBean) {
                mView.onSuccess(responseBean);
            }

            @Override
            public void onError(String message) {
                mView.onFailure(message);
            }
        });
    }


}
