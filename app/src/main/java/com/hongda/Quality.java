/**
 * 
 */
package com.hongda;
/**
 * Description:指纹质量
 * @author zg
 * @Date   2015/7/31
 * @Company:重庆易联软件
 */
public class Quality {
	public native static int GetQualityScore(byte[] pFingerImgBuf, byte[] pnScore);
	static{
		System.loadLibrary("fingerprint_id2");
	}
}
