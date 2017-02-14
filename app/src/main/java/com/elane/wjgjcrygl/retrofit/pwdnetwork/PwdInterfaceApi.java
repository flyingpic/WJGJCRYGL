package com.elane.wjgjcrygl.retrofit.pwdnetwork;

import com.elane.wjgjcrygl.model.Passwords;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/***
 * retrofit interface for request verify password
 * create by zg  2017/01/22
 */
public interface PwdInterfaceApi {

    /**
     * request 验证密码
     * @param username 数据库用户名
     * @param password 数据库密码
     * @param sid 数据库SID
     * @param ip 数据库IP
     * @param port 数据库Port
     * @return
     */
    @FormUrlEncoded
    @POST("ABDoorServlet")
    Observable<Passwords> verifyPwd(@Field("username") String username,
                                    @Field("password") String password,
                                    @Field("sid") String sid,
                                    @Field("ip") String ip,
                                    @Field("port") String port);
}
