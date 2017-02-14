package com.elane.wjgjcrygl.model;

import org.simpleframework.xml.Element;

/**
 * Created by zgbf on 2017/2/10.
 */

public class OfficerInfo {
    @Element(name = "TYPE") public String type;
    @Element(name = "CODE") public String zjhm;//证件号码
    @Element(name = "BASE64IMAGE") public String zpsj;//照片数据
    @Element(name = "DWBM") public String jsbh;//监所编号
    @Element(name = "XM") public String xm;//姓名
    @Element(name = "ZWBM") public String job;//职务
    @Element(name = "DWMC") public String dwmc;//单位名称
    @Element(name = "XBMC") public String xb;//性别
}
