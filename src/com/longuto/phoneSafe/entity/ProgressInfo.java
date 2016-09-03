package com.longuto.phoneSafe.entity;

import android.graphics.drawable.Drawable;

/**
 * 线程的实体类
 * @author longuto
 *
 */
public class ProgressInfo {
	private String name;	// 名称
	private Drawable icon;	// 图标
	private long memory;	// 大小
	private String packname;	// 包名称
	private boolean isUserd;	// 是否用户
	private boolean isChecked;	// 是否选中
	
	public ProgressInfo() {}
	public ProgressInfo(String name, Drawable icon, long memory, String packname, boolean isUserd, boolean isChecked) {
		this.name = name;
		this.icon = icon;
		this.memory = memory;
		this.packname = packname;
		this.isUserd = isUserd;
		this.isChecked = isChecked;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Drawable getIcon() {
		return icon;
	}
	public void setIcon(Drawable icon) {
		this.icon = icon;
	}
	public long getMemory() {
		return memory;
	}
	public void setMemory(long memory) {
		this.memory = memory;
	}
	public String getPackname() {
		return packname;
	}
	public void setPackname(String packname) {
		this.packname = packname;
	}
	public boolean isUserd() {
		return isUserd;
	}
	public void setUserd(boolean isUserd) {
		this.isUserd = isUserd;
	}
	public boolean isChecked() {
		return isChecked;
	}
	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}
}
