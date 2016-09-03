package com.longuto.phoneSafe.business;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import com.longuto.phoneSafe.entity.SoftWareInfo;

public class SoftWareManager {
	
	/**
	 * 获取所有包信息
	 * @return 返回包信息集合
	 */
	public static List<SoftWareInfo> getAllSoftWares(Context context) {
		List<SoftWareInfo> softWares = new ArrayList<SoftWareInfo>();
		PackageManager manager = context.getPackageManager();	// 获取包管理者
		List<PackageInfo> packageInfos = manager.getInstalledPackages(0);	// 获取所有包信息集合
		for (PackageInfo info : packageInfos) {
			Drawable icon = info.applicationInfo.loadIcon(manager);	//应用的图标
			String name = (String) info.applicationInfo.loadLabel(manager);	// 应用名称
			int versionCode = info.versionCode;	// 版本名称
			String packageName = info.packageName;	// 包名称
			boolean isUserd = true;	// 默认是用户程序
			boolean isSdCard = false;	// 默认不是SDCard程序
			int flags = info.applicationInfo.flags;	// 应用标志
			//　通过标志位，判断是否为系统程序
			if((flags&ApplicationInfo.FLAG_SYSTEM) == ApplicationInfo.FLAG_SYSTEM) {
				isUserd = false;
			}
			// 通过标志位判断是否是用户程序
			if((flags&ApplicationInfo.FLAG_EXTERNAL_STORAGE) == ApplicationInfo.FLAG_EXTERNAL_STORAGE) {
				isSdCard = true;
			}
			SoftWareInfo temp = new SoftWareInfo(icon, name, versionCode, packageName, isUserd, isSdCard);
			softWares.add(temp);
		}
		return softWares;	
	}
}
