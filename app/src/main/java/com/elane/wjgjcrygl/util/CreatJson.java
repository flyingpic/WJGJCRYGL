package com.elane.wjgjcrygl.util;

import org.json.JSONException;
import org.json.JSONObject;


import com.elane.wjgjcrygl.model.Passwords;

public class CreatJson {
	public static JSONObject creatJson(String intoutype, String rybh,
			String InOutFlag) {
		// json组装
		JSONObject jsonObj_xinxi = new JSONObject();//
		try {
			jsonObj_xinxi.put("UserID", rybh);
			jsonObj_xinxi.put("InOutType", intoutype);
			jsonObj_xinxi.put("InOutFlag", InOutFlag);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return jsonObj_xinxi;

	}

	public static JSONObject creatJsonpwd(String intoutype, String PWDType,
			String InOutFlag) {
		// json组装
		JSONObject jsonObj_xinxi = new JSONObject();//
		try {
			jsonObj_xinxi.put("PWDType", PWDType);
			jsonObj_xinxi.put("InOutType", intoutype);
			jsonObj_xinxi.put("InOutFlag", InOutFlag);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return jsonObj_xinxi;

	}

	public static Passwords AnalyzeJSION(String json) {
		Passwords pwd = new Passwords();
		JSONObject jsonobj;
		try {
			jsonobj = new JSONObject(json);
			String zcpwd = jsonobj.getString("zcpwd");
			pwd.setZcpwd(zcpwd);
			String fjcpwd = jsonobj.getString("fjcpwd");
			pwd.setFjcpwd(fjcpwd);
			String tcpwd = jsonobj.getString("tcpwd");
			pwd.setTcpwd(tcpwd);;
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return pwd;
	}
}
