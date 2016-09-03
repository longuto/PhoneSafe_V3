package com.longuto.phoneSafe.entity;
/**
 * 
 * @author longuto 黑名单实体类
 *
 */
public class BackNumInfo {
	private int id;		// 编号
	private String num;	// 电话号码
	private int mode;	// 模式
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}
	public int getMode() {
		return mode;
	}
	public void setMode(int mode) {
		this.mode = mode;
	}
}
