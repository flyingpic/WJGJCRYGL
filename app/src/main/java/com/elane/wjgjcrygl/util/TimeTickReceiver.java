package com.elane.wjgjcrygl.util;


import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Date;
import java.util.List;

public class TimeTickReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		
		if(intent.getAction().equals(Intent.ACTION_TIME_TICK)){
			if(new Date().getHours() == 0 && new Date().getMinutes() == 0){

			}
			ActivityManager activityManger = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);// 获取Activity管理器
			List<RunningServiceInfo> serviceList = activityManger.getRunningServices(30);// 从窗口管理器中获取正在运行的Service
			Log.i("TimeTickReceiver", "获取到了系统时间广播");
			if(!ServiceIsStart(serviceList,"com.elane.iit.listen.ListenService")){
				try {
					Intent service =new Intent();
					service.setComponent(new ComponentName("com.elane.iit.listen", "com.elane.iit.listen.ListenService"));
					context.startService(service);
					Log.i("TimeTickReceiver", "启动了守护进程");
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}
	}
	private boolean ServiceIsStart(List<RunningServiceInfo> list, String className) {// 判断某个服务是否启动
		
		for (int i = 0; i < list.size(); i++) {
			if (className.equals(list.get(i).service.getClassName()))
				return true;
		}
		return false;
	}
}
