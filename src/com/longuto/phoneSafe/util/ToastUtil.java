package com.longuto.phoneSafe.util;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {
	/**
	 * ��˾������
	 * @param context ������
	 * @param string ��Ҫ��ʾ���ַ���
	 */
	public static void show(Context context, String string) {
		Toast.makeText(context, string, Toast.LENGTH_SHORT).show();	
	}

}
