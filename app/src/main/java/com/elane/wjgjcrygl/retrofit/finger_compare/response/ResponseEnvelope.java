package com.elane.wjgjcrygl.retrofit.finger_compare.response;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

/**
 * 用户角色返回总信息
 * Created by SmileXie on 16/7/15.
 */
@Root(name = "s:Envelope")
@Namespace(reference = "http://schemas.xmlsoap.org/soap/envelope/", prefix = "s")
public class ResponseEnvelope {
    @Element(name = "Body")
    public ResponseBody body;

}
