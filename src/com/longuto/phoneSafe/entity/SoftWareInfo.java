package com.longuto.phoneSafe.entity;

import android.graphics.drawable.Drawable;

/**
 * �����Ϣʵ����
 * @author longuto
 *
 */
public class SoftWareInfo {
	private Drawable icon; 	// ͼ��
	private String name;	// �������
	private String packageName;	// ������
	private int versionCode;	// ����汾��
	private boolean isUserd;	// �Ƿ����û�����
	private boolean isSdCard;	// �Ƿ����ֻ�����Ӧ��
	
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
