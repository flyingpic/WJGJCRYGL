package com.elane.wjgjcrygl.retrofit.person_info.request;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by zgbf on 2016/11/30.
 *
 * Request:
 *
 * <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:eud="http://EUDAP.elane.cn">
 * <soapenv:Header/>
 * <soapenv:Body>
 * <eud:ResolveXml>
 * <xml>?</xml>
 * </eud:ResolveXml>
 * </soapenv:Body>
 * </soapenv:Envelope>
 *
 *
 */
@Root(name = "eud:ResolveXml")
public class RequestXml {
    @Element(name = "xml") public String xml;
}
