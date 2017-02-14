package com.elane.wjgjcrygl.retrofit.finger_compare.request;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * 用户角色返回body
 * Created by SmileXie on 16/7/15.
 */
@Root(name = "s:Body")
public class RequestBody {
    @Element(name = "GetZYRYBHFromFingerprint") public GetZYRYBHFromFingerprint getZYRYBHFromFingerprint;
}
