package com.phicomm.remotecontrol.httpclient;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.phicomm.remotecontrol.util.LogUtil;
import com.phicomm.remotecontrol.beans.KeyEvent;
import com.phicomm.remotecontrol.beans.Status;
import com.phicomm.remotecontrol.constant.ErrorCode;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by xufeng02.zhou on 2017/7/18.
 */

public class RemoteServiceImpl implements IRemoterService {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    String mRootPath;
    OkHttpClient mOkHttpClient;

    public RemoteServiceImpl(String ip, int port) {
        mRootPath = "http://" + ip + ":" + port + "/";
        mOkHttpClient = new OkHttpClient();
    }

    @Override
    public Observable<Status> getStatus() {
        return Observable.create(new Observable.OnSubscribe<Status>() {

            @Override
            public void call(final Subscriber<? super Status> subscriber) {
                LogUtil.d("call getStatus");
                try {
                    String tgtPath = mRootPath + "v1/status";
                    Gson gson = new Gson();

                    Request request = new Request.Builder()
                            .url(tgtPath)
                            .build();
                    Response response = mOkHttpClient.newCall(request).execute();
                    int code = response.code();
                    if (ErrorCode.OK == code) {
                        LogUtil.d("getStatus OK");
                        ResponseBody body = response.body();
                        Status status = gson.fromJson(body.string(), Status.class);
                        subscriber.onNext(status);
                    } else {
                        LogUtil.w("Error:" + code + "  " + response.body().string());
                        subscriber.onError(new Exception("Error"));
                    }
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<String> ping() {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(final Subscriber<? super String> subscriber) {

                try {
                    String tgtPath = mRootPath + "v1/ping";
                    Request request = new Request.Builder()
                            .url(tgtPath)
                            .build();
                    Response response = mOkHttpClient.newCall(request).execute();
                    int code = response.code();
                    if (ErrorCode.OK == code) {
                        ResponseBody body = response.body();
                        subscriber.onNext(body.string());
                    } else {
                        LogUtil.w("Error:" + code + "  " + response.body().string());
                        subscriber.onError(new Exception("error"));
                    }
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<String> sendKeyEvent(final KeyEvent keyEvent) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(final Subscriber<? super String> subscriber) {
                LogUtil.d("call sendKeyEvent");
                try {
                    String tgtPath = mRootPath + "v1/keyevent";
                    Gson gson = new Gson();
                    RequestBody requestBody = RequestBody.create(JSON, gson.toJson(keyEvent));

                    Request request = new Request.Builder()
                            .url(tgtPath)
                            .post(requestBody)
                            .build();
                    Response response = mOkHttpClient.newCall(request).execute();

                    int code = response.code();
                    if (ErrorCode.OK == code) {
                        LogUtil.d("sendKeyEvent OK");
                        ResponseBody body = response.body();
                        subscriber.onNext(body.string());
                    } else {
                        LogUtil.w("Error:" + code + "  " + response.body().string());
                        subscriber.onError(new Exception("error"));
                    }
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<String> sendCommand(final String action) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(final Subscriber<? super String> subscriber) {
                try {
                    String tgtPath = mRootPath + "v1/action";
                    JsonObject object = new JsonObject();
                    object.addProperty("action", action);
                    RequestBody requestBody = RequestBody.create(JSON, object.toString());

                    Request request = new Request.Builder()
                            .url(tgtPath)
                            .post(requestBody)
                            .build();
                    Response response = mOkHttpClient.newCall(request).execute();

                    int code = response.code();
                    if (ErrorCode.OK == code) {
                        ResponseBody body = response.body();
                        subscriber.onNext(body.string());
                    } else {
                        LogUtil.w("Error:" + code + "  " + response.body().string());
                        subscriber.onError(new Exception("error"));
                    }
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<byte[]> doScreenshot() {
        return Observable.create(new Observable.OnSubscribe<byte[]>() {
            @Override
            public void call(final Subscriber<? super byte[]> subscriber) {
                try {
                    String tgtPath = mRootPath + "v1/screenshot";
                    Request request = new Request.Builder()
                            .url(tgtPath)
                            .build();
                    Response response = mOkHttpClient.newCall(request).execute();

                    int code = response.code();
                    if (ErrorCode.OK == code) {
                        byte[] picByte = response.body().bytes();
                        subscriber.onNext(picByte);
                    } else {
                        LogUtil.w("Error:" + code + "  " + response.body().string());
                        subscriber.onError(new Exception("error"));
                    }
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }
}
