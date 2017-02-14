package com.elane.wjgjcrygl.db;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DBManager {
	private DBOpenhelper helper;
	private SQLiteDatabase db;

	public DBManager(Context context) {
		helper = new DBOpenhelper(context);
		// 因为getWritableDatabase内部调用了mContext.openOrCreateDatabase(mName, 0,
		// mFactory);
		// 所以要确保context已初始化,我们可以把实例化DBManager的步骤放在Activity的onCreate里
		db = helper.getWritableDatabase();
	}

	// 插入新记录
	public void insertJiLu(String id, String name, String zhiwei, String zhaop) {
		db.execSQL("INSERT INTO mjxx(id ,name,zhiwei,zhaop) values (?,?,?,?)", new String[] { id, name, zhiwei, zhaop });
		Log.i("插入人员信息", "------------------成功");
	}

	/***/
	public void closeDB(){
		db.close();
	}
}
