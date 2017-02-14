package com.elane.wjgjcrygl.retrofit.person_info.response;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by zgbf on 2016/12/1.
 */
@Root(name = "SERVICE")
public class WebServiceService {
    @Element(name = "RESULT") public WebServiceResult resultNode;
}
