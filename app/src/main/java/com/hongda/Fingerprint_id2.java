package com.hongda;

public class Fingerprint_id2 {
	
	static {
		System.loadLibrary("fingerprint_id2");
	}
	
	public native static int FP_GetVersion(byte[] code);
	public native static int FP_Begin();
	public native static int FP_End();
	public native static int FP_FeatureExtract(byte cScannerType,
				byte cFingerCode,
				byte[] pFingerImgBuf,
				byte[] pFeatureData);
	public native static int FP_FeatureMatch(byte[] pFeatureData1,
				byte[] pFeatureData2,
				float[] pfSimilarity);
	/**
	 * 指纹图片质量函数，返�?1为成功，否则为失�?
	 * pFingerImgBuf指纹图片数据，pnScore为输出参数，pnScore[0]的�?�为0~100，�?�数越高说明指纹图片越好�?
	 * 可以判断当前图片中是否有可用的指纹，�?值可以自行根本自己方的应用进行测试，并设定一个合理的值，
	 * 我方建议�?30�?40左右，高于它指定有指纹，低于它认定无指纹
	 */
	public native static int FP_GetQualityScore(byte[] pFingerImgBuf, byte[] pnScore);
	
}
