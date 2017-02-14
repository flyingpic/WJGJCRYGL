package com.elane.wjgjcrygl.model;

public class Passwords {
	private String zcpwd;
	private String fjcpwd;
	private String tcpwd;
	
	
	
	public String getTcpwd() {
		return tcpwd;
	}
	public void setTcpwd(String tcpwd) {
		this.tcpwd = tcpwd;
	}
	public String getZcpwd() {
		return zcpwd;
	}
	public void setZcpwd(String zcpwd) {
		this.zcpwd = zcpwd;
	}
	public String getFjcpwd() {
		return fjcpwd;
	}
	public void setFjcpwd(String fjcpwd) {
		this.fjcpwd = fjcpwd;
	}

	@Override
	public String toString() {
		return "Passwords:"+zcpwd+","+fjcpwd+","+tcpwd;
	}
}
