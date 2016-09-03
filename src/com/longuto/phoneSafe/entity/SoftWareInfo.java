package com.longuto.phoneSafe.entity;

import android.graphics.drawable.Drawable;

/**
 * 软件信息实体类
 * @author longuto
 *
 */
public class SoftWareInfo {
	private Drawable icon; 	// 图标
	private String name;	// 软件名称
	private String packageName;	// 包名称
	private int versionCode;	// 软件版本号
	private boolean isUserd;	// 是否是用户程序
	private boolean isSdCard;	// 是否是手机卡的应用
	
	public SoftWareInfo() {}
	
	public SoftWareInfo(Drawable icon, String name, int versionCode, String packageName, boolean isUserd, boolean isSdCard) {
		this.icon = icon;
		this.name = name;
		this.versionCode = versionCode;
		this.packageName = packageName;
		this.isUserd = isUserd;
		this.isSdCard = isSdCard;
	}

	public Drawable getIcon() {
		return icon;
	}
	public void setIcon(Drawable icon) {
		this.icon = icon;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getVersionCode() {
		return versionCode;
	}
	public void setVersionCode(int versionCode) {
		this.versionCode = versionCode;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public boolean isUserd() {
		return isUserd;
	}
	public void setUserd(boolean isUserd) {
		this.isUserd = isUserd;
	}
	public boolean isSdCard() {
		return isSdCard;
	}
	public void setSdCard(boolean isSdCard) {
		this.isSdCard = isSdCard;
	}
	
	
}
