package com.elane.wjgjcrygl.retrofit.person_info.response;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

/**
 * 用户角色返回body
 * Created by SmileXie on 16/7/15.
 */
@Root(name = "Body")
@Namespace(reference = "http://EUDAP.elane.cn", prefix = "ns1")
public class ResponseBody {
    @Element(name = "ResolveXmlResponse")
    public ResponseReturn returnNode;

}
