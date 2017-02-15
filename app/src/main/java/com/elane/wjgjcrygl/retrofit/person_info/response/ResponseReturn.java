package com.elane.wjgjcrygl.retrofit.person_info.response;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by zgbf on 2016/11/30.
 *
 * Response:
 *
 * <soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
 * <soap:Body>
 * <ns1:ResolveXmlResponse xmlns:ns1="http://EUDAP.elane.cn">
 * <return><![CDATA[<?xml version="1.0" encoding="UTF-8" standalone="no"?><WS><SERVICE NAME=""><RESULT STATUS="-1" MSG="xml转换出错，请检查xml或者参数,Error on line 1: Content is not allowed in prolog."></RESULT></SERVICE></WS>]]></return>
 * </ns1:ResolveXmlResponse>
 * </soap:Body>
 * </soap:Envelope>
 */

@Root(name = "ResolveXmlResponse")
public class ResponseReturn {
    @Element(name = "return",required = false) public String result;
}
