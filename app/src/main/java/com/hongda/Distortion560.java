package com.hongda;

public class Distortion560 {
	public native static boolean InitAB();// ��ʼ��

	public native static void UninitAB();// �����ͷ�
//1.���2.ԭ����3.ԭͼ��4.ԭͼ��
	public native static void ReviseImgDat(byte[] pBuf, byte[] pOrgBuf,
			int OrgW, int OrgH);
	
	static {
		System.loadLibrary("distortion560");
	}
}
