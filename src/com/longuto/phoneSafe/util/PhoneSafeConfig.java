package com.longuto.phoneSafe.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Xml系统配置文件
 * @author longuto
 */

public class PhoneSafeConfig {
	public static final String CONFIG = "phoneSafe_config";	//应用的xml配置文件
	public static final String IS_FIRST_USED = "is_first_used";	// 是否第一次使用本App
	public static final String AUTO_UPDATE = "auto_update";	//版本号 
	public static final String SERVER_INFO_URL = "http://10.0.2.2:8080/updateInfo.json";
	public static final String SAFE_PASSWORD = "safe_password";	// 防盗功能的密码
	public static final String IS_FIRST_SETUP = "is_first_setup";	// 是否第一次设置防盗向导
	public static final String BIND_SIM_SERIAL = "bind_sim_serial";	// 绑定手机卡的序列号
	public static final String SAFE_PHONENUM = "safe_phonenum";	// 安全手机号码
	public static final String IS_OPEN_SAFELOST = "is_open_safelost";	// 是否开启防盗功能
	public static final String PHONELATITUDE = "phoneLatitude";	// 手机纬度
	public static final String PHONELONGITUDE = "phoneLongitude";	// 手机经度
	public static final String TOASTSTYLE = "toastStyle";	// 手机经度
	public static final String IS_OPEN_WATCHDOG = "is_open_watchdog";	// 看门狗功能是否开启
	
	private SharedPreferences sp;	//SharedPreferences对象
	
	// 构造方法
	public PhoneSafeConfig(Context context, String name) {
		sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
	}
	
	// 获取SharedPreferences对象
	public SharedPreferences getSharedPreferences() {
		return sp;
	}
	
	// 存储布尔值至Xml文件
	public void putBooleanToXml(String key, Boolean value) {
		sp.edit().putBoolean(key, value).commit();
	}
	
	// 获取Xml中的布尔值,默认返回defvalue
	public boolean getBooleanFromXml(String key, Boolean defValue) {
		return sp.getBoolean(key, defValue);
	}
	
	// 获取Xml中的布尔值,默认返回false
	public boolean getBooleanDefalse(String key) {
		return sp.getBoolean(key, false);
	}
	
	// 存储字符串至Xml文件
	public void putStringToXml(String key, String value) {
		sp.edit().putString(key, value).commit();
	}
	
	// 获取Xml中的字符串,默认返回defValue
	public String getStringFromXml(String key, String defValue) {
		return sp.getString(key, defValue);
	}
	
	// 获取Xml中的字符串,默认返回null
	public String getStringFromXml(String key) {
		return sp.getString(key, null);
	}
	
	// 存储整型至Xml文件
	public void putIntToXml(String key, int value) {
		sp.edit().putInt(key, value).commit();
	}
	
	// 获取Xml中的整型,默认返回defValue
	public int getIntFromXml(String key, int defValue) {
		return sp.getInt(key, defValue);
	}
	
	// 获取Xml中的整型,默认返回0
	public int getIntFromXml(String key) {
		return sp.getInt(key, 0);
	}
}
