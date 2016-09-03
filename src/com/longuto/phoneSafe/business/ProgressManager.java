package com.longuto.phoneSafe.business;

import java.util.ArrayList;
import java.util.List;

import com.longuto.phoneSafe.entity.ProgressInfo;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.os.Debug.MemoryInfo;

public class ProgressManager {
	
	public static List<ProgressInfo> getAllProgress(Context context) {
		List<ProgressInfo> data = new ArrayList<ProgressInfo>();
		// 获取Activity管理器
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		// 获取包管理器
		PackageManager pm = context.getPackageManager();

		List<RunningAppProcessInfo> Processes = am.getRunningAppProcesses();	// 获取所有的进程
		for (RunningAppProcessInfo info : Processes) {
			String packageName = info.processName;	// 进程的包名
			MemoryInfo[] memoryInfos = am.getProcessMemoryInfo(new int[]{info.pid});	// 获取进程集合对应的内存集合
			long memory = memoryInfos[0].getTotalPss() / 1024;
			try {
				ApplicationInfo applicationInfo = pm.getApplicationInfo(packageName, 0);
				String name = (String) applicationInfo.loadLabel(pm);	// 当前进程的应用名称
				Drawable icon = applicationInfo.loadIcon(pm);	// 当前进程的应用图标
				boolean isUserd = true;
				if((applicationInfo.flags&ApplicationInfo.FLAG_SYSTEM) == ApplicationInfo.FLAG_SYSTEM) {
					isUserd = false;
				}
				boolean isChecked = false;
				ProgressInfo temp = new ProgressInfo(name, icon, memory, packageName, isUserd, isChecked);
				data.add(temp);
			} catch (NameNotFoundException e) {
				e.printStackTrace();
			}			
		}
		return data;
	}
}
