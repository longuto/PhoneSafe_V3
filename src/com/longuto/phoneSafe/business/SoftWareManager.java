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
	 * ��ȡ���а���Ϣ
	 * @return ���ذ���Ϣ����
	 */
	public static List<SoftWareInfo> getAllSoftWares(Context context) {
		List<SoftWareInfo> softWares = new ArrayList<SoftWareInfo>();
		PackageManager manager = context.getPackageManager();	// ��ȡ��������
		List<PackageInfo> packageInfos = manager.getInstalledPackages(0);	// ��ȡ���а���Ϣ����
		for (PackageInfo info : packageInfos) {
			Drawable icon = info.applicationInfo.loadIcon(manager);	//Ӧ�õ�ͼ��
			String name = (String) info.applicationInfo.loadLabel(manager);	// Ӧ������
			int versionCode = info.versionCode;	// �汾����
			String packageName = info.packageName;	// ������
			boolean isUserd = true;	// Ĭ�����û�����
			boolean isSdCard = false;	// Ĭ�ϲ���SDCard����
			int flags = info.applicationInfo.flags;	// Ӧ�ñ�־
			//��ͨ����־λ���ж��Ƿ�Ϊϵͳ����
			if((flags&ApplicationInfo.FLAG_SYSTEM) == ApplicationInfo.FLAG_SYSTEM) {
				isUserd = false;
			}
			// ͨ����־λ�ж��Ƿ����û�����
			if((flags&ApplicationInfo.FLAG_EXTERNAL_STORAGE) == ApplicationInfo.FLAG_EXTERNAL_STORAGE) {
				isSdCard = true;
			}
			SoftWareInfo temp = new SoftWareInfo(icon, name, versionCode, packageName, isUserd, isSdCard);
			softWares.add(temp);
		}
		return softWares;	
	}
}
