package com.elane.wjgjcrygl.model;

import org.simpleframework.xml.Element;

/**
 * Created by zgbf on 2017/2/10.
 */

public class OfficerInfo {
    @Element(name = "TYPE",required = false) public String type;
    @Element(name = "CODE") public String zjhm;//证件号码
    @Element(name = "BASE64IMAGE",required = false) public String zpsj;//照片数据
    @Element(name = "DWBM",required = false) public String jsbh;//监所编号
    @Element(name = "XM",required = false) public String xm;//姓名
    @Element(name = "ZWBM",required = false) public String job;//职务
    @Element(name = "DWMC",required = false) public String dwmc;//单位名称
    @Element(name = "XBMC",required = false) public String xb;//性别

    @Override
    public String toString() {
        return "OfficerInfo: Type="+type+",CODE="+zjhm+",ZPSJ="+zpsj+",JSBH="+jsbh+",XM="+xm+",ZWBM="+job+",DWMC="+dwmc+",XBMC"+xb;
    }
}
