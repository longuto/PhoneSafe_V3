package com.longuto.phoneSafe.entity;

/**
 * 联系人实体类
 * @author longuto
 */
public class Contact {
	private String name;	// 联系人姓名
	private String phoneNum;	// 联系人号码
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhoneNum() {
		return phoneNum;
	}
	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}
}
