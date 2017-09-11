package com.phicomm.remotecontrol.request;


import com.phicomm.remotecontrol.beans.ResultBean;

import java.util.Map;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;
import retrofit.http.QueryMap;
import rx.Observable;

/**
 * Created by hao04.wu on 2017/7/4.
 */

public interface RequestService {


    @GET("order/list")
    Observable<ResultBean> getResult(@QueryMap Map<String, String> options);

    @GET("order/list")
    Call<ResultBean> getResult1(@QueryMap Map<String, String> options);

    @POST("order/list")
    Observable<ResultBean> postResult(@QueryMap Map<String, String> options);

}
