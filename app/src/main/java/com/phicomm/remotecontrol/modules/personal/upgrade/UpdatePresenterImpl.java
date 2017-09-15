package com.phicomm.remotecontrol.modules.personal.upgrade;

import android.content.Context;
import android.content.Intent;

import com.phicomm.remotecontrol.beans.BaseResponseBean;
import com.phicomm.remotecontrol.request.RequestManager;

import java.util.Map;

/**
 * Created by hk on 2016/10/11.
 */
public class UpdatePresenterImpl implements UpdatePresenter {
    private Context mContext;
    private UpdateView mView;

    public UpdatePresenterImpl(Context mContext, UpdateView mView) {
        this.mContext = mContext;
        this.mView = mView;

    }

    @Override
    public void checkVersion(Map<String, String> options) {
        RequestManager.getInstance().checkVersion(options, new RequestManager.OnCallListener() {
            @Override
            public void onSuccess(BaseResponseBean responseBean) {
                UpdateInfoResponseBean bean = (UpdateInfoResponseBean) responseBean;
                mView.onSuccess(null);
                mView.checkVersion(bean);
            }

            @Override
            public void onError(String message) {
                mView.showMessage(message);
            }
        });

    }

    @Override
    public void downloadFile(String url, String versionName) {
        Intent intent = new Intent();
        intent.setClass(mContext, UpdateService.class);
        intent.putExtra(UpdateService.DOWNLOAD_URL, url);
        intent.putExtra(UpdateService.DOWNLOAD_NAME, versionName + ".apk");
        mContext.startService(intent);
    }
}
