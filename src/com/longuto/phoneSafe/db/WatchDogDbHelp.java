package com.longuto.phoneSafe.db;

import com.longuto.phoneSafe.util.LogUtil;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 关于手机软件看门狗数据库的DbHelp创建
 * @author longuto
 */
public class WatchDogDbHelp extends SQLiteOpenHelper {
	public static final String DATABASENAME = "watchDog.db";
	public static final int VERSION = 1;
	private static final String TAG = "WatchDogDbHelp"; 
	
	private Context context;	// 上下文

	public WatchDogDbHelp(Context context) {
		super(context, DATABASENAME, null, VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		LogUtil.v(TAG, "onCreate");
		String sql = "CREATE TABLE T_WATCHDOG (_id INTEGER PRIMARY KEY AUTOINCREMENT, W_PACKAGENAME TEXT NOT NULL UNIQUE)";
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		LogUtil.v(TAG, "onUpgrade");
	}

}
