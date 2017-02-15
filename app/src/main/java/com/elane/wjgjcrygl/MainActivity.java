package com.elane.wjgjcrygl;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.elane.wjgjcrygl.model.OfficerInfo;
import com.elane.wjgjcrygl.model.Passwords;
import com.elane.wjgjcrygl.retrofit.person_info.PersonInfoRetrofitGenerator;
import com.elane.wjgjcrygl.retrofit.person_info.request.RequestEnvelope;
import com.elane.wjgjcrygl.retrofit.person_info.request.RequestXml;
import com.elane.wjgjcrygl.retrofit.person_info.response.ResponseEnvelope;
import com.elane.wjgjcrygl.retrofit.person_info.response.WebServiceResponse;
import com.elane.wjgjcrygl.retrofit.pwdnetwork.RetrofitGenerator;
import com.elane.wjgjcrygl.util.SQLiteOperation;
import com.elane.wjgjcrygl.util.StringUtil;
import com.elane.wjgjcrygl.util.Ws_Zw;
import com.elane.wjgjcrygl.view.KeyboardView;
import com.elane.wjgjcrygl.view.PasswordEditText;
import com.hongda.Distortion3000;

import org.simpleframework.xml.core.Persister;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;
import dmax.dialog.SpotsDialog;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static rx.Observable.interval;

