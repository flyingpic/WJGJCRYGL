package com.elane.wjgjcrygl.retrofit.finger_compare.request;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;


/**
 * 用户角色请求Envelope
 * Created by SmileXie on 16/7/15.
 */
@Root(name = "Envelope")
@Namespace(reference = "http://schemas.xmlsoap.org/soap/envelope/", prefix = "s")
public class RequestEnvelope {
    @Element(name = "s:Body") public RequestBody requestBodyNode;
}
