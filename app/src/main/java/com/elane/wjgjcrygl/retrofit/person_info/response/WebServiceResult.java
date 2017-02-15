package com.elane.wjgjcrygl.retrofit.person_info.response;

import com.elane.wjgjcrygl.model.OfficerInfo;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by zgbf on 2016/12/1.
 */
@Root(name = "RESULT")
public class WebServiceResult {
    @Attribute(name = "STATUS",required = false) public String status;

    @Attribute(name = "MSG",required = false,empty = "") public String msg;
    @Element(name = "DATA",required = false) public OfficerInfo dataNode;
}
