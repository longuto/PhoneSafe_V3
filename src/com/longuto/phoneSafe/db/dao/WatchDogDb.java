package com.longuto.phoneSafe.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.longuto.phoneSafe.db.WatchDogDbHelp;

/**
 * 看门狗的数据库操作
 * @author longuto
 */
public class WatchDogDb {
	public static final String TABLE = "T_WATCHDOG";	//数据库表
	public static final String ID = "_id";	//加了看门狗的软件id
	public static final String PACKNAME = "W_PACKAGENAME";	//加了看门狗软件的包名
	
	private Context context;	// 上下文
	private WatchDogDbHelp dbHelp;	// 数据库OpenHelp
	
	public WatchDogDb(Context context) {
		dbHelp = new WatchDogDbHelp(context);
		this.context = context;
	}
	
	/**
	 * 按照包名查询指定的软件是否存在
	 * @param packName 指定的包名
	 * @return 存在返回true, 否则返回false
	 */
	public boolean queryWatchDogByPackName(String packName) {
		boolean flag = false;
		SQLiteDatabase db = dbHelp.getReadableDatabase();
		Cursor cursor = db.query(TABLE, new String[]{ID, PACKNAME}, PACKNAME + "=?", new String[]{packName}, null, null, null);
		if(cursor.moveToNext()) {
			flag = true;
		}
		cursor.close();	// 关闭游标
		db.close();	// 关闭DataBase
		return flag;
	}
	
	/**
	 * 根据指定包名,将其加入数据库
	 * @param packName 指定包名
	 */
	public void addWatchDogByPackName(String packName) {
		SQLiteDatabase db = dbHelp.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(PACKNAME, packName);
		db.insert(TABLE, null, values);
		db.close();
	}
	
	/**
	 * 根据指定的包名,将其从数据库中删除
	 * @param packName 指定包名
	 */
	public void delWatchDogByPackName(String packName) {
		SQLiteDatabase db = dbHelp.getWritableDatabase();	
		db.delete(TABLE, PACKNAME + "=?", new String[]{packName});
		db.close();
	}
}
