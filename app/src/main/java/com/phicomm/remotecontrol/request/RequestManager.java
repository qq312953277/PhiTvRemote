package com.phicomm.remotecontrol.request;


import com.phicomm.remotecontrol.beans.BaseBean;
import com.phicomm.remotecontrol.beans.ResultBean;

import java.util.Map;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by hao04.wu on 2017/7/5.
 */

public class RequestManager {
    private static RequestManager mInsatnce = null;
    private Retrofit retrofit = null;
    private RequestService mRequestService = null;
    private String url = "";

    private RequestManager() {
        retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(OKHttpManager.getNewOkHttpClientSSL())
                .build();
        mRequestService = retrofit.create(RequestService.class);
    }

    public RequestManager(String baseUrl) {
        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(OKHttpManager.getNewOkHttpClientSSL())
                .build();
        mRequestService = retrofit.create(RequestService.class);
    }

    public static RequestManager getInstance() {
        if (mInsatnce == null) {
            mInsatnce = new RequestManager();
        }
        return mInsatnce;
    }


    /**
     * RxJava
     *
     * @param options
     * @param onCallListener
     */
    public void getResult(Map<String, String> options, final OnCallListener onCallListener) {
        Observable<ResultBean> mObservable = mRequestService.getResult(options);
        mObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseBean>() {
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
                    public void onNext(BaseBean resultBean) {
                        dealResponse(resultBean, onCallListener);
                    }
                });
    }

    /**
     * RxJava
     *
     * @param options
     * @param onCallListener
     */
    public void postResult(Map<String, String> options, final OnCallListener onCallListener) {
        Observable<ResultBean> mObservable = mRequestService.postResult(options);
        mObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseBean>() {
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
                    public void onNext(BaseBean resultBean) {
                        dealResponse(resultBean, onCallListener);
                    }
                });
    }


    /**
     * 用于统一处理各个code代表的问题
     *
     * @param resultBean
     * @param onCallListener
     */
    private void dealResponse(BaseBean resultBean, OnCallListener onCallListener) {
        if (resultBean == null || resultBean.code == null) {
            return;
        }
        int code = Integer.valueOf(resultBean.code);
        switch (code) {
            case 0:
                if (onCallListener != null) {
                    onCallListener.onSuccess(resultBean);
                }
                break;
            case 99://未登录或没登录信息失效
                break;
            case 5:
                break;
            default:
                break;

        }
    }

    public interface OnCallListener {

        void onSuccess(BaseBean result);

        void onError(String message);

    }
}
