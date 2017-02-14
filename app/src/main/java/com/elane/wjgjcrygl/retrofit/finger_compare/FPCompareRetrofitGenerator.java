package com.elane.wjgjcrygl.retrofit.finger_compare;

import android.util.Log;

import com.elane.wjgjcrygl.MyApplication;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.convert.AnnotationStrategy;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.strategy.Strategy;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

/**
 * Retrofit变量初始化
 * Created by SmileXie on 16/7/16.
 */
public class FPCompareRetrofitGenerator {
    public static WebserviceInterfaceApi webserviceInterfaceApi;

    private static Strategy strategy = new AnnotationStrategy();
    private static Serializer serializer = new Persister(strategy);

    private static OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder();

    private static Retrofit.Builder retrofitBuilder =  new Retrofit.Builder()
            .addConverterFactory(SimpleXmlConverterFactory.create(serializer))
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .baseUrl(MyApplication.FINGER_COMPARE_BASE_URL);

    public static <S> S createService(Class<S> serviceClass) {
        okHttpClient.interceptors().add(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request original = chain.request();

                Request.Builder requestBuilder = original.newBuilder()
//                        .header("Content-Type", "text/xml; charset=utf-8")   // 对于SOAP 1.1， 如果是soap1.2 应是Content-Type: application/soap+xml; charset=utf-8
                        .header("SOAPAction","http://tempuri.org/IFingerprintMatch/GetZYRYBHFromFingerprint")
                        .method(original.method(), original.body());

                Request request = requestBuilder.build();
                Log.d("test", "intercept: "+request.body().toString());
                return chain.proceed(request);
            }
        });

        OkHttpClient client = okHttpClient.connectTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(2, TimeUnit.MINUTES)
                .readTimeout(2, TimeUnit.MINUTES)
                .build();
        Retrofit retrofit = retrofitBuilder.client(client).build();
        return retrofit.create(serviceClass);
    }

    public static WebserviceInterfaceApi getWebserviceInterfaceApi() {
        if(webserviceInterfaceApi == null) {
            webserviceInterfaceApi = createService(WebserviceInterfaceApi.class);
        }
        return webserviceInterfaceApi;
    }
}
