package com.elane.wjgjcrygl;

public class Global {

	public final static String EXIT_CODE = "67887778";
	
	/* ----------------- 常量 ------------------- */
	/** NAMESPACE web service名称空间  */
	public static String NAMESPACE = "http://EUDAP.elane.cn";
	/**  METHODNAME web service方法名称  */
	public static String METHODNAME = "ResolveXml";
	/**  XML的版本号  */
	public static final String XMLVersionTag = "<?xml version=\"1.0\" encoding=\"utf-8\"?>";

	public final static String ACTION_CLOSE_LISTENSERVICE="close_listenservice"; // 关闭守护进程
	
	//江看ip=10.155.22.236
	public static String oracle_ip = "10.155.22.236";//oracle数据库ip  10.155.22.236
	public static String oracle_port = "1521";//oracle数据库port  1521
	//江看sid = orcl
	public static String oracle_sid = "fxgl";//oracle数据库sid  orcl
	public static String oracle_username = "ECMIS_WJG";//oracle数据库用户名  ECMIS_IIT
	public static String oracle_password = "ECMIS@WJG";//oracle数据库密�?  ecmis_i.it
	public static String servlet_url = "http://192.168.4.41:8888/web.iit/servlet/ABDoorServlet";  //http://10.155.22.236:8080/web.iit/servlet/ABDoorServlet

	public static String FingerprintUrl = "http://192.168.4.236:8732/XMLFingerprintMatch/FingerprintMatch/?wsdl";
	public static String FingerprintUserName = "520000111";  //指纹匹配用户�?   500105111
	public static String FingerprintPassWord = "Vs7alZLAMps=";  //指纹匹配密码  31AbRLiuIRlmnN2X9JYI4A==
	
	//统一访问
	public static String eudap_wsdl = "http://192.168.4.229:9009/EUDAPM/web/";//http://10.154.12.78:9009/EUDAPM/web/EUDAP?
	public static String eudap_lisence = "K2zMnha3X2IbU9j+puXZI4SmZwNw==";//1cH71XMCgvoTtwHO7y1d20csnCRrWGgn7eidLUbW/vTQ==
	
	public static String socket_ip="10.155.34.59";
	public static int socket_duankou=22555;

	public static final int WHAT_GET_LEADER_INFO = 1001;
	public static final int WHAT_SUBMIT_LEADER_INFO = 1002;
	public static final int WHAT_GET_HTTP_GET = 1003;

	public static final String TABLE_NAME_PERSION_INFO = "Table_PersonInfo";
	
	public static String rybh = "";
	public static String jinOrchu="2";
	public static String jsbh="520000111";//500105111

	public static boolean isParamsNull(){
		/*if(oracle_ip == null || oracle_ip.equals(""))return false;
		if(oracle_port == null || oracle_port.equals(""))return false;
		if(oracle_sid == null || oracle_sid.equals(""))return false;
		if(oracle_username == null || oracle_username.equals(""))return false;
		if(oracle_password == null || oracle_password.equals(""))return false;
		
		if(servlet_url == null || servlet_url.equals(""))return false;*/
		
		if(FingerprintUrl == null || FingerprintUrl.equals(""))return false;
		if(FingerprintUserName == null || FingerprintUserName.equals(""))return false;
		if(FingerprintPassWord == null || FingerprintPassWord.equals(""))return false;
		
		if(eudap_wsdl == null || eudap_wsdl.equals(""))return false;
		if(eudap_lisence == null || eudap_lisence.equals(""))return false;
		return true;
	}
	
}
