package com.elane.wjgjcrygl.retrofit.person_info.response;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;

/**
 * Created by zgbf on 2016/12/1.
 */
@Root(name = "return",strict = false)
public class WebServiceWS {
    @Path("Envelope/Body/ResolveXmlResponse/return/WS")
    @Element(name = "WS",required = false) public WebServiceResponse wsNode;
}
