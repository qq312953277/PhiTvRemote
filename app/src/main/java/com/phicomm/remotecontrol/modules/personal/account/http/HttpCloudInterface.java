package com.phicomm.remotecontrol.modules.personal.account.http;

import com.phicomm.remotecontrol.modules.personal.account.resultbean.AccountDetailBean;
import com.phicomm.remotecontrol.modules.personal.account.resultbean.AuthorizationResponseBean;
import com.phicomm.remotecontrol.modules.personal.account.resultbean.CaptchaResponseBean;
import com.phicomm.remotecontrol.modules.personal.account.resultbean.CheckphonenumberResponseBean;
import com.phicomm.remotecontrol.modules.personal.account.resultbean.ForgetpasswordResponseBean;
import com.phicomm.remotecontrol.modules.personal.account.resultbean.LoginResponseBean;
import com.phicomm.remotecontrol.modules.personal.account.resultbean.RegisterResponseBean;
import com.phicomm.remotecontrol.modules.personal.account.resultbean.TokenUpdateBean;
import com.phicomm.remotecontrol.modules.personal.account.resultbean.VerifycodeResponseBean;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by yong04.zhou on 2017/9/13.
 */

public interface HttpCloudInterface {

    @Headers("Content-Type:application/x-www-form-urlencoded")
    @GET("authorization")
    Observable<AuthorizationResponseBean> authorization(@QueryMap Map<String, String> map);


    @Headers("Content-Type:application/x-www-form-urlencoded")
    @POST("login")
    Observable<LoginResponseBean> login(@Body RequestBody body);  //RequestBody待提交的参数 POST请求必须

    @Headers("Content-Type:application/x-www-form-urlencoded")
    @POST("expresslogin")
    Observable<LoginResponseBean> expressLogin(@Body RequestBody body);

    @Headers("Content-Type:application/x-www-form-urlencoded")
    @GET("captcha")
    Observable<CaptchaResponseBean> captcha(@QueryMap Map<String, String> map);

    @Headers("Content-Type:application/x-www-form-urlencoded")
    @GET("checkPhonenumber")
    Observable<CheckphonenumberResponseBean> checkPhonenumber(@QueryMap Map<String, String> map);

    @Headers("Content-Type:application/x-www-form-urlencoded")
    @GET("verificationMsg")
    Observable<VerifycodeResponseBean> verifyCodeCaptcha(@QueryMap Map<String, String> map);

    @Headers("Content-Type:application/x-www-form-urlencoded")
    @GET("verificationCode")
    Observable<VerifycodeResponseBean> verifyCodeWithNoCaptcha(@QueryMap Map<String, String> map);

    @Headers("Content-Type:application/x-www-form-urlencoded")
    @POST("account")
    Observable<RegisterResponseBean> register(@Body RequestBody body);  //RequestBody待提交的参数 POST请求必须

    @POST("forgetpassword")
    Observable<ForgetpasswordResponseBean> forgetPassword(@Body RequestBody body);

    @Headers("Content-Type:application/x-www-form-urlencoded")
    @GET("token")
    Observable<TokenUpdateBean> refreshToken(@Header("Authorization") String access_token, @QueryMap Map<String, String> map);


    @Headers("Content-Type:application/x-www-form-urlencoded")
    @GET("accountDetail")
    Observable<AccountDetailBean> accountDetail(@Header("Authorization") String access_token);
}
