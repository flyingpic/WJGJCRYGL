package com.elane.wjgjcrygl.retrofit.person_info.response;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

/**
 * 用户角色返回总信息
 * Created by SmileXie on 16/7/15.
 */
@Root(name = "soap:Envelope")
@Namespace(reference = "http://schemas.xmlsoap.org/soap/envelope/", prefix = "soap")
public class ResponseEnvelope {
    @Element(name = "Body")
    public ResponseBody body;

}
