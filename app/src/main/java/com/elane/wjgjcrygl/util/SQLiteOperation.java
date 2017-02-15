package com.elane.wjgjcrygl.util;
/**
 * 1 建立数据库
 * 2 建立数据库表（ "T_D_APPINFO";  // 应用信息表
 "T_B_BASE"; // 基础参数配置表
 "T_B_REDIS_OPTION";  // Redis连接配置表）
 3 对每个数据库表需要曾，删，改，查的数据表操作工具类
 */

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.elane.wjgjcrygl.Global;
import com.elane.wjgjcrygl.MyApplication;
import com.elane.wjgjcrygl.model.OfficerInfo;

public class SQLiteOperation extends SQLiteOpenHelper {

	private final static String TAG = "SQLiteOperation";

	public SQLiteOperation(Context context, String name, CursorFactory factory,
						   int version) {
		super(context, name, factory, version);
	}

	public SQLiteOperation(Context context, String DBname, int version) {
		this(context, DBname, null, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		//建人员照片数据表
		db.execSQL("CREATE TABLE if NOT EXISTS "+ Global.TABLE_NAME_PERSION_INFO+ "("
				+" ZJHM varchar(50) PRIMARY KEY,"
				+ "JOB varchar(30),"
				+ "XM varchar(30),"
				+ "ZPSJ BLOB,"
				+ "DWBM varchar(30),"
				+ "DWMC varchar(30),"
				+ "XBMC varchar(10)"
				+")"
		);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.e("db", newVersion + "");
		MyApplication.gLogger.debug("db " +newVersion + "");

	}

	/**根据证件号码查询人员是否存在*/
	public static OfficerInfo selectPersonByID(SQLiteDatabase db, String IDNumber){
		OfficerInfo officerInfo = null;
		String sql = "SELECT * FROM " + Global.TABLE_NAME_PERSION_INFO +" where ZJHM = '"+IDNumber+"'";
		Log.d(TAG, "查询人员信息："+sql);
		Cursor cursor = null;
		try {
			cursor = db.rawQuery(sql, null);
			String xm = "";
			String zjhm =	"";
			byte[] zpsj;
			String job = "";
			while (cursor.moveToNext()) {
				xm = cursor.getString(cursor.getColumnIndex("XM"));
				zjhm = cursor.getString(cursor.getColumnIndex("ZJHM"));
				zpsj = cursor.getBlob(cursor.getColumnIndex("ZPSJ"));
				job = cursor.getString(cursor.getColumnIndex("JOB"));

				officerInfo = new OfficerInfo();

				officerInfo.xm = xm;
				officerInfo.job = job;
				officerInfo.zjhm = zjhm;
				officerInfo.zpsj = ImageUtil.byteArray2Str(zpsj);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(cursor!=null){
				cursor.close();
			}
		}
		if(officerInfo==null){
			Log.d(TAG, "本地未查询到人员信息");
		}else{
			Log.d(TAG, "本地查询到人员信息："+officerInfo.toString());
		}
		return officerInfo;
	}


	/**
	 * 更新人员信息
	 * @param db
	 * @param officerInfo
	 */
	public static void replacePersonInfo(SQLiteDatabase db, OfficerInfo officerInfo){
		if(officerInfo == null){
			return;
		}
		String sql = "REPLACE INTO " +Global.TABLE_NAME_PERSION_INFO + "(ZJHM,XM,JOB,ZPSJ,DWBM,DWMC,XBMC) VALUES ('"
				+officerInfo.zjhm+"','"+officerInfo.xm+"','"+officerInfo.job+"','"+officerInfo.zpsj+"','"+officerInfo.jsbh+"','"+officerInfo.dwmc+"','"+officerInfo.xb+"')";
		Log.d("SQLiteOperation", "insert or update sql:"+sql);
		try {
			db.execSQL(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
