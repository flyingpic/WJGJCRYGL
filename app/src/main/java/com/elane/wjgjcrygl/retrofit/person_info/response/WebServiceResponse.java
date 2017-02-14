package com.elane.wjgjcrygl.retrofit.person_info.response;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by zgbf on 2016/12/1.
 */
@Root(name = "WS")
public class WebServiceResponse {
    @Element(name = "SERVICE") public WebServiceService serviceNode;
}
