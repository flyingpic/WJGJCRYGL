package com.elane.wjgjcrygl.retrofit.finger_compare.request;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

/**
 * Created by zgbf on 2017/1/17.
 */
@Root(name = "GetZYRYBHFromFingerprint")
@Namespace(reference = "http://tempuri.org/")
public class GetZYRYBHFromFingerprint {
    @Element(name = "base64imagefinger",required = true) public String base64imagefinger;
    @Element(name = "username",required = true) public String username;
    @Element(name = "password",required = true) public String password;
}
