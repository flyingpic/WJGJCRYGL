package com.hongda;

import android.graphics.Bitmap;

public class Distortion3000 {
	public native static boolean InitAB();// ��ʼ������

	public native static void UninitAB();// �ͷź���
	/**
	 * ���δ�����
	 * @param pBuf ԭʼͼ������
	 * @param pOrgBuf ���ͼ������
	 * @param OrgW ԭʼͼ����
	 * @param OrgH ԭʼͼ��߶�
	 */
	public native static void ReviseImgDat(byte[] pBuf, byte[] pOrgBuf,
			int OrgW, int OrgH);
	
	public native static void pixeltobmp(byte[] src, Bitmap bitmap);
	
	static {
		System.loadLibrary("distortion3000");
	}
}
