package com.phicomm.remotecontrol.modules.personal.account.http;

import android.util.Log;

import com.phicomm.remotecontrol.constant.HttpConfig;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by yong04.zhou on 2017/9/13.
 */

public class HttpDataRepository {

    public static final int CONNECT_TIMEOUT = 15;

    private static HttpDataRepository mInstance = null;
    private Retrofit mCommonRetrofit = null;
    private HttpCloudInterface mHttpCloudInterface = null;

    //http log
    class HttpLogger implements HttpLoggingInterceptor.Logger {
        @Override
        public void log(String message) {
            Log.d("OkHttpLogInfo", message);
        }
    }

    public HttpDataRepository() {

        //log
        HttpLoggingInterceptor okhttpLoggingInterceptor = new HttpLoggingInterceptor(new HttpLogger());
        okhttpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);


        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
        okHttpClientBuilder.connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .addNetworkInterceptor(okhttpLoggingInterceptor);

        mCommonRetrofit = new Retrofit.Builder().baseUrl(HttpConfig.PHICOMM_CLOUD_BASE_URL)
                .client(okHttpClientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        mHttpCloudInterface = mCommonRetrofit.create(HttpCloudInterface.class);
    }

    public static HttpDataRepository getInstance() {
        if (mInstance == null) {
            mInstance = new HttpDataRepository();
        }
        return mInstance;
    }

    public Subscription getAuthorization(Observer subscriber, Map<String, String> map) {
        return mHttpCloudInterface.authorization(map)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);

    }

    public Subscription login(Observer subscriber, RequestBody body) {
        return mHttpCloudInterface.login(body)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);

    }


    public Subscription captcha(Observer subscriber, Map<String, String> map) {
        return mHttpCloudInterface.captcha(map)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);

    }

    public Subscription checkPhoneNumber(Observer subscriber, Map<String, String> map) {
        return mHttpCloudInterface.checkPhonenumber(map)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public Subscription verifyCode(Observer subscriber, Map<String, String> map) {
        return mHttpCloudInterface.verifyCodeCaptcha(map)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }


    public Subscription register(Observer subscriber, RequestBody body) {
        return mHttpCloudInterface.register(body)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public Subscription forgetPassword(Observer subscriber, RequestBody body) {
        return mHttpCloudInterface.forgetPassword(body)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);

    }

    public Subscription refreshToken(Observer subscriber, String refreshToken,
                                     Map<String, String> map) {
        return mHttpCloudInterface.refreshToken(refreshToken, map)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * accountDetail
     *
     * @param subscriber
     * @param accessToken
     */

    public Subscription accountDetail(Observer subscriber,
                                      String accessToken) {
        return mHttpCloudInterface.accountDetail(accessToken)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

}
