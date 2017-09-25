package com.phicomm.remotecontrol.request;

import com.phicomm.remotecontrol.modules.personal.apply.ApplyInfosBean;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by hao04.wu on 2017/9/18.
 */

public interface HttpAppInfosService {
    @GET("applist")
    Observable<ApplyInfosBean> getAppInfos();

    //    @FormUrlEncoded 表单形式提交
    @Headers("Content-Type:application/json;charset=UTF-8")//json形式提交
    @POST("application")
    Observable<ApplyInfosBean> openApplication(@Body RequestBody body);

}
