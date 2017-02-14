package com.elane.wjgjcrygl.retrofit.finger_compare.response;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

/**
 * Created by zgbf on 2017/1/17.
 */

@Root(name = "GetZYRYBHFromFingerprintResponse")
@Namespace(reference = "http://tempuri.org/")
public class GetZYRYBHFromFingerprintResponse {
    @Element(name = "GetZYRYBHFromFingerprintResult") public String GetZYRYBHFromFingerprintResult;
}
