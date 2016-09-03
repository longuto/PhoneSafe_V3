package com.longuto.phoneSafe.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Xmlϵͳ�����ļ�
 * @author longuto
 */

public class PhoneSafeConfig {
	public static final String CONFIG = "phoneSafe_config";	//Ӧ�õ�xml�����ļ�
	public static final String IS_FIRST_USED = "is_first_used";	// �Ƿ��һ��ʹ�ñ�App
	public static final String AUTO_UPDATE = "auto_update";	//�汾�� 
	public static final String SERVER_INFO_URL = "http://10.0.2.2:8080/updateInfo.json";
	public static final String SAFE_PASSWORD = "safe_password";	// �������ܵ�����
	public static final String IS_FIRST_SETUP = "is_first_setup";	// �Ƿ��һ�����÷�����
	public static final String BIND_SIM_SERIAL = "bind_sim_serial";	// ���ֻ��������к�
	public static final String SAFE_PHONENUM = "safe_phonenum";	// ��ȫ�ֻ�����
	public static final String IS_OPEN_SAFELOST = "is_open_safelost";	// �Ƿ�����������
	public static final String PHONELATITUDE = "phoneLatitude";	// �ֻ�γ��
	public static final String PHONELONGITUDE = "phoneLongitude";	// �ֻ�����
	public static final String TOASTSTYLE = "toastStyle";	// �ֻ�����
	public static final String IS_OPEN_WATCHDOG = "is_open_watchdog";	// ���Ź������Ƿ���
	
	private SharedPreferences sp;	//SharedPreferences����
	
	// ���췽��
	public PhoneSafeConfig(Context context, String name) {
		sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
	}
	
	// ��ȡSharedPreferences����
	public SharedPreferences getSharedPreferences() {
		return sp;
	}
	
	// �洢����ֵ��Xml�ļ�
	public void putBooleanToXml(String key, Boolean value) {
		sp.edit().putBoolean(key, value).commit();
	}
	
	// ��ȡXml�еĲ���ֵ,Ĭ�Ϸ���defvalue
	public boolean getBooleanFromXml(String key, Boolean defValue) {
		return sp.getBoolean(key, defValue);
	}
	
	// ��ȡXml�еĲ���ֵ,Ĭ�Ϸ���false
	public boolean getBooleanDefalse(String key) {
		return sp.getBoolean(key, false);
	}
	
	// �洢�ַ�����Xml�ļ�
	public void putStringToXml(String key, String value) {
		sp.edit().putString(key, value).commit();
	}
	
	// ��ȡXml�е��ַ���,Ĭ�Ϸ���defValue
	public String getStringFromXml(String key, String defValue) {
		return sp.getString(key, defValue);
	}
	
	// ��ȡXml�е��ַ���,Ĭ�Ϸ���null
	public String getStringFromXml(String key) {
		return sp.getString(key, null);
	}
	
	// �洢������Xml�ļ�
	public void putIntToXml(String key, int value) {
		sp.edit().putInt(key, value).commit();
	}
	
	// ��ȡXml�е�����,Ĭ�Ϸ���defValue
	public int getIntFromXml(String key, int defValue) {
		return sp.getInt(key, defValue);
	}
	
	// ��ȡXml�е�����,Ĭ�Ϸ���0
	public int getIntFromXml(String key) {
		return sp.getInt(key, 0);
	}
}
