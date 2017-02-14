package com.elane.wjgjcrygl.retrofit.person_info.request;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * 用户角色返回body
 * Created by SmileXie on 16/7/15.
 */
@Root(name = "soapenv:Body")
public class RequestBody {
    @Element(name = "eud:ResolveXml") public RequestXml requestXml;
}
