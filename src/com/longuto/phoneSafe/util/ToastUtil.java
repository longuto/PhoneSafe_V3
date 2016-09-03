package com.longuto.phoneSafe.util;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {
	/**
	 * 吐司工具类
	 * @param context 上下文
	 * @param string 需要显示的字符串
	 */
	public static void show(Context context, String string) {
		Toast.makeText(context, string, Toast.LENGTH_SHORT).show();	
	}

}
