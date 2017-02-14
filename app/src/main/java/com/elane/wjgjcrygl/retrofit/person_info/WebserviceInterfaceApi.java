package com.elane.wjgjcrygl.retrofit.person_info;

import com.elane.wjgjcrygl.retrofit.person_info.request.RequestEnvelope;
import com.elane.wjgjcrygl.retrofit.person_info.response.ResponseEnvelope;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * 接口请求
 * Created by SmileXie on 16/7/15.
 */
public interface WebserviceInterfaceApi {

    @POST("EUDAP?wsdl")
    Observable<ResponseEnvelope> webRequestPersonInfo(@Body RequestEnvelope requestEnvelope);
}
