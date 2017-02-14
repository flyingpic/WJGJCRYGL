package com.elane.wjgjcrygl.retrofit.person_info.response;

import com.elane.wjgjcrygl.model.OfficerInfo;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by zgbf on 2016/12/1.
 */

@Root(name = "RESULT")
public class WebServiceDateList {

    @Attribute(name = "STATUS") public String status;

    @Attribute(name = "MSG") public String msg;

    @ElementList(name = "DATA_LIST") public List<OfficerInfo> dataListNode;
}
