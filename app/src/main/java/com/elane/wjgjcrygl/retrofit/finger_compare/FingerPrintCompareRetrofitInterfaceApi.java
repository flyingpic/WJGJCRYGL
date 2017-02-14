package com.elane.wjgjcrygl.retrofit.finger_compare;


import com.elane.wjgjcrygl.model.FingerPrintRequestBody;
import com.elane.wjgjcrygl.retrofit.finger_compare.response.ResponseEnvelope;

import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import rx.Observable;

/**
 * 接口请求
 * Created by SmileXie on 16/7/15.
 */
public interface FingerPrintCompareRetrofitInterfaceApi {

    @Headers("Content-Type: text/xml; charset=utf-8")
    @POST("?wsdl")
    Observable<ResponseEnvelope> webserviceRequest(@Body FingerPrintRequestBody requestEnvelope);

}
