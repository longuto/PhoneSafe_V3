package com.longuto.phoneSafe.util;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;

public class ServiceStateUtil {

	/**
	 * 判断服务是否在运行
	 * @param context
	 * @param serviceName 服务的包名 
	 * @return
	 */
	public static boolean isServiceRunning(Context context, String serviceName) {
		// 获取Activity管理者
		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		// 获取最多100个服务集合
		List<RunningServiceInfo> services = activityManager.getRunningServices(100);
		for (RunningServiceInfo temp : services) {
			if(serviceName.equals(temp.service.getClassName())) {
				return true;
			}
		}
		return false;
	}
	
}
