package com.phicomm.remotecontrol.request;


import com.google.gson.Gson;
import com.phicomm.remotecontrol.beans.BaseResponseBean;
import com.phicomm.remotecontrol.constant.HttpConfig;
import com.phicomm.remotecontrol.modules.personal.apply.ApplyInfosBean;
import com.phicomm.remotecontrol.modules.personal.upgrade.UpdateInfoResponseBean;
import com.phicomm.remotecontrol.util.CommonUtils;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Created by hao04.wu on 2017/7/5.
 */

public class HttpRequestManager {
    private static HttpRequestManager mInsatnce = null;
    private HttpVersionService mHttpVersionService = null;
    private HttpAppInfosService mHttpAppInfosService = null;
    private static OkHttpClient mOkHttpClient;
    public static final long DEFAULT_TIMEOUT = 3;

    public static HttpRequestManager getInstance() {
        if (mInsatnce == null) {
            mInsatnce = new HttpRequestManager();
        }
        return mInsatnce;
    }

    private HttpRequestManager() {
        Retrofit updateRetrofit = getRetrofit(HttpConfig.PHICOMM_OTA_BASE_URL);
        mHttpVersionService = updateRetrofit.create(HttpVersionService.class);
    }

    private Retrofit getRetrofit(String baseUrl) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(getOkHttpClientSSL())
                .build();
        return retrofit;
    }

    private OkHttpClient getOkHttpClientSSL() {

        if (mOkHttpClient == null) {
            //https cer
            HttpsCer.SSLParams sslParams = HttpsCer.getSslSocketFactory(null, null, null);
            mOkHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                    .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                    .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                    .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
                    .build();
        }
        return mOkHttpClient;
    }

    /**
     * 由于连接的每一个设备IP地址不一定相同，所以每次请求的URL也是可能发生变化的
     */
    public HttpRequestManager getHttpAppInfosService() {
        if (CommonUtils.getCurrentUrl() != null) {
            Retrofit appInfosRetrofit = getRetrofit(CommonUtils.getCurrentUrl());
            mHttpAppInfosService = appInfosRetrofit.create(HttpAppInfosService.class);
        }
        return mInsatnce;
    }


    /**
     * RxJava
     *
     * @param options
     * @param onCallListener
     */
    public void checkVersion(Map<String, String> options, final OnCallListener onCallListener) {
        Observable<UpdateInfoResponseBean> mObservable = mHttpVersionService.checkVersion(options);
        mObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseResponseBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (onCallListener != null) {
                            onCallListener.onError(e.getMessage());
                        }
                    }

                    @Override
                    public void onNext(BaseResponseBean resultBean) {
                        dealResponse(resultBean, onCallListener);
                    }
                });
    }

    /**
     * RxJava
     * 获取box所有已安装应用的信息
     *
     * @param onCallListener
     */
    public void getAppInfos(final OnCallListener onCallListener) {
        if (mHttpAppInfosService != null) {
            Observable<ApplyInfosBean> mObservable = mHttpAppInfosService.getAppInfos();
            mObservable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<BaseResponseBean>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                            if (onCallListener != null) {
                                onCallListener.onError(e.getMessage());
                            }
                        }

                        @Override
                        public void onNext(BaseResponseBean resultBean) {
                            dealResponse(resultBean, onCallListener);
                        }
                    });
        }
    }

    public void openApplication(Map<String, String> options, final OnCallListener onCallListener) {
        Gson gson = new Gson();
        String strEntity = gson.toJson(options);
        RequestBody formBody = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"), strEntity);
        if (mHttpAppInfosService != null) {
            Observable<ApplyInfosBean> mObservable = mHttpAppInfosService.openApplication(formBody);
            mObservable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<BaseResponseBean>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            if (onCallListener != null) {
                                onCallListener.onError(e.getMessage());
                            }
                        }

                        @Override
                        public void onNext(BaseResponseBean resultBean) {
                            dealResponse(resultBean, onCallListener);
                        }
                    });
        }
    }


    /**
     * 用于统一处理各个code代表的问题
     *
     * @param responseBean
     * @param onCallListener
     */
    private void dealResponse(BaseResponseBean responseBean, OnCallListener onCallListener) {
        if (responseBean == null) {
            return;
        }
        if (onCallListener != null) {
            onCallListener.onSuccess(responseBean);
        }
    }

    public interface OnCallListener {

        void onSuccess(BaseResponseBean responseBean);

        void onError(String message);

    }
}