public class MainActivity extends Activity implements SurfaceHolder.Callback,
		Camera.ShutterCallback, Camera.PictureCallback,
		Camera.AutoFocusCallback {

	private final static String TAG = "MainActivity";

	private static final String[] KEY = new String[] {
			"1", "2", "3",
			"4", "5", "6",
			"7", "8", "9",
			"<<", "0", "完成"
	};

	// 匹配指纹进度框
	private AlertDialog mLoadingDialog;

	private Context mContext;

	private Camera mCamera;

	private boolean isFPMatching = false;

	private int rollback2ScreenProtectorTime = 15;// 初始时间

	private SQLiteDatabase mSqLiteDatabase;

	private Subscription screenControlObservable ;
	private Subscription authenticationObservable;

	@BindView(R.id.surfaceview_activity_main) SurfaceView mSurfaceView;
	@BindView(R.id.iv_screen_protector) ImageView iv_screen_protector;
	@BindView(R.id.iv_close) ImageView iv_exit;
	@BindView(R.id.password) PasswordEditText passwordView;
	@BindView(R.id.keyboard) KeyboardView keyboardView;

	@OnTouch(R.id.iv_screen_protector) boolean onTouch(){
		Log.d(TAG, "onTouch: ");
		rollback2ScreenProtectorTime = 15;
		inputContent = new StringBuffer();
		passwordView.setText(inputContent.toString());
		screenViewToggle(false);
		return false;
	}

	@OnClick(R.id.iv_close) void onClose(){
		finish();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

		setContentView(R.layout.activity_main);

		ButterKnife.bind(this);

		mContext = this;

		mSqLiteDatabase = new SQLiteOperation(mContext.getApplicationContext(),"DATABASE",1).getReadableDatabase();

		initViewAddEvent();

		//查询密码并缓存本地
		RetrofitGenerator.getPwdInterfaceApi()
				.verifyPwd(MyApplication.oracle_username,MyApplication.oracle_password,MyApplication.oracle_sid,MyApplication.oracle_ip,MyApplication.oracle_port)
				.subscribeOn(Schedulers.newThread())
				.observeOn(Schedulers.newThread())
				.map(new Func1<Passwords, Passwords>() {
					@Override
					public Passwords call(Passwords passwords) {
						Log.d(TAG, "得到密码: "+passwords.toString());
						Passwords p = null;
						if(passwords!=null){

						}
						return p;
					}

				})
				.subscribe(new Subscriber<Passwords>() {
					@Override
					public void onCompleted() {
						Log.d(TAG, "onCompleted: ");
					}

					@Override
					public void onError(Throwable e) {
						Log.d(TAG, "onError: "+e.getMessage());
						// TODO: 2017/1/22 停止接收密码

					}

					@Override
					public void onNext(Passwords passwords) {
						Log.d(TAG, "onNext: ");
					}
				});

		//屏幕控制
		screenControlObservable = interval(0,1,TimeUnit.SECONDS)
				.filter(new Func1<Long, Boolean>() {
					@Override
					public Boolean call(Long aLong) {
						boolean result = iv_screen_protector.getVisibility() == View.GONE;
						return result;
					}
				})
				.observeOn(AndroidSchedulers.mainThread())
				.map(new Func1<Long, Integer>() {
					@Override
					public Integer call(Long aLong) {
						screenViewToggle(rollback2ScreenProtectorTime-- <= 0);
						return null;
					}
				})
				.subscribe(new Action1<Integer>() {
					@Override
					public void call(Integer aBoolean) {

					}
				});


		// TODO: 2017/1/23 指纹比对
		startCamera();

		authenticationObservable = getAuthenticationObservable();
	}

	private void screenViewToggle(boolean isOpen){
		iv_screen_protector.setVisibility(isOpen ? View.VISIBLE : View.GONE);
	}

	private StringBuffer inputContent = new StringBuffer();

	private void initViewAddEvent() {

		mLoadingDialog = new SpotsDialog(mContext,R.style.CustomDialogStyle);
		mLoadingDialog.setMessage("验证中");

		//设置键盘
		keyboardView.setKeyboardKeys(KEY);

		keyboardView.setOnClickKeyboardListener(new KeyboardView.OnClickKeyboardListener() {
			@Override
			public void onKeyClick(int position, String value) {

				if (position < 11 && position != 9) {
					inputContent.append(value);
				} else if (position == 9) {
					if(inputContent.length()>0){
						inputContent.deleteCharAt(inputContent.length()-1);
					}
				}else if (position == 11) {
					iv_exit.setVisibility(inputContent.toString().equals(Global.EXIT_CODE) ? View.VISIBLE : View.GONE);
					Toast.makeText(getApplication(), "您的密码是：" + passwordView.getText(), Toast.LENGTH_SHORT).show();
				}
				passwordView.setText(inputContent.toString());
			}
		});
	}

	/**
	 * 将图片转为灰度图
	 *
	 * @param img 指纹图像
	 * @return String
	 */
	public String convertGreyImg(Bitmap img) {
		int width = img.getWidth();
		int height = img.getHeight();
		int[] pixels = new int[width * height];
		img.getPixels(pixels, 0, width, 0, 0, width, height);

		int bai = 0;
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				int grey = pixels[width * i + j];

				int red = ((grey & 0x00FF0000) >> 16);
				int green = ((grey & 0x0000FF00) >> 8);
				int blue = (grey & 0x000000FF);

				if (red == 255 && (green == 255) && blue == 255) {
					bai++;
				}
			}
		}

		// 创建一个数值格式化对象
		NumberFormat numberFormat = NumberFormat.getInstance();
		// 设置精确到小数点后2位
		numberFormat.setMaximumFractionDigits(2);
		String result = numberFormat.format((float) bai / (float) pixels.length
				* 100);
		// Log.e("aa", result + "%");
		return result;
	}

	int w = 800; int h = 600;
	byte[] preview_finger_img_data;

	/** 获取指纹信息的回调对象 */
	private PreviewCallback previewCallback = new PreviewCallback() {
		@Override
		public void onPreviewFrame(byte[] finger_img_data, Camera camera) {
//			Log.d(TAG,"正在预览指纹图像");
			preview_finger_img_data = finger_img_data;
		}
	};

	/**
	 * 打开相机
	 */
	private void startCamera() {

		mSurfaceView.getHolder().addCallback(this);
		mCamera = Camera.open();
		if(mCamera!=null){
			mCamera.setPreviewCallback(previewCallback);
		}else{
			Toast.makeText(mContext, "指纹仪打开失败", Toast.LENGTH_SHORT).show();
			finish();
		}
	}

	private Subscription getAuthenticationObservable(){
		return interval(0,1,TimeUnit.SECONDS)
				.subscribeOn(AndroidSchedulers.mainThread())
				.observeOn(Schedulers.newThread())
				//每次任务开始时判断当前是否正处于一次指纹比对的过程中
				.filter(new Func1<Long, Boolean>() {
					@Override
					public Boolean call(Long time) {
						Log.d(TAG, "call: 判断当前是否处于指纹比对中："+isFPMatching);
						return !isFPMatching;
					}
				})
				.doOnNext(new Action1<Long>() {
					@Override
					public void call(Long aLong) {
						Log.d(TAG, "-------------------------- 当前正在验证 --------------------------------");
						isFPMatching = true;
					}
				})
//				.just(preview_finger_img_data)
				.filter(new Func1<Long, Boolean>() {
					@Override
					public Boolean call(Long intervalTime) {
						Log.d(TAG, "filter: 判断预览到的指纹图像数据是否为空");
						if(preview_finger_img_data ==null){
							Log.d(TAG, "-------------------------- 预览的指纹数据问题，结束本次验证 --------------------------------");
							isFPMatching = false;
							if(mLoadingDialog.isShowing()){
								mLoadingDialog.dismiss();
							}
						}
						return preview_finger_img_data != null ;
					}
				})
				//指纹图像数据转为指纹图像
				.map(new Func1<Long, Bitmap>() {
					@Override
					public Bitmap call(Long intervalTime) {
						Bitmap bitmap;
						YuvImage image;
						ByteArrayOutputStream os;

						image = new YuvImage(preview_finger_img_data, ImageFormat.NV21, w, h, null);

						os = new ByteArrayOutputStream(preview_finger_img_data.length);

						//判断预览数据是否能转为图片，否，预览数据无效
						if (!image.compressToJpeg(new Rect(0, 0, w, h), 100, os)) {
							Log.d(TAG, "-------------------------- 预览的指纹数据问题，结束本次验证1 --------------------------------");
							isFPMatching = false;
							if(mLoadingDialog.isShowing()){
								mLoadingDialog.dismiss();
							}
							return null;
						}

						//预览数据转为Bitmap
						byte[] tmp = os.toByteArray();
						bitmap = BitmapFactory.decodeByteArray(tmp, 0, tmp.length);
						bitmap = Bitmap.createScaledBitmap(bitmap, 256, 360, true);

						try {
							os.close();
						} catch (Exception e) {
							Log.d(TAG, "-------------------------- 预览的指纹数据问题，结束本次验证2 --------------------------------");
							isFPMatching = false;
							if(mLoadingDialog.isShowing()){
								mLoadingDialog.dismiss();
							}
							e.printStackTrace();
						}

						return bitmap;
					}
				})
				.filter(new Func1<Bitmap, Boolean>() {
					@Override
					public Boolean call(Bitmap finger_img_bitmap) {
						if(finger_img_bitmap == null){
							Log.d(TAG, "-------------------------- 预览的指纹数据问题，结束本次验证3 --------------------------------");
							isFPMatching = false;
							if(mLoadingDialog.isShowing()){
								mLoadingDialog.dismiss();
							}
						}
						return finger_img_bitmap != null;
					}
				})
				.map(new Func1<Bitmap, Float>() {
					@Override
					public Float call(Bitmap finger_img_bitmap) {
						float whiteProportion = Float.valueOf(convertGreyImg(finger_img_bitmap));
						Log.d(TAG, "call: 指纹图像白占比："+whiteProportion);
						return whiteProportion;
					}
				})
				//判断指纹质量
				.filter(new Func1<Float, Boolean>() {
					@Override
					public Boolean call(Float whiteProportion) {
						if(whiteProportion>=58){
							Log.d(TAG, "-------------------------- 白占比高于阈值，结束本次验证 --------------------------------");
							isFPMatching = false;
							if(mLoadingDialog.isShowing()){
								mLoadingDialog.dismiss();
							}
						}
						return whiteProportion<58;
					}
				})
//				.debounce(2,TimeUnit.SECONDS)
				//停止预览指纹
				.observeOn(AndroidSchedulers.mainThread())
				.doOnNext(new Action1<Float>() {
					@Override
					public void call(Float whiteProportion) {
						Log.d(TAG, "-------------------------- 成功获取指纹，开始验证 --------------------------------");
						isFPMatching = true;
						if(!mLoadingDialog.isShowing()){
							mLoadingDialog.show();
						}

//						mCamera.startPreview();
//						mSurfaceView.destroyDrawingCache();
//						mCamera.setPreviewCallback(previewCallback);
					}
				})
				.observeOn(Schedulers.newThread())
				//处理指纹图像数据
				.map(new Func1<Float, byte[]>() {
					@Override
					public byte[] call(Float aFloat) {
						Log.d(TAG, "call: 处理指纹图像数据");
						byte[] handled_finger_img_data = new byte[256 * 360];
						Distortion3000.InitAB();
						Distortion3000.ReviseImgDat(handled_finger_img_data, preview_finger_img_data, 800, 600);
						return handled_finger_img_data;
					}
				})
				.observeOn(AndroidSchedulers.mainThread())
				.doOnNext(new Action1<byte[]>() {
					@Override
					public void call(byte[] bytes) {
						Log.d(TAG, "界面提示正在验证指纹: ");
						Toast.makeText(getApplicationContext(), "开始验证指纹", Toast.LENGTH_SHORT).show();
//							if(loadingDialog!=null && !loadingDialog.isShowing()){
//								loadingDialog.show();
//							}
					}
				})
				.observeOn(Schedulers.newThread())
				.map(new Func1<byte[], String>() {
					@Override
					public String call(byte[] bytes) {
						Log.d(TAG, "call: 编码指纹图像");
						return Base64.encodeToString(bytes,Base64.DEFAULT);
					}
				})
				// TODO: 2017/1/23 Retrofit请求指纹服务器
//				.flatMap(new Func1<String, Observable<ResponseEnvelope>>() {
//					@Override
//					public Observable<ResponseEnvelope> call(String fingerbmpBase64) {
//						Log.d(TAG, "call: 上传指纹比对");
//						GetZYRYBHFromFingerprint requestContent = new GetZYRYBHFromFingerprint();
//						requestContent.base64imagefinger = fingerbmpBase64;
//						requestContent.username = Global.FingerprintUserName;
//						requestContent.password = Global.FingerprintPassWord;
//
//						com.elane.wjgjcrygl.retrofit.finger_compare.request.RequestBody requestBody = new com.elane.wjgjcrygl.retrofit.finger_compare.request.RequestBody();
//						requestBody.getZYRYBHFromFingerprint = requestContent;
//
//						RequestEnvelope requestEnvelope = new RequestEnvelope();
//						requestEnvelope.requestBodyNode = requestBody;
//
//						FingerPrintRequestBody fingerPrintRequestBody = new FingerPrintRequestBody();
//						fingerPrintRequestBody.setBody("<?xml version=\"1.0\"?><s:Envelope xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\" ><s:Body><GetZYRYBHFromFingerprint xmlns=\"http://tempuri.org/\"><base64imagefinger>"
//								+fingerbmpBase64+"</base64imagefinger><username>"+Global.FingerprintUserName+"</username><password>"+Global.FingerprintPassWord+"</password></GetZYRYBHFromFingerprint></s:Body></s:Envelope>");
//
//						return FPCompareRetrofitGenerator.getWebserviceInterfaceApi().webserviceRequest(requestEnvelope);
////						return FPComparePostXmlRetrofitGenerator.getWebserviceInterfaceApi().webserviceRequest(fingerPrintRequestBody);
//					}
//				})
//				.map(new Func1<ResponseEnvelope,String>() {
//					@Override
//					public String call(ResponseEnvelope responseEnvelope) {
//						ResponseBody body = responseEnvelope.body;
//						GetZYRYBHFromFingerprintResponse response = body.getZYRYBHFromFingerprintResponse;
//						Log.d(TAG, "call: 解析响应"+response.GetZYRYBHFromFingerprintResult);
//						return response.GetZYRYBHFromFingerprintResult;
//					}
//				})
				.map(new Func1<String, String>() {
					@Override
					public String call(String s) {
						return "444";
					}
				})
				.observeOn(AndroidSchedulers.mainThread())
				.filter(new Func1<String, Boolean>() {
					@Override
					public Boolean call(String fingerCompareResult) {
						Log.d(TAG, "call: 判断指纹比对是否成功");

						if(fingerCompareResult!=null && !fingerCompareResult.equals("")&& !fingerCompareResult.contains("-")){
							Toast.makeText(mContext.getApplicationContext(), "指纹比对成功", Toast.LENGTH_SHORT).show();
//							try{
//								mCamera.startPreview();
//								mSurfaceView.destroyDrawingCache();
//								mCamera.setPreviewCallback(previewCallback);
//							}catch (Exception e){
//								e.printStackTrace();
//							}

							return true;
						}else{
							Toast.makeText(mContext.getApplicationContext(), "指纹比对失败，错误信息："+fingerCompareResult, Toast.LENGTH_SHORT).show();
						}
						Log.d(TAG, "call: 重新预览指纹");
//						try{
//							mCamera.startPreview();
//							mSurfaceView.destroyDrawingCache();
//							mCamera.setPreviewCallback(previewCallback);
//						}catch (Exception e){
//							e.printStackTrace();
//						}
						Log.d(TAG, "-------------------------- 指纹比对失败，本次验证停止 --------------------------------");
						isFPMatching = false;
						if(mLoadingDialog.isShowing()){
							mLoadingDialog.dismiss();
						}
						return false;
					}
				})
				.observeOn(Schedulers.newThread())
				//查询本地是否有该人员信息
				.map(new Func1<String, Object>() {
					@Override
					public Object call(String id) {
						OfficerInfo officerInfo = null;
						try{
							officerInfo = SQLiteOperation.selectPersonByID(mSqLiteDatabase,id);
						}catch (Exception e){
							e.printStackTrace();
						}
						if(officerInfo == null){
							return id;
						}else{
							return officerInfo;
						}
					}
				})
				//判断使用本地数据还是网络获取
				.flatMap(new Func1<Object, Observable<OfficerInfo>>() {
					@Override
					public Observable<OfficerInfo> call(Object object) {
						if(object instanceof String){
							String id = (String) object;
							return getOperatorOfficerDatafromNetObservable(id);
						}else{
							return getOfficerDataObservable((OfficerInfo) object);
						}
					}
				})
				//Retrofit2 Webservice Request officer data
//				.flatMap(new Func1<String, Observable<ResponseEnvelope>>() {
//					@Override
//					public Observable<ResponseEnvelope> call(String zjhm) {
//
//						Log.d(TAG, "网络获取人员信息");
//
//						RequestXml requestXml = new RequestXml();
//						requestXml.xml = "<WS><SERVICE NAME=\"#EAIOS_JYXX\" XLH=\"K2zMnha3X2IbU9j+puXZI4SmZwNw==\"><INPARAM><DWBM TYPE=\"STRING\">520000111</DWBM><ZJHM TYPE=\"STRING\">123456789123456789</ZJHM></INPARAM></SERVICE></WS>";
//
//						com.elane.wjgjcrygl.retrofit.person_info.request.RequestBody requestbody = new com.elane.wjgjcrygl.retrofit.person_info.request.RequestBody();
//						requestbody.requestXml = requestXml;
//
//						RequestEnvelope requestEnvelope = new RequestEnvelope();
//						requestEnvelope.requestBodyNode = requestbody;
//
//						return PersonInfoRetrofitGenerator.getWebserviceInterfaceApi().webRequestPersonInfo(requestEnvelope);
//					}
//				})
//				//Gain Webservice Response
//				.map(new Func1<ResponseEnvelope, String>() {
//					@Override
//					public String call(ResponseEnvelope response) {
//
//						ResponseEnvelope responseEnvelope = response;
//						String result = responseEnvelope.body.returnNode.result;
//
//						// TODO: 2017/2/14 类型转换错误
//						/***
//						 * Error: org.simpleframework.xml.core.ValueRequiredException: Unable to satisfy @org.simpleframework.xml.Element(data=false, name=SERVICE, required=true, type=void) on field 'serviceNode' public com.elane.wjgjcrygl.retrofit.person_info.response.WebServiceService com.elane.wjgjcrygl.retrofit.person_info.response.WebServiceResponse.serviceNode for class com.elane.wjgjcrygl.retrofit.person_info.response.WebServiceResponse at line 1
//						 * 这里暂时手动解析
//						 */
//
//						Log.d(TAG, "得到人员信息响应:"+result.toString());
//						return result.toString();
//					}
//				})
//				//解析Officer Data xml Response
//				.map(new Func1<String, OfficerInfo>() {
//					@Override
//					public OfficerInfo call(String responseXml) {
//						OfficerInfo officerInfo = null;
//						Persister persister = new Persister();
//
//						try {
//							WebServiceResponse response = persister.read(WebServiceResponse.class,responseXml);
//							officerInfo = response.serviceNode.resultNode.dataNode;
//						} catch (Exception e) {
//							e.printStackTrace();
//							authenticationSubscriber.onError(e);
//						}
//						if(officerInfo==null){
//							Log.d(TAG, "解析人员信息失败");
//						}else{
//							Log.d(TAG, "解析得到人员信息："+officerInfo.toString());
//						}
//
//						return officerInfo;
//					}
//				})
//				//信息缓存到本地
//				.doOnNext(new Action1<OfficerInfo>() {
//					@Override
//					public void call(OfficerInfo officerInfo) {
//						SQLiteOperation.replacePersonInfo(mSqLiteDatabase,officerInfo);
//					}
//				})
//		// 从本地或网络获取人员信息
//				.flatMap(new Func1<String, Observable<OfficerInfo>>() {
//					@Override
//					public Observable<OfficerInfo> call(String id) {
//						Log.d(TAG, "Observable.concat");
//						return Observable.concat(getOperatorOfficerDatafromDBObservable(id),getOperatorOfficerDatafromNetObservable(id),getEmptyOfficerObservable());
//					}
//				})
//				.first(new Func1<OfficerInfo, Boolean>() {
//					@Override
//					public Boolean call(OfficerInfo officerInfo) {
//						return officerInfo!=null;
//					}
//				})
				.filter(new Func1<OfficerInfo, Boolean>() {
					@Override
					public Boolean call(OfficerInfo officerInfo) {
						if(officerInfo != null && officerInfo.zjhm!=null && !officerInfo.zjhm.equals("")){
							return true;
						}
						Log.d(TAG, "-------------------------- 通过该证件号码查询人员信息失败，本次验证结束 --------------------------------");
						isFPMatching = false;
						if(mLoadingDialog.isShowing()){
							mLoadingDialog.dismiss();
						}
						Toast.makeText(mContext, "无人员信息", Toast.LENGTH_SHORT).show();
//						authenticationSubscriber.onCompleted();
						return false;
					}
				})
//				.map(new Func1<OfficerInfo, String>() {
//					@Override
//					public String call(OfficerInfo officerInfo) {
//						return officerInfo.xm;
//					}
//				})
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(authenticationSubscriber);

	}

	private Observable<OfficerInfo> getOfficerDataObservable(OfficerInfo officerInfo){
		return Observable.just(officerInfo);
	}

	private Observable<OfficerInfo> getOperatorOfficerDatafromDBObservable(final String id){
		return Observable
				.just(id)
				.map(new Func1<String, OfficerInfo>() {
					@Override
					public OfficerInfo call(String s) {
						OfficerInfo officerInfo = null;
						try{
							officerInfo = SQLiteOperation.selectPersonByID(mSqLiteDatabase,id);
						}catch (Exception e){
							e.printStackTrace();
						}
						return officerInfo;
					}
				});
	}

	private Observable<OfficerInfo> getOperatorOfficerDatafromNetObservable(String id){
		return Observable
				.just(id)
				//Retrofit2 Webservice Request officer data
				.flatMap(new Func1<String, Observable<ResponseEnvelope>>() {
					@Override
					public Observable<ResponseEnvelope> call(String zjhm) {

						Log.d(TAG, "网络获取人员信息");

						RequestXml requestXml = new RequestXml();
						requestXml.xml = "<WS><SERVICE NAME=\"#EAIOS_JYXX\" XLH=\"K2zMnha3X2IbU9j+puXZI4SmZwNw==\"><INPARAM><DWBM TYPE=\"STRING\">520000111</DWBM><ZJHM TYPE=\"STRING\">444</ZJHM></INPARAM></SERVICE></WS>";

						com.elane.wjgjcrygl.retrofit.person_info.request.RequestBody requestbody = new com.elane.wjgjcrygl.retrofit.person_info.request.RequestBody();
						requestbody.requestXml = requestXml;

						RequestEnvelope requestEnvelope = new RequestEnvelope();
						requestEnvelope.requestBodyNode = requestbody;

						return PersonInfoRetrofitGenerator.getWebserviceInterfaceApi().webRequestPersonInfo(requestEnvelope);
					}
				})
				//Gain Webservice Response
				.map(new Func1<ResponseEnvelope, String>() {
					@Override
					public String call(ResponseEnvelope response) {

						ResponseEnvelope responseEnvelope = response;
						String result = responseEnvelope.body.returnNode.result;

						// TODO: 2017/2/14 类型转换错误
						/***
						 * Error: org.simpleframework.xml.core.ValueRequiredException: Unable to satisfy @org.simpleframework.xml.Element(data=false, name=SERVICE, required=true, type=void) on field 'serviceNode' public com.elane.wjgjcrygl.retrofit.person_info.response.WebServiceService com.elane.wjgjcrygl.retrofit.person_info.response.WebServiceResponse.serviceNode for class com.elane.wjgjcrygl.retrofit.person_info.response.WebServiceResponse at line 1
						 * 这里暂时手动解析
						 */

						Log.d(TAG, "得到人员信息响应:"+result.toString());
						return result.toString();
					}
				})
				//解析Officer Data xml Response
				.map(new Func1<String, OfficerInfo>() {
					@Override
					public OfficerInfo call(String responseXml) {
						OfficerInfo officerInfo = null;
						Persister persister = new Persister();

						try {
							WebServiceResponse response = persister.read(WebServiceResponse.class,responseXml);
							officerInfo = response.serviceNode.resultNode.dataNode;
						} catch (Exception e) {
							e.printStackTrace();
							authenticationSubscriber.onError(e);
						}
						if(officerInfo==null){
							Log.d(TAG, "解析人员信息失败");
						}else{
							Log.d(TAG, "解析得到人员信息："+officerInfo.toString());
						}

						return officerInfo;
					}
				})
				//信息缓存到本地
				.doOnNext(new Action1<OfficerInfo>() {
					@Override
					public void call(OfficerInfo officerInfo) {
						SQLiteOperation.replacePersonInfo(mSqLiteDatabase,officerInfo);
					}
				});
	}

	private Subscriber<OfficerInfo> operatorOfficerDataSubscriber = new Subscriber<OfficerInfo>() {
		@Override
		public void onCompleted() {
			Log.d(TAG, "getOperatorOfficerDataSubscriber.onCompleted");
		}

		@Override
		public void onError(Throwable e) {
			Log.d(TAG, "getOperatorOfficerDataSubscriber.onError:"+e.getMessage());
		}

		@Override
		public void onNext(OfficerInfo officerInfo) {
			Log.d(TAG, "getOperatorOfficerDataSubscriber.onNext:"+officerInfo.toString());
		}
	};

	private Subscriber<OfficerInfo> authenticationSubscriber = new Subscriber<OfficerInfo>() {
		@Override
		public void onCompleted() {
			Log.d(TAG, "-------------------------- authenticationSubscriber.onCompleted --------------------------------");
			isFPMatching = false;
			if(mLoadingDialog.isShowing()){
				mLoadingDialog.dismiss();
			}
		}

		@Override
		public void onError(Throwable e) {
			MyApplication.gLogger.info("MainActivity.authenticationSubscriber.onError:"+e.getMessage());
			Toast.makeText(mContext, "身份验证失败\n异常："+e.getMessage(), Toast.LENGTH_SHORT).show();
			isFPMatching = false;
			if(mLoadingDialog.isShowing()){
				mLoadingDialog.dismiss();
			}
			if(!authenticationSubscriber.isUnsubscribed()){
				authenticationSubscriber.unsubscribe();
			}
			authenticationObservable = getAuthenticationObservable();
		}

		@Override
		public void onNext(OfficerInfo s) {
			Log.d(TAG, "-------------------------- 本次验证成功，本次验证结束:"+s.toString() +" --------------------------------");
			isFPMatching = false;
			if(mLoadingDialog.isShowing()){
				mLoadingDialog.dismiss();
			}
			mLoadingDialog.show();
			Toast.makeText(mContext, "身份验证成功", Toast.LENGTH_SHORT).show();
		}
	};

	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		if (mCamera != null) {
			Camera.Parameters params = mCamera.getParameters();
			// 得到设备支持的尺寸,并选择第一个(最大)
			List<Camera.Size> sizes = params.getSupportedPictureSizes();
			Camera.Size selected = sizes.get(0);
			params.setPreviewSize(selected.width, selected.height);
			// 设置拍完照输出的照片方向
			params.setRotation(0);
			mCamera.setParameters(params);
			// 设置视频采集前预览数据的绘制方向
			mCamera.setDisplayOrientation(0);
			mCamera.startPreview();
		}
	}

	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		try {
			mCamera.setPreviewDisplay(mSurfaceView.getHolder());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
	}

	@Override
	public void onAutoFocus(boolean arg0, Camera arg1) {
	}

	@Override
	public void onPictureTaken(byte[] data, Camera arg1) {
	}

	@Override
	public void onShutter() {
	}
	Ws_Zw ws = null;

	/**
	 * 将字符串转换成Bitmap类型
	 * */
	public Drawable stringtoBitmap(String string) {
		Drawable drawable = null;
		Bitmap bitmap = null;
		try {
			byte[] bitmapArray;
			bitmapArray = Base64.decode(string, Base64.DEFAULT);
			bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0,
					bitmapArray.length);
			drawable = new BitmapDrawable(bitmap);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return drawable;
	}

	/**
	 * 数据库中有该人员指纹 通过eudap获取人员姓名
	 */
	private void requstEudapData() {
		try {
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("XLH", Global.eudap_lisence);
			params.put("SERVICENAME", "#EAIOS_JYXX#");
			params.put("ZJHM", Global.rybh);
			params.put("DWBM", Global.jsbh);

			String requestXML = StringUtil.inputStreamToString(getAssets()
					.open("request_body_officer_info.xml"), params);
			Log.e("EUDAPXML", requestXML);

//			WebServiceUtil.getSoapObject(requestXML, handler);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		// 界面销毁时，关闭相机
		if (mCamera != null) {
			mCamera.setPreviewCallback(null);
			mCamera.stopPreview();
			mCamera.release();
			mCamera = null;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(screenControlObservable.isUnsubscribed()){
			screenControlObservable.unsubscribe();
		}
		if(authenticationObservable.isUnsubscribed()){
			authenticationObservable.unsubscribe();
		}

	}

	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(0, R.anim.base_slide_right_out);
	}

	/**
	 * 查询数据中的密码进行比对
	 * */
	public void selectPWD(final Handler handler) {
		final Message msg = new Message();
		msg.what = 12;

		MyApplication.gLogger.info("请求"+Global.oracle_ip+"/"+Global.oracle_port+"/"+Global.oracle_sid+"/"+Global.oracle_username+"/"+Global.oracle_password+" 数据："+Global.servlet_url);

		RequestBody formBody = new FormBody.Builder()
				.add("ip", Global.oracle_ip)
				.add("port", Global.oracle_port)
				.add("sid", Global.oracle_sid)
				.add("username", Global.oracle_username)
				.add("password", Global.oracle_password)
				.build();

		Request request = new Request.Builder()
				.url(Global.servlet_url)
				.addHeader("Content-Type","text/json; Charset=UTF-8")
				.post(formBody)
				.build();

//		try {
//			mOkHttpClient.newCall(request).enqueue(new Callback() {
//				@Override
//				public void onFailure(Call call, IOException e) {
//					msg.obj = "连接数据库服务失败";
//					handler.sendMessage(msg);
//				}
//
//				@Override
//				public void onResponse(Call call, Response response) throws IOException {
//					String res = ""+response.body().string();
//
//					msg.what = 12;
//					msg.obj = res;
//					handler.sendMessage(msg);
//				}
//			});
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		new Thread(new Runnable() {

			@Override
			public void run() {
//				Message msg = new Message();
//				msg.what = 12;
//				try {
//					List<NameValuePair> params = new ArrayList<NameValuePair>();
//					params.add(new BasicNameValuePair("ip", Global.oracle_ip));
//					params.add(new BasicNameValuePair("port",
//							Global.oracle_port));
//					params.add(new BasicNameValuePair("sid", Global.oracle_sid));
//					params.add(new BasicNameValuePair("username",
//							Global.oracle_username));
//					params.add(new BasicNameValuePair("password",
//							Global.oracle_password));
//					String pwd = HttpUtils.getResponseByHttpPost(
//							Global.servlet_url, params);
//					msg.obj = pwd;
//				} catch (Exception e) {
//					msg.obj = "连接数据库服务失败";
//					e.printStackTrace();
//				}
//				handler.sendMessage(msg);


			}
		}).start();

	}

}
