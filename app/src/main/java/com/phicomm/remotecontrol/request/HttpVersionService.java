package com.phicomm.remotecontrol.request;


import com.phicomm.remotecontrol.modules.personal.upgrade.UpdateInfoResponseBean;

import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by hao04.wu on 2017/7/4.
 */

public interface HttpVersionService {

    @GET("Service/App/checkupdate")
    Observable<UpdateInfoResponseBean> checkVersion(@QueryMap Map<String, String> options);
}
