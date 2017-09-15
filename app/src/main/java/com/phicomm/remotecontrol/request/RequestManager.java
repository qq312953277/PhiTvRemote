package com.phicomm.remotecontrol.request;


import com.phicomm.remotecontrol.beans.BaseResponseBean;
import com.phicomm.remotecontrol.constant.HttpConfig;
import com.phicomm.remotecontrol.modules.personal.upgrade.UpdateInfoResponseBean;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
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

public class RequestManager {
    private static RequestManager mInsatnce = null;
    private RequestService mRequestService = null;
    private static OkHttpClient mOkHttpClient;
    public static final long DEFAULT_TIMEOUT = 3 * 1000;


    public static RequestManager getInstance() {
        if (mInsatnce == null) {
            mInsatnce = new RequestManager();
        }
        return mInsatnce;
    }

    private RequestManager() {
        Retrofit updateRetrofit = getRetrofit(HttpConfig.PHICOMM_OTA_BASE_URL);
        mRequestService = updateRetrofit.create(RequestService.class);
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
     * RxJava
     *
     * @param options
     * @param onCallListener
     */
    public void checkVersion(Map<String, String> options, final OnCallListener onCallListener) {
        Observable<UpdateInfoResponseBean> mObservable = mRequestService.checkVersion(options);
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
