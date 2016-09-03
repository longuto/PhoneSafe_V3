package com.longuto.phoneSafe.util;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;

public class ServiceStateUtil {

	/**
	 * �жϷ����Ƿ�������
	 * @param context
	 * @param serviceName ����İ��� 
	 * @return
	 */
	public static boolean isServiceRunning(Context context, String serviceName) {
		// ��ȡActivity������
		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		// ��ȡ���100�����񼯺�
		List<RunningServiceInfo> services = activityManager.getRunningServices(100);
		for (RunningServiceInfo temp : services) {
			if(serviceName.equals(temp.service.getClassName())) {
				return true;
			}
		}
		return false;
	}
	
}
