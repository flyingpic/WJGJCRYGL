package com.elane.wjgjcrygl.model;

/**
 * 人员信息
 *
 * create by ZG
 *
 * date: 2017/02/09
 *
 * */
public class PersonInfo {
	private String job;//职务
	private String xm;//姓名
	private byte[] zpsj;//照片数据
	private String zjhm;//证件号码


	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
	}

	public String getXm() {
		return xm;
	}

	public void setXm(String xm) {
		this.xm = xm;
	}

	public byte[] getZpsj() {
		return zpsj;
	}

	public void setZpsj(byte[] zpsj) {
		this.zpsj = zpsj;
	}

	public String getZjhm() {
		return zjhm;
	}

	public void setZjhm(String zjhm) {
		this.zjhm = zjhm;
	}
}
