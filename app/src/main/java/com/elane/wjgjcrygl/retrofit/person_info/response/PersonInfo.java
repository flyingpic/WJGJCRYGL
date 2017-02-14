package com.elane.wjgjcrygl.retrofit.person_info.response;

import org.simpleframework.xml.Element;

/**
 * Created by zgbf on 2016/12/1.
 */

@Element(name = "DATA")
public class PersonInfo {
    @Element(name = "RYBH") public String rybh;//人员编号
    @Element(name = "XM") public String xm;//姓名
    @Element(name = "ZPSJ") public String zpsj;//照片数据
    @Element(name = "PWH") public String pwh;//铺位号
    @Element(name = "WZJSYY") public String wzjsyy;//未在监室原因
}
