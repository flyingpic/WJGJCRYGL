package com.elane.wjgjcrygl.retrofit.person_info.response;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by zgbf on 2016/12/1.
 */
@Root(name = "RESULT")
public class WebServiceResult {

    @Attribute(name = "NAME") public String serviceName;

    @Element(name = "DATA_LIST") public WebServiceDateList dateListNode;
}
