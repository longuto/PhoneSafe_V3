package com.longuto.phoneSafe.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class AddressManager {
	/**
	 * 通过手机号码和上下文查询数据库 -- 数据库文件地址在手机内存files文件下
	 * @param num 手机号码
	 * @param context 上下文
	 * @return 手机号码归属地
	 */
	public static String queryLocation(String num, Context context) {
		String numLocation = ""; 	// 定义一个字符串变量表示手机号码归属地
		String addressPath = context.getFilesDir() + "/address.db";		// 文件路径
		SQLiteDatabase db = SQLiteDatabase.openDatabase(addressPath, null, SQLiteDatabase.OPEN_READONLY);	// 获取db
		// 如果匹配手机号码
		if(num.matches("^1[345678]\\d{9}$")) {
			// 获取查询游标
			Cursor cursor = db.rawQuery("select location from data2 where id in (select outkey from data1 where id=?)", new String[]{num.substring(0, 7)});
			if(cursor.moveToNext()) {
				numLocation = cursor.getString(0);
			}
		}else {	
			int len = num.length();	// 获取手机号码长度
			switch (num.length()) {
			case 3:
				numLocation = "警匪台";
				break;
			case 4:
				numLocation = "虚拟号码";
				break;
			case 5:
				numLocation = "服务号码";
				break;
			case 7:
			case 8:
				numLocation = "本地座机";
				break;
			default:
				// 长途座机
				if(num.startsWith("0")) {
					if(len >= 10) {
						Cursor c1 = db.rawQuery("select location from data2 where area=?", new String[]{num.substring(1, 4)});
						if(c1.moveToNext()) {
							numLocation = c1.getString(0);
						}else {
							Cursor c2 = db.rawQuery("select location from data2 where area=?", new String[]{num.substring(1, 3)});
							if(c2.moveToNext()) {
								numLocation = c2.getString(0);
							}
						}
					}
				}
				break;
			}
		}
		return numLocation;
	}
}
