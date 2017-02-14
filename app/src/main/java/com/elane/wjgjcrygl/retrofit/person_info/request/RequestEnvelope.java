package com.elane.wjgjcrygl.retrofit.person_info.request;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.NamespaceList;
import org.simpleframework.xml.Root;


/**
 * 用户角色请求Envelope
 * Created by SmileXie on 16/7/15.
 */
@Root(name = "soapenv:Envelope")
@NamespaceList({
        @Namespace(reference = "http://EUDAP.elane.cn", prefix = "eud"),
        @Namespace(reference = "http://schemas.xmlsoap.org/soap/envelope/", prefix = "soapenv")
})
public class RequestEnvelope {
    @Element(name = "soapenv:Body") public RequestBody requestBodyNode;
}
