package com.elane.wjgjcrygl.util;


import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;



public class MessageUtil {

	private static Toast mToast;
	
	public static void messageUtil(Context context, String text){
		if(mToast==null){
			mToast=Toast.makeText(context, text, Toast.LENGTH_SHORT);
		}
		mToast.setGravity(Gravity.CENTER,0,0);
		mToast.setText(text);
		mToast.show();
	}
	
}
