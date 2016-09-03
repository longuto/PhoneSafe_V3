package com.longuto.phoneSafe.util;

import android.util.Log;

public class LogUtil {
	private static boolean isDebug = true;	// 是否处于debug模式
	
	public static void i(String tag, String msg) {
		if(isDebug) {
			Log.i(tag, msg);
		}
	}
	
	public static void v(String tag, String msg) {
		if(isDebug) {
			Log.v(tag, msg);
		}
	}
	
	public static void w(String tag, String msg) {
		if(isDebug) {
			Log.w(tag, msg);
		}
	}
	
	
}
