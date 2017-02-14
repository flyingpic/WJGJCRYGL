package com.elane.wjgjcrygl.retrofit.finger_compare.response;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * 用户角色返回body
 * Created by SmileXie on 16/7/15.
 */
@Root(name = "s:Body")
public class ResponseBody {
    @Element(name = "GetZYRYBHFromFingerprintResponse")
    public GetZYRYBHFromFingerprintResponse getZYRYBHFromFingerprintResponse;

}
